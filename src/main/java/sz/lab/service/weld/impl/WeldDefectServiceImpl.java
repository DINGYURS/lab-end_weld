package sz.lab.service.weld.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import sz.lab.dto.system.PageResultDTO;
import sz.lab.dto.weld.WeldDefectInsertDTO;
import sz.lab.dto.weld.WeldDefectUpdateDTO;
import sz.lab.dto.weld.WeldDefectPageQueryDTO;
import sz.lab.entity.weld.Weld;
import sz.lab.mapper.weld.WeldDefectMapper;
import sz.lab.service.weld.WeldDefectService;
import sz.lab.utils.MinioUtil;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class WeldDefectServiceImpl implements WeldDefectService {

    @Resource
    private WeldDefectMapper weldDefectMapper;

    @Resource
    private MinioUtil minioUtil;

    /**
     * 焊缝缺陷信息查询
     * @param weldDefectPageQueryDTO
     * @return
     */
    @Override
    public PageResultDTO pageQuery(WeldDefectPageQueryDTO weldDefectPageQueryDTO) {
        PageHelper.startPage(weldDefectPageQueryDTO.getPage(), weldDefectPageQueryDTO.getPageSize());
        Page<Weld> page = weldDefectMapper.pageQuery(weldDefectPageQueryDTO);

        return new PageResultDTO(page.getTotal(), page.getResult());
    }

    /**
     * 插入缺陷焊缝信息
     * @param weldDefectInsertDTO
     */
    @Override
    public void insert(WeldDefectInsertDTO weldDefectInsertDTO) {
        // 上传minio，然后简化URL
        String imageUrl = simplifyUrl(minioUtil.upload(weldDefectInsertDTO.getImage()));
        String imageCheckUrl = simplifyUrl(minioUtil.upload(weldDefectInsertDTO.getImageCheck()));

        // 截取 '?' 之前的部分，简化 URL
        imageUrl = simplifyUrl(imageUrl);
        imageCheckUrl = simplifyUrl(imageCheckUrl);

        Weld weld = Weld.builder()
                .image(imageUrl)
                .imageCheck(imageCheckUrl)
                .info(weldDefectInsertDTO.getInfo())
                .isVerified(0)  // 默认未审核
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        weldDefectMapper.insert(weld);
    }

    /**
     * 修改焊缝缺陷信息
     * @param weldDefectUpdateDTO
     */
    @Override
    public void update(WeldDefectUpdateDTO weldDefectUpdateDTO) {
        Weld weld = new Weld();
        BeanUtils.copyProperties(weldDefectUpdateDTO, weld);

        weld.setUpdateTime(LocalDateTime.now());
        weld.setIsVerified(1);
        weldDefectMapper.update(weld);
    }

    /**
     * 截取 URL 中 '?' 之前的部分
     * @param url 完整的 URL
     * @return 简化后的 URL
     */
    private String simplifyUrl(String url) {
        int index = url.indexOf('?');
        if (index != -1) {
            return url.substring(0, index);
        }
        return url;
    }
}


