package cn.liveland.blog.param.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对String字符串进行去除空格
 *
 * @author xiyatu
 * @date 2019/4/25 17:53
 * Description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface ClearBlank {
    boolean isAll() default false;
}
