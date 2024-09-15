package sz.lab.service.system.lay;

import com.baomidou.mybatisplus.extension.service.IService;
import sz.lab.dto.system.OperateResultDTO;
import sz.lab.entity.orga.user.UserEntity;
/**
 * <p>
 * 消息通知表，用于通知用户信息。 服务类
 * </p>
 */
public interface LayNoticeService extends IService<UserEntity> {
    /**
     * @Description: 判断是否需要密码重置
     **/
    OperateResultDTO checkPwdUpdate(Integer userId);
}
