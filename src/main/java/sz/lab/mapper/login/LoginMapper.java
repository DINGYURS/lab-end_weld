package sz.lab.mapper.login;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import sz.lab.dto.login.LoginDTO;
import sz.lab.dto.system.account.AccountDTO;

import java.util.List;

/**
 * @Description: 用户登录
 * @Author: 宋光慧
 * @Date: 2023/7/11
 **/
@Mapper
public interface LoginMapper {

    @Select({"select * from system_account " +
            "where login_code = #{loginCode} LIMIT 1"})
    AccountDTO getByLoginCode(@Param("loginCode") String loginCode);

    @Select({"SELECT * from orga_user where user_id = #{userId}"})
    LoginDTO getByUserId(@Param("userId") Integer userId);

    List<String> getUserRoles(@Param("userId") Integer userId);

    int isAdmin(@Param("userId") Integer userId);
}
