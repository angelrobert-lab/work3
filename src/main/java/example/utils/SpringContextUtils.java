//package com.utils;
//
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Component;
//
///**
// * Spring Context 工具类
// *
// * 用于在非Spring管理的类中获取Spring容器中的Bean
// * 实现ApplicationContextAware接口，Spring启动时会自动注入ApplicationContext
// *
// * 使用场景：
// * 1. 在工具类、静态方法中获取Spring Bean
// * 2. 在普通Java类中获取Spring管理的Bean
// * 3. 在实体类、监听器等非Spring组件中调用Service
// */
//@Component
//public class SpringContextUtils implements ApplicationContextAware {
//
//    /**
//     * 静态ApplicationContext引用
//     * 保存Spring应用上下文，便于静态方法访问
//     */
//    public static ApplicationContext applicationContext;
//
//    /**
//     * 实现ApplicationContextAware接口的方法
//     * Spring容器启动时会自动调用此方法，注入ApplicationContext
//     *
//     * @param applicationContext Spring应用上下文
//     * @throws BeansException 如果注入过程中发生错误
//     */
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        // 将Spring注入的applicationContext赋值给静态变量
//        SpringContextUtils.applicationContext = applicationContext;
//    }
//
//    /**
//     * 通过Bean名称获取Bean实例
//     *
//     * @param name Bean的名称
//     * @return Bean实例对象
//     */
//    public static Object getBean(String name) {
//        return applicationContext.getBean(name);
//    }
//
//    /**
//     * 通过Bean名称和类型获取Bean实例
//     *
//     * @param <T> Bean的类型
//     * @param name Bean的名称
//     * @param requiredType Bean的Class类型
//     * @return 指定类型的Bean实例
//     */
//    public static <T> T getBean(String name, Class<T> requiredType) {
//        return applicationContext.getBean(name, requiredType);
//    }
//
//    /**
//     * 通过类型获取Bean实例（需要确保该类型在容器中唯一）
//     *
//     * @param <T> Bean的类型
//     * @param requiredType Bean的Class类型
//     * @return 指定类型的Bean实例
//     */
//    public static <T> T getBean(Class<T> requiredType) {
//        return applicationContext.getBean(requiredType);
//    }
//
//    /**
//     * 检查Spring容器中是否包含指定名称的Bean
//     *
//     * @param name Bean的名称
//     * @return true-包含，false-不包含
//     */
//    public static boolean containsBean(String name) {
//        return applicationContext.containsBean(name);
//    }
//
//    /**
//     * 判断指定名称的Bean是否是单例
//     *
//     * @param name Bean的名称
//     * @return true-单例，false-原型(多例)
//     */
//    public static boolean isSingleton(String name) {
//        return applicationContext.isSingleton(name);
//    }
//
//    /**
//     * 获取指定名称Bean的类型
//     *
//     * @param name Bean的名称
//     * @return Bean的Class类型
//     */
//    public static Class<? extends Object> getType(String name) {
//        return applicationContext.getType(name);
//    }
//
//    /**
//     * 获取当前环境配置（开发、测试、生产）
//     *
//     * @return 当前环境名称
//     */
//    public static String getActiveProfile() {
//        return applicationContext.getEnvironment().getActiveProfiles()[0];
//    }
//
//    /**
//     * 检查当前是否是指定环境
//     *
//     * @param profile 环境名称（dev、test、prod等）
//     * @return true-是指定环境，false-不是指定环境
//     */
//    public static boolean isProfileActive(String profile) {
//        String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
//        for (String activeProfile : activeProfiles) {
//            if (activeProfile.equals(profile)) {
//                return true;
//            }
//        }
//        return false;
//    }
//}