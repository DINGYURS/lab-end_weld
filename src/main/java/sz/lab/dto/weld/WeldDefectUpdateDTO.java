package sz.lab.dto.weld;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class WeldDefectUpdateDTO implements Serializable {
    //主键ID
    private Long id;

    // 是否已审查（0表示未审查，1代表已审查）
    private Integer isVerified;
}
