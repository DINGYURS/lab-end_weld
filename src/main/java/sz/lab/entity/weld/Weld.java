package sz.lab.entity.weld;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Weld implements Serializable {
    //主键ID
    private Long id;

    // 初始照片
    private String image;

    // 检验后的照片
    private String imageCheck;

    // 缺陷信息
    private String info;

    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    // 修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    // 是否已审查（0表示未审查，1代表已审查）
    private Integer isVerified;
}
