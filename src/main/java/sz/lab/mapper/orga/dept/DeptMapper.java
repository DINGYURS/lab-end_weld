package sz.lab.mapper.orga.dept;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import sz.lab.entity.orga.dept.DeptEntity;


/**
 * <p>
 * 部门（实验室）信息表，公司ID和部门ID在配置文件中设置，以实验室为基本使用单位，设计实验室支持二级组织架构，不支持三级，程序中提示加锁定 Mapper 接口
 * </p>
 *
 * @author ckd
 * @since 2023-11-24
 */
@Mapper
public interface DeptMapper extends BaseMapper<DeptEntity> {

}
