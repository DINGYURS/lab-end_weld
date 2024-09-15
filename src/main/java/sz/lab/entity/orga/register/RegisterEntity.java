package sz.lab.entity.orga.register;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("register_table")
public class RegisterEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField(value = "login_code")
    private String logincode;
    @TableField(value = "password")
    private String password;
    @TableField(value = "state")
    private String state;
    @TableField(value = "account_name")
    private String accountname;
    @TableField(value = "CREATE_TIME")
    private String createtime;

}
