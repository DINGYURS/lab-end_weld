package sz.lab.service.system.lay.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sz.lab.dto.system.OperateResultDTO;
import sz.lab.dto.system.lay.LayNoticeDTO;
import sz.lab.entity.orga.user.UserEntity;
import sz.lab.entity.system.account.AccountEntity;
import sz.lab.mapper.orga.user.UserMapper;
import sz.lab.mapper.system.account.AccountMapper;
import sz.lab.service.system.lay.LayNoticeService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LayNoticeServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements LayNoticeService {
    @Autowired
    private AccountMapper accountMapper;
    @Override
    public OperateResultDTO checkPwdUpdate(Integer userId) {
//        UserEntity user = baseMapper.selectById(userId);
        AccountEntity account = accountMapper.selectOne(Wrappers.lambdaQuery(AccountEntity.class)
                .eq(AccountEntity::getUserId, userId));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastUpdate = account.getPwdLastUpdate();
        LayNoticeDTO noticeDTO = new LayNoticeDTO();
        if(lastUpdate.isBefore(now.minusMonths(3))){
            noticeDTO.setType("1");
            noticeDTO.setAvatar("");
            noticeDTO.setDatetime("");
            noticeDTO.setTitle("账号安全");
            noticeDTO.setDescription("距离您上次更新密码已超过三个月，为了保证您的账号安全，请尽快更新密码。");
            noticeDTO.setStatus("danger");
            noticeDTO.setExtra("重要");
            return new OperateResultDTO(true,"成功", noticeDTO);
        }
        return new OperateResultDTO(true,"成功", null);
    }
}
