package sz.lab.controller.system;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sz.lab.config.annotation.SystemControllerLog;
import sz.lab.controller.BaseController;
import sz.lab.dto.system.OperateResultDTO;
import sz.lab.dto.system.TableRequestDTO;
import sz.lab.dto.system.account.AccountDTO;
import sz.lab.service.system.account.AccountService;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 账号管理
 */
@Validated
@RequestMapping("/account")
@RestController
public class AccountController extends BaseController {
    @Resource
    private AccountService accountService;
    /**
     * @Description: 分页查询
     **/
    @PostMapping("/list")
    public OperateResultDTO list(@RequestBody TableRequestDTO tableRequestDTO) {
        OperateResultDTO operateResultDTO = new OperateResultDTO();
        try {
            operateResultDTO = accountService.pageList(tableRequestDTO);
        } catch (Exception e) {
            operateResultDTO.setSuccess(false);
            operateResultDTO.setMessage(e.getMessage());
        }
        return operateResultDTO;
    }
    /**
     * @Description: 用户下拉选项查询
     **/
    @PostMapping("/userOption")
    public OperateResultDTO userOptionList() {
        OperateResultDTO operateResultDTO = new OperateResultDTO();
        try {
            operateResultDTO = accountService.userOptionList();
        } catch (Exception e) {
            operateResultDTO.setSuccess(false);
            operateResultDTO.setMessage(e.getMessage());
        }
        return operateResultDTO;
    }

    /**
     * @Description: 角色下拉选项查询
     **/
    @PostMapping("/roleOption")
    public OperateResultDTO roleOption() {
        OperateResultDTO operateResultDTO = new OperateResultDTO();
        try {
            operateResultDTO = accountService.roleOptionList();
        } catch (Exception e) {
            operateResultDTO.setSuccess(false);
            operateResultDTO.setMessage(e.getMessage());
        }
        return operateResultDTO;
    }
    /**
     * @Description: 新增账号
     **/
    @PostMapping("/add")
    public OperateResultDTO add(@RequestBody AccountDTO input) {
        OperateResultDTO operateResultDTO = new OperateResultDTO();
        try {
            operateResultDTO = accountService.add(input);
        } catch (Exception e) {
            operateResultDTO.setSuccess(false);
            operateResultDTO.setMessage(e.getMessage());
        }
        return operateResultDTO;
    }
    /**
     * @Description: 修改账号
     **/
    @SystemControllerLog(type="账号模块",content = "编辑账号信息")
    @PostMapping("/modify")
    public OperateResultDTO update(@RequestBody AccountDTO input) {
        OperateResultDTO operateResultDTO = new OperateResultDTO();
        try {
            operateResultDTO = accountService.update(input);
        } catch (Exception e) {
            operateResultDTO.setSuccess(false);
            operateResultDTO.setMessage(e.getMessage());
        }
        return operateResultDTO;
    }
    /**
     * @Description: 修改账号角色
     **/
    /**
     * @Description: 判断账号是否存在
     **/
    @PostMapping("/codeIsExist")
    public OperateResultDTO codeIsExist(@RequestBody AccountDTO input) {
        OperateResultDTO operateResultDTO = new OperateResultDTO();
        try {
            operateResultDTO = accountService.codeIsExist(input);
        } catch (Exception e) {
            operateResultDTO.setSuccess(false);
            operateResultDTO.setMessage(e.getMessage());
        }
        return operateResultDTO;
    }
    /**
     * @Description: 删除账号
     **/
    @PostMapping("/remove")
    public OperateResultDTO remove(@NotNull(message = "ids不能为空") @RequestBody Map<String, List<Integer>> map) {
        OperateResultDTO operateResultDTO = new OperateResultDTO();
        try {
            accountService.removeByIds(map.get("ids"));
            operateResultDTO = new OperateResultDTO(true,"成功",null);
        } catch (Exception e) {
            operateResultDTO.setSuccess(false);
            operateResultDTO.setMessage(e.getMessage());
        }
        return operateResultDTO;
    }
}
