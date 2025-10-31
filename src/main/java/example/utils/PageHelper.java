package com.utils;


import com.domain.query.PageQuery;

/**
 * 分页工具类
 */
public final class PageHelper {

    private PageHelper() {
        // 工具类防止实例化
    }

    /**
     * 计算偏移量
     */
    public static Integer calculateOffset(PageQuery request) {
        if (request.getPage() == null || request.getSize() == null) {
            return 0;
        }
        return (request.getPage() - 1) * request.getSize();
    }

    /**
     * 计算总页数
     */
    public static Integer calculatePages(Long total, Integer size) {
        if (total == null || size == null || size == 0) {
            return 0;
        }
        return (int) Math.ceil((double) total / size);
    }

    /**
     * 获取安全的排序方向
     */
    public static String getSafeSortOrder(String sortOrder) {
        if ("DESC".equalsIgnoreCase(sortOrder)) {
            return "DESC";
        }
        return "ASC";
    }

    /**
     * 验证分页参数
     */
    public static void validatePageParams(PageQuery request) {
        if (request.getPage() == null || request.getPage() < 1) {
            throw new IllegalArgumentException("页码参数无效");
        }
        if (request.getSize() == null || request.getSize() < 1 || request.getSize() > 1000) {
            throw new IllegalArgumentException("每页大小参数无效");
        }
    }
}