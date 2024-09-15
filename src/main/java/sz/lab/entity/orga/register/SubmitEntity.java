package sz.lab.entity.orga.register;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitEntity {
    private Integer id;
    private Integer orgauser;
    private String phone;
    private String state;
    private String info;
}
