package sz.lab.service.system.account;

import com.baomidou.mybatisplus.extension.service.IService;
import sz.lab.dto.system.OperateResultDTO;
import sz.lab.dto.system.TableRequestDTO;
import sz.lab.dto.system.account.AccountDTO;
import sz.lab.entity.system.account.AccountEntity;
/**
 * <p>
 * 账号信息表，用于记录账号信息。 服务类
 * </p>
 */
public interface AccountService extends IService<AccountEntity> {
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
    OperateResultDTO add(AccountDTO input);
    /**
     * @Description: 修改用户
     **/
    OperateResultDTO update(AccountDTO input);
    /**
     * @Description: 判断账号是否存在
     **/
    OperateResultDTO codeIsExist(AccountDTO input);
}
