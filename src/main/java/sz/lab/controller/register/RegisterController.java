package sz.lab.controller.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sz.lab.dto.login.LoginDTO;
import sz.lab.entity.orga.register.RegisterEntity;
import sz.lab.entity.orga.register.RegisterRoleEntity;
import sz.lab.entity.orga.register.SubmitEntity;
import sz.lab.mapper.register.registerMapper;

import java.util.List;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private registerMapper registerMapper;
    //注册角色获取
    @GetMapping(value = "/getroles", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RegisterEntity> getRoles() {
        return registerMapper.getroles();
    }
    //注册用户提交
    @RequestMapping(value = "/setregister",method = RequestMethod.POST)
    public boolean registerroles(@RequestBody RegisterRoleEntity registerRoleEntity) {
        return registerMapper.setregister(registerRoleEntity);
    }
    //注册用户审核通过后加入用户表
    @Transactional // 确保整个方法在一个事务中执行
    @RequestMapping(value = "/insertregister",method = RequestMethod.POST)
    public boolean insertroles(@RequestBody SubmitEntity requestBody) {
        if(requestBody.getState().equals("2")) {
            registerMapper.updatestate(requestBody.getId(),requestBody.getPhone(),requestBody.getState());
        } else {
            if(registerMapper.updatestate(requestBody.getId(),requestBody.getPhone(),requestBody.getState()))
                return true;
        }
        return true;
    }
    //获取orga_user表数据
    @GetMapping(value = "/getttroles", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LoginDTO> gettRoles() {
        return registerMapper.lookfororgaroles();
    }

}