package sz.lab.end;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sz.lab.entity.system.SystemMenuEntity;
import sz.lab.mapper.system.menu.SystemMenuMapper;
import sz.lab.service.login.LoginService;

import javax.annotation.Resource;
import java.util.List;
@Slf4j
@SpringBootTest
class LabEndApplicationTests {
    @Resource
    private LoginService loginService;
    @Resource
    private SystemMenuMapper systemMenuMapper;
    @Test
    void contextLoads() {
        List<SystemMenuEntity> list = systemMenuMapper.selectList(Wrappers.lambdaQuery(SystemMenuEntity.class));
        log.info("{}",list.get(0));
    }

}
