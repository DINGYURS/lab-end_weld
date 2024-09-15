package sz.lab.controller;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import sz.lab.config.cfg.SystemConfig;
import sz.lab.config.constants.RoleEnum;
import sz.lab.dto.login.TokenDTO;
import sz.lab.utils.JWTUtil;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    public BaseController() {
        userId = ThreadUtil.createThreadLocal(false);
        role = ThreadUtil.createThreadLocal(false);
    }

    protected ThreadLocal<Integer> userId;
    protected ThreadLocal<RoleEnum> role;

    @ModelAttribute
    public void init(HttpServletRequest request) {
        String token = request.getHeader(SystemConfig.TOKEN_KEY);
        if (StrUtil.isNotBlank(token)) {
            token = token.replace("Bearer ", "");
        }
        TokenDTO tokenDTO = JWTUtil.verifyToken(token);
        if (null == tokenDTO) {
            userId.set(0);
        } else {
            userId.set(tokenDTO.getUserId());
            role.set(RoleEnum.getRoleByCode(tokenDTO.getRoleCodes()[0]));
        }
    }
}
