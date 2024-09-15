package sz.lab.entity.system.account;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description: 账号表
 **/
@Getter
@Setter
@TableName("system_account")
public class AccountEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 账号ID，主键
     */
    @TableId(value = "account_id", type = IdType.AUTO)
    private Integer accountId;
    /**
     * 账号姓名
     */
    @TableField(value = "account_name")
    private String accountName;

    /**
     * 登录账号
     */
    @TableField(value = "login_code")
    private String loginCode;

    /**
     * 登录密码
     */
    @TableField(value = "login_pwd")
    private String loginPwd;
    /**
     * 用户id，外键
     */
    @TableField(value = "user_id")
    private Integer userId;
//    /**
//     * 备注信息
//     */
//    @TableField(value = "account_info")
//    private String accountInfo;

    /**
     * 上次修改密码时间
     */
    @TableField(value = "pwd_last_update", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime pwdLastUpdate;

    /**
     * 上次修改登录时间
     */
    @TableField(value = "pwd_last_update", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtLastLogin;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtCreate;
    /**
     * 修改时间
     */
    @TableField(value = "gmt_modify", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtModify;


    @TableField(value = "is_deleted")
    @TableLogic
    private Integer isDeleted;
}
