package sz.lab.utils;

import cn.hutool.core.util.ObjectUtil;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class MinioUtil {
    @Autowired
    private MinioClient minioClient;
    @Value("${minio.bucket}")
    private String bucket;
    @Value("${minio.server}")
    private String server;
    @Value("${minio.port}")
    private int port;

    @Resource
    private LinkShortUtil linkShortUtil;
    @SneakyThrows
    public boolean existBucket(String name) {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
        }
        return exists;
    }

    /**
     * 创建存储bucket
     *
     * @param bucketName 存储bucket名称
     * @return Boolean
     */
    @SneakyThrows
    public Boolean makeBucket(String bucketName) {
        boolean exist = existBucket(bucketName);
        if (!exist) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
            return true;
        }
        return false;
    }
    /**
     * 删除存储bucket
     *
     * @param bucketName 存储bucket名称
     * @return Boolean
     */
    @SneakyThrows
    public Boolean removeBucket(String bucketName) {
        boolean exist = existBucket(bucketName);
        if (!exist) return false;
        Iterable<Result<Item>> results =
                minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        for (Result<Item> result : results) {
            Item item = result.get();
            // 桶不为空不允许删除
            if (item.size() > 0) {
                return false;
            }
        }
        minioClient.removeBucket(RemoveBucketArgs.builder()
                .bucket(bucketName)
                .build());
        return true;
    }
    /**
     * 列出所有存储桶
     */
    @SneakyThrows
    public List<Bucket> listBuckets() {
        return minioClient.listBuckets();
    }
    /**
     * 列出所有存储桶名称
     */
    public List<String> listBucketNames() {
        List<Bucket> bucketList = listBuckets();
        if (ObjectUtil.isEmpty(bucketList))
            return null;
        List<String> bucketListName = new ArrayList<>();
        for (Bucket bucket : bucketList) {
            bucketListName.add(bucket.name());
        }
        return bucketListName;
    }

    /**
     * 列出存储桶中的所有对象名称
     *
     * @param bucketName 存储桶名称
     */
    @SneakyThrows
    public List<String> listObjectNames(String bucketName) {
        boolean exist = existBucket(bucketName);
        if (!exist) return null;

        List<String> listObjectNames = new ArrayList<>();
        Iterable<Result<Item>> results =
                minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        for (Result<Item> result : results) {
            Item item = result.get();
            listObjectNames.add(item.objectName());
        }
        return listObjectNames;
    }

    /**
     * 查看文件对象
     *
     * @param bucketName 存储bucket名称
     * @return 存储bucket内文件对象信息
     */
    public Map<String, Object> listObjects(String bucketName) {
        boolean exist = existBucket(bucketName);
        if (!exist) return null;

        Iterable<Result<Item>> results =
                minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        Map<String, Object> map = new HashMap<>();
        try {
            for (Result<Item> result : results) {
                Item item = result.get();
                map.put(item.objectName(), item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }

    /**
     * 文件访问路径
     *
     * @param bucketName
     * @param objectName 存储桶里的对象名称
     */
    @SneakyThrows
    public String getObjectUrl(String bucketName, String objectName) {
        boolean exist = existBucket(bucketName);
        if (!exist) return null;
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(7, TimeUnit.DAYS)
                        .build());
    }
    /**
     * 删除一个对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     */
    @SneakyThrows
    public boolean removeObject(String bucketName, String objectName) {
        boolean exist = existBucket(bucketName);
        if (!exist) return false;
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
        return true;
    }

    /**
     * 删除指定桶的多个文件对象
     *
     * @param bucketName  存储桶名称
     * @param objectNames 含有要删除的多个object名称的迭代器对象
     */
    @SneakyThrows
    public boolean removeObject(String bucketName, List<String> objectNames) {
        boolean exist = existBucket(bucketName);
        if (!exist) return false;

        List<DeleteObject> objects = new LinkedList<>();
        for (String objectName : objectNames) {
            objects.add(new DeleteObject(objectName));
        }
        minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(objects).build());
        return true;
    }

    /**
     * 批量删除文件对象
     *
     * @param bucketName 存储bucket名称
     * @param objects    对象名称集合
     */
    public Iterable<Result<DeleteError>> removeObjects(String bucketName, List<String> objects) {
        List<DeleteObject> dos = objects.stream().map(DeleteObject::new).collect(Collectors.toList());
        return minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(dos).build());
    }

    /**
     * 文件上传
     */
    public String upload(MultipartFile multipartFile) {
        if (multipartFile == null) {
            return "MultipartFile is null";
        }

        // 获取文件的扩展名
        String fileName = multipartFile.getOriginalFilename();
        assert fileName != null;
        String[] fileArray = fileName.split("\\.");
        String fileExtension = fileArray[fileArray.length - 1];  // 获取扩展名

        // 获取当前日期
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String format = sdf.format(now);

        // 使用当前时间戳作为文件名
        long currentTimeMillis = System.currentTimeMillis();
        fileName = format + "/" + currentTimeMillis + "." + fileExtension;

        try {
            InputStream inputStream = multipartFile.getInputStream();
            // 上传到 minio 服务器
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .stream(inputStream, -1L, 10485760L)
                    .build());
        } catch (Exception e) {
            System.out.println("上传失败");
            e.printStackTrace();
            return "上传失败";
        }

        // 获取上传文件的 URL
        return  "http://" +server + ":" + port + "/" + bucket + "/" + fileName;
    }

//    /**
//     * 文件下载
//     *
//     * @param fileName 文件名
//     * @param delete   是否删除
//     */
//    public String getImage(String fileName, Boolean delete, HttpServletResponse response) {
//        InputStream inputStream = null;
//        OutputStream outputStream = null;
//        try {
//            if (StringUtils.isBlank(fileName)) {
//                response.setHeader("Content-type", "text/html;charset=UTF-8");
//                String data = "文件下载失败";
//                OutputStream ps = response.getOutputStream();
//                ps.write(data.getBytes(StandardCharsets.UTF_8));
//                return;
//            }
//            outputStream = response.getOutputStream();
//            // 获取文件对象
//            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(fileName).build());
//            byte[] buf = new byte[1024];
//            int length = 0;
//            response.reset();
//            response.setHeader("Content-Disposition", "attachment;filename=" +
//                    URLEncoder.encode(fileName.substring(fileName.lastIndexOf("/") + 1), "UTF-8"));
//            response.setContentType("application/octet-stream");
//            response.setCharacterEncoding("UTF-8");
//            // 输出文件
//            while ((length = inputStream.read(buf)) > 0) {
//                outputStream.write(buf, 0, length);
//            }
//            inputStream.close();
//            // 判断：下载后是否同时删除minio上的存储文件
//            if (BooleanUtils.isTrue(delete)) {
//                minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(fileName).build());
//            }
//        } catch (Throwable ex) {
//            response.setHeader("Content-type", "text/html;charset=UTF-8");
//            String data = "文件下载失败";
//            try {
//                OutputStream ps = response.getOutputStream();
//                ps.write(data.getBytes(StandardCharsets.UTF_8));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } finally {
//            try {
//                outputStream.close();
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//    }
    public String getImage(String fileName) {
        String url = this.getObjectUrl(bucket, fileName);
        return url;
    }
}