package cn.liveland.blog.param.aspect;


import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 去除参数空格
 *
 * @author xiyatu
 * @date 2019/04/24 13:49:52
 * @description 去除某方法字符串参数的空格。
 */
@Component
@Aspect
public class ParamAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParamAspect.class);

    @Around("execution( * cn.liveland.blog.param.controller.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取目标类与方法
        Object target = joinPoint.getTarget();
        String method = joinPoint.getSignature().getName();
        //获取参数
        Object[] args = joinPoint.getArgs();
        //获取参数类型列表
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Class<?>[] parameterTypes = methodSignature.getMethod().getParameterTypes();
        try {
            //获取方法
            Method m = target.getClass().getMethod(method, parameterTypes);
            if (m != null) {
                //获取每个参数的注解
                Annotation[][] parameterAnnotations = m.getParameterAnnotations();
                //开始遍历参数
                for (int i = 0; i < parameterTypes.length; i++) {
                    //获取参数的注解
                    Annotation[] annotations = parameterAnnotations[i];
                    //开始遍历注解
                    for (Annotation annotation : annotations) {
                        // 如果参数被ClearBlank注解修饰则进行处理
                        if (annotation.annotationType() == ClearBlank.class) {
                            ClearBlank clearBlank = (ClearBlank) annotation;
                            //只处理String类型的参数
                            if (parameterTypes[i] == String.class) {
                                String param = (String) args[i];
                                //空数据不进行处理
                                if (StringUtils.isBlank(param)) {
                                    continue;
                                }
                                if (clearBlank.isAll()) {
                                    //去除所有的空格
                                    args[i] = param.replaceAll(" ", "");
                                } else {
                                    //去除首尾的空格
                                    args[i] = param.trim();
                                }
                            }
                        } else if (annotation.annotationType() == Valid.class) {
                            // 如果参数被Valid注解修饰则是Json的对象类参数，需要对整个类进行反射处理。
                            // 获取类中的所有字段
                            Field[] fields = parameterTypes[i].getDeclaredFields();
                            //获取对应的参数值
                            Object obj = args[i];
                            //开始遍历字段
                            for (Field field : fields) {
                                //如果该字段是String类型（首先判断这个，即使其他字段被ClearBlank修饰也不处理）并且被ClearBlank修饰
                                if (field.getType() == String.class && field.isAnnotationPresent(ClearBlank.class)) {
                                    //获取注解值
                                    ClearBlank clearBlank = field.getAnnotation(ClearBlank.class);
                                    //设置可见性
                                    field.setAccessible(true);
                                    // 获取字段的值
                                    String param = (String) field.get(obj);
                                    //空数据不进行处理
                                    if (StringUtils.isBlank(param)) {
                                        continue;
                                    }
                                    if (clearBlank.isAll()) {
                                        //去除所有的空格
                                        param = param.replaceAll(" ", "");
                                    } else {
                                        //去除首尾的空格
                                        param = param.trim();
                                    }
                                    //将处理好的参数设置进去。
                                    field.set(obj, param);
                                    //关闭可见性
                                    field.setAccessible(false);
                                }
                            }
                            //覆盖原参数
                            args[i] = obj;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.info("===== Processing Param Error={}", e.getMessage());
        }
        return joinPoint.proceed(args);
    }
}
