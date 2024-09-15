package sz.lab.service.orga.user;


import com.baomidou.mybatisplus.extension.service.IService;
import sz.lab.dto.orga.UserDTO;
import sz.lab.dto.system.OperateResultDTO;
import sz.lab.dto.system.TableRequestDTO;
import sz.lab.entity.orga.user.UserEntity;

/**
 * <p>
 * 用户信息表，用于记录用户账号信息。 服务类
 * </p>
 */
public interface UserService extends IService<UserEntity> {
    /**
     * @Description: 分页查询
     **/
    OperateResultDTO pageList(TableRequestDTO tableRequestDTO);
    /**
     * @Description: 角色下拉选项查询
     **/
    OperateResultDTO roleOptionList();
    /**
     * @Description: 用户下拉选项查询
     **/
    OperateResultDTO userOptionList();
    /**
     * @Description: 新增用户
     **/
    OperateResultDTO add(UserDTO userDTO);
    /**
     * @Description: 修改用户
     **/
    OperateResultDTO update(UserDTO userDTO);

}
