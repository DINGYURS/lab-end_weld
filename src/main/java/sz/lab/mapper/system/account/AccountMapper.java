package sz.lab.mapper.system.account;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import sz.lab.entity.system.account.AccountEntity;

@Mapper
public interface AccountMapper extends BaseMapper<AccountEntity> {
}
