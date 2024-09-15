package sz.lab.dto.orga;

import lombok.Data;

import java.util.List;

@Data
public class UserOutputDTO {
    private Integer userId;
    private String loginCode;
    private String userName;
    private String userPhone;
    private String userInfo;
    private Integer deptId;
    private String deptName;
    private Integer isDeleted;
    private List<Integer> roleIdList;
}
