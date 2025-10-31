package com.aop;

import com.annotation.AtLeastOneNotNull;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 至少一个字段不为空的校验切面
 */
@Aspect
@Component
public class AtLeastOneNotNullAop {

    /**
     * 切入点：拦截所有标记了@AtLeastOneNotNull的方法或类
     */
    @Before("@annotation(atLeastOneNotNull) || @within(atLeastOneNotNull)")
    public void validate(JoinPoint joinPoint, AtLeastOneNotNull atLeastOneNotNull) {
        // 获取注解参数：需要校验的字段名和提示信息
        String[] fields = atLeastOneNotNull.fields();
        String message = atLeastOneNotNull.message();
        // 检查字段数组是否为空（避免用户忘记配置fields）
        if (fields == null || fields.length == 0) {
            throw new IllegalArgumentException("注解@AtLeastOneNotNull的fields属性不能为空");
        }
        // 获取方法参数（需要校验的对象）
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return; // 无参数则无需校验
        }
        // 遍历方法参数，对每个参数进行字段校验
        for (Object arg : args) {
            if (arg == null) {
                continue; // 参数为null不校验
            }
            // 标记是否存在非空字段
            boolean hasNonNullField = false;
            // 反射获取参数对象的所有字段
            Class<?> clazz = arg.getClass();
            for (String fieldName : fields) {
                try {
                    // 获取字段（包括私有字段）
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true); // 允许访问私有字段
                    // 获取字段值
                    Object fieldValue = field.get(arg);

                    // 判断字段值是否不为空（字符串需排除空白字符）
                    if (fieldValue != null) {
                        if (fieldValue instanceof String) {
                            if (StringUtils.hasText((String) fieldValue)) {
                                hasNonNullField = true;
                                break; // 找到一个非空字段，无需继续检查
                            }
                        } else {
                            // 非字符串类型，只要不为null即视为有效
                            hasNonNullField = true;
                            break;
                        }
                    }
                } catch (NoSuchFieldException e) {
                    // 如果字段不存在，抛出异常提示
                    throw new IllegalArgumentException("字段不存在：" + fieldName + "（检查@AtLeastOneNotNull的fields配置）", e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("反射获取字段值失败", e);
                }
            }

            // 如果所有字段都为空，抛出校验失败异常
            if (!hasNonNullField) {
                throw new IllegalArgumentException(message);
            }
        }
    }
}