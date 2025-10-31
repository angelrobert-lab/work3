package com.utils;

import cn.hutool.core.lang.Dict;  // Hutool工具类的字典对象，类似Map
import cn.hutool.core.util.ArrayUtil;  // Hutool的数组工具类
import cn.hutool.core.util.ObjectUtil;  // Hutool的对象工具类
import cn.hutool.core.util.StrUtil;  // Hutool的字符串工具类
import cn.hutool.extra.spring.SpringUtil;  // Hutool的Spring工具类，用于获取Spring容器中的Bean
import com.fasterxml.jackson.core.JsonProcessingException;  // Jackson的JSON处理异常
import com.fasterxml.jackson.core.type.TypeReference;  // Jackson的类型引用，用于泛型解析
import com.fasterxml.jackson.databind.ObjectMapper;  // Jackson的核心JSON处理类
import com.fasterxml.jackson.databind.exc.MismatchedInputException;  // Jackson的类型不匹配异常
import lombok.AccessLevel;  // Lombok注解，用于控制构造方法访问级别
import lombok.NoArgsConstructor;  // Lombok注解，生成无参构造方法

import java.io.IOException;  // IO异常
import java.util.ArrayList;  // 集合类
import java.util.List;  // 集合接口

/**
 * JSON 工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)  // 私有构造方法，禁止外部实例化（工具类通常设计为单例或静态方法）
public class JsonUtils {

    /**
     * 全局共享的 ObjectMapper 实例
     * 从 Spring 容器中获取配置好的 ObjectMapper（可能包含自定义序列化/反序列化规则）
     */
    private static final ObjectMapper OBJECT_MAPPER = SpringUtil.getBean(ObjectMapper.class);

    /**
     * 将 Java 对象序列化为 JSON 字符串
     *
     * @param object 待序列化的对象（可为null）
     * @return 序列化后的JSON字符串；若输入对象为null，返回null
     * @throws RuntimeException 若序列化过程中发生错误（如循环引用、不支持的类型）
     */
    public static String toJsonString(Object object) {
        if (ObjectUtil.isNull(object)) {  // 使用Hutool工具判断对象是否为null
            return null;
        }
        try {
            // 调用Jackson的writeValueAsString方法进行序列化
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {  // 捕获JSON处理异常
            throw new RuntimeException(e);  // 包装为运行时异常抛出，避免强制声明受检异常
        }
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的 Java 对象
     *
     * @param text 待反序列化的JSON字符串（可为空）
     * @param clazz 目标对象的类类型（如 User.class）
     * @param <T> 目标对象的泛型类型
     * @return 反序列化后的对象；若输入字符串为空，返回null
     * @throws RuntimeException 若反序列化过程中发生错误（如JSON格式错误、类型不匹配）
     */
    public static <T> T parseObject(String text, Class<T> clazz) {
        if (StrUtil.isEmpty(text)) {  // 使用Hutool工具判断字符串是否为空（null或空串）
            return null;
        }
        try {
            // 调用Jackson的readValue方法反序列化为指定类的对象
            return OBJECT_MAPPER.readValue(text, clazz);
        } catch (IOException e) {  // 捕获IO异常（包含JSON格式错误等）
            throw new RuntimeException(e);
        }
    }

    /**
     * 将字节数组（JSON格式）反序列化为指定类型的 Java 对象
     * 适用于从文件、网络流等字节数据中解析JSON
     *
     * @param bytes 待反序列化的字节数组（可为空）
     * @param clazz 目标对象的类类型
     * @param <T> 目标对象的泛型类型
     * @return 反序列化后的对象；若字节数组为空，返回null
     * @throws RuntimeException 若反序列化过程中发生错误
     */
    public static <T> T parseObject(byte[] bytes, Class<T> clazz) {
        if (ArrayUtil.isEmpty(bytes)) {  // 使用Hutool工具判断数组是否为空
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSON 字符串反序列化为复杂泛型类型的对象（如 List<User>、Map<String, Integer> 等）
     * 解决 Java 泛型擦除导致的反序列化问题
     *
     * @param text 待反序列化的JSON字符串（可为空）
     * @param typeReference 泛型类型引用（如 new TypeReference<List<User>>() {}）
     * @param <T> 目标泛型类型
     * @return 反序列化后的对象；若输入字符串为空，返回null
     * @throws RuntimeException 若反序列化过程中发生错误
     */
    public static <T> T parseObject(String text, TypeReference<T> typeReference) {
        if (StrUtil.isBlank(text)) {  // 判断字符串是否为空白（包含空格、制表符等）
            return null;
        }
        try {
            // 使用TypeReference保留泛型信息，实现复杂类型反序列化
            return OBJECT_MAPPER.readValue(text, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSON 字符串反序列化为 Hutool 的 Dict 对象（类似 Map<String, Object>）
     * 适用于JSON格式为键值对的场景
     *
     * @param text 待反序列化的JSON字符串（可为空）
     * @return 反序列化后的Dict对象；若输入为空或JSON格式不是对象，返回null
     * @throws RuntimeException 若反序列化过程中发生非类型不匹配的错误
     */
    public static Dict parseMap(String text) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        try {
            // 构造Dict类型并反序列化
            return OBJECT_MAPPER.readValue(text, OBJECT_MAPPER.getTypeFactory().constructType(Dict.class));
        } catch (MismatchedInputException e) {
            // 若JSON不是对象类型（如数组、字符串），返回null（不抛出异常）
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSON 字符串反序列化为 List<Dict> 集合（JSON数组转字典列表）
     * 适用于JSON格式为对象数组的场景
     *
     * @param text 待反序列化的JSON字符串（可为空）
     * @return 反序列化后的List<Dict>；若输入为空，返回null
     * @throws RuntimeException 若反序列化过程中发生错误
     */
    public static List<Dict> parseArrayMap(String text) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        try {
            // 构造List<Dict>类型并反序列化
            return OBJECT_MAPPER.readValue(text,
                    OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, Dict.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的 List 集合（JSON数组转对象列表）
     *
     * @param text 待反序列化的JSON字符串（可为空）
     * @param clazz 集合元素的类类型（如 User.class）
     * @param <T> 集合元素的泛型类型
     * @return 反序列化后的List<T>；若输入为空，返回空集合（避免NPE）
     * @throws RuntimeException 若反序列化过程中发生错误
     */
    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (StrUtil.isEmpty(text)) {
            return new ArrayList<>();  // 输入为空时返回空集合，而非null，减少空指针风险
        }
        try {
            // 构造List<T>类型并反序列化
            return OBJECT_MAPPER.readValue(text,
                    OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}