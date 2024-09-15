package sz.lab.entity.orga.user;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * <p>
 * 用户信息表，用于记录用户账号信息。
 * </p>
 */
@Getter
@Setter
@TableName("orga_user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID，主键
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户姓名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 部门ID，外键
     */
    @TableField(value = "dept_id")
    private Integer deptId;

    /**
     * 手机号码，必填
     */
    @TableField(value = "user_phone")
    private String userPhone;

    /**
     * 备注信息
     */
    @TableField(value = "user_info")
    private String userInfo;

//    /**
//    * 上次登录时间
//    */
//    @TableField(value = "gmt_last_login")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private LocalDateTime gmtLastLogin;
//
//    /**
//     * 上次修改密码时间
//     */
//    @TableField(value = "pwd_last_update", fill = FieldFill.INSERT)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private LocalDateTime pwdLastUpdate;

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