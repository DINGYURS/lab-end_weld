package sz.lab.config.cfg;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * mybatisPlus配置类
 */
@Configuration
public class MybatisPlusConfig implements MetaObjectHandler {
    /**
     * 实体类的@TableField注解配置fill = FieldFill.INSERT时，自动填充字段
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "gmtModify", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "gmtCreate", LocalDateTime.class, LocalDateTime.now());
    }
    /**
     * 实体类的@TableField注解配置fill = FieldFill.UPDATE时，自动填充字段
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "gmtModify", LocalDateTime.class, LocalDateTime.now());
    }
}