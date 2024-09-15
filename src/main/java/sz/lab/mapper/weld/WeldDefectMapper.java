package sz.lab.mapper.weld;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import sz.lab.dto.weld.WeldDefectUpdateDTO;
import sz.lab.dto.weld.WeldDefectPageQueryDTO;
import sz.lab.entity.weld.Weld;

@Mapper
public interface WeldDefectMapper {
    /**
     * 焊缝缺陷信息查询
     * @param weldDefectPageQueryDTO
     * @return
     */
    Page<Weld> pageQuery(WeldDefectPageQueryDTO weldDefectPageQueryDTO);

    /**
     * 插入焊缝缺陷信息
     * @param weld
     */
    @Insert("insert into weld_defect_info (image, image_check, info, create_time, update_time, is_verified) " +
            "values (#{image}, #{imageCheck}, #{info}, #{createTime}, #{updateTime}, #{isVerified})")
    void insert(Weld weld);

    /**
     * 修改焊缝缺陷信息
     * @param weld
     */
    void update(Weld weld);
}
