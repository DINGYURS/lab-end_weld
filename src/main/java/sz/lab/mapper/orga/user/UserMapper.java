package sz.lab.mapper.orga.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import sz.lab.entity.orga.user.UserEntity;

import java.util.List;

/**
 * <p>
 * 用户信息表，用于记录用户账号信息。 Mapper 接口
 * </p>
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
    List<UserEntity> engineerList(String roleCode, String userName, Integer deptId);
}
