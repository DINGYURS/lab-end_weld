package sz.lab.mapper.system.menu;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import sz.lab.entity.system.SystemMenuEntity;
/**
 * <p>
 * 系统菜单表，用于记录菜单信息。 Mapper 接口
 * </p>
 */
@Mapper
public interface SystemMenuMapper extends BaseMapper<SystemMenuEntity> {
}
