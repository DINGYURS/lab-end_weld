package sz.lab.dto.weld;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class WeldDefectInsertDTO implements Serializable {
    // 初始照片
    private MultipartFile image;

    // 检验后的照片
    private MultipartFile imageCheck;

    // 缺陷信息
    private String info;
}
