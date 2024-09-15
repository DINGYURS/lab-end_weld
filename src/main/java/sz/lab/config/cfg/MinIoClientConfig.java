package sz.lab.config.cfg;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "minio")
public class MinIoClientConfig {
    @Value("${minio.server}")
    private String server;
    @Value("${minio.port}")
    private int port;
    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.access-key}")
    private String accessKey;
    @Value("${minio.secret-key}")
    private String secretKey;
    @Value("${minio.bucket}")
    private String bucket;
    /**
     * 创建minio连接对象
     * @return
     */
    @Bean
    public MinioClient minioClient(){
        return  MinioClient.builder()
                .endpoint(server,port,false)
                .credentials(accessKey,secretKey)
                .build();
    }
    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }
 
    public void setPort(int port) {
        this.port = port;
    }
 
    public String getEndpoint() {
        return endpoint;
    }
 
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
 
    public String getAccessKey() {
        return accessKey;
    }
 
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
 
    public String getSecretKey() {
        return secretKey;
    }
 
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
 
    public String getBucket() {
        return bucket;
    }
 
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}