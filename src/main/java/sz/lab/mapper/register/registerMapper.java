package sz.lab.mapper.register;

import org.apache.ibatis.annotations.*;
import sz.lab.dto.login.LoginDTO;
import sz.lab.entity.orga.register.RegisterEntity;
import sz.lab.entity.orga.register.RegisterRoleEntity;
import sz.lab.entity.orga.register.SubmitEntity;

import java.util.List;

@Mapper
public interface registerMapper {
    @Select({"select * from register_table"})
    List<RegisterEntity> getroles();
    @Insert({"insert into register_table(account_name,password,login_code,state) values (#{name},#{password},#{phone},#{state})"})
    boolean setregister(RegisterRoleEntity emp);

//    获取用户表
    @Select({"SELECT * FROM orga_user "})
    List<LoginDTO> lookfororgaroles();
    //更新state
    @Update({"UPDATE register_table SET state=#{state} WHERE id=#{id} and login_code = #{phone}"})
    boolean updatestate(@Param("id") Integer id,@Param("phone") String phone,@Param("state") String state);
}
