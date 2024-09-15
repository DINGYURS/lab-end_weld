package sz.lab.dto.system.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class AccountDTO {
    @NotNull(message = "账号id不能为空")
    private Integer accountId;
    @NotBlank(message = "账号名称不能为空")
    private String accountName;
    @NotBlank(message = "登录账号不能为空")
    private String loginCode;
    @NotBlank(message = "登录密码不能为空")
    private String loginPwd;
    //    private String accountInfo;
    private Integer userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtLastLogin;
    private Integer pwdErrorNum;
    private LocalDateTime pwdLastUpdate;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModify;
    private List<Integer> roleIdList;
    private Integer isDeleted;
}