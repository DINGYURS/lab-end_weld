package sz.lab.dto.weld;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class WeldDefectPageQueryDTO implements Serializable {
    // 页码
    private int page;

    // 每页记录数
    private int pageSize;

    // 创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin;

    // 修改时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;
}
