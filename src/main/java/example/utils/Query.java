package com.utils;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.domain.query.PageQuery;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数构建工具类
 */
public class Query<T> extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    private final Page<T> mybatisPage;

    /**
     * 基于规范的分页请求对象构建
     */
    public Query(PageQuery pageRequest) {
        // 验证参数
        PageHelper.validatePageParams(pageRequest);

        int current = pageRequest.getPage();
        int size = pageRequest.getSize();

        // 防止SQL注入
        String sortBy = SQLFilter.sqlInject(pageRequest.getSortBy());
        String sortOrder = PageHelper.getSafeSortOrder(pageRequest.getSortOrder());
        // 初始化MyBatis-Plus分页对象
        mybatisPage = new Page<>(current, size);
        setupSorting(sortBy, sortOrder);
        setupQueryParams(current, size, sortBy, sortOrder, pageRequest);
    }

    /**
     * 基于Map参数构建
     */
    public Query(Map<String, Object> params) {
        putAll(params);

        int current = extractCurrent(params);
        int size = extractSize(params);

        String sortBy = extractSortBy(params);
        String sortOrder = extractSortOrder(params);

        // 初始化MyBatis-Plus分页对象
        mybatisPage = new Page<>(current, size);
        setupSorting(sortBy, sortOrder);
        setupQueryParams(current, size, sortBy, sortOrder, null);
    }

    private int extractCurrent(Map<String, Object> params) {
        if (params.get("page") != null) {
            return Integer.parseInt(params.get("page").toString());
        }
        return 1;
    }

    private int extractSize(Map<String, Object> params) {
        if (params.get("size") != null) {
            return Integer.parseInt(params.get("size").toString());
        }
        if (params.get("limit") != null) {
            return Integer.parseInt(params.get("limit").toString());
        }
        return 10;
    }

    private String extractSortBy(Map<String, Object> params) {
        return SQLFilter.sqlInject(getParamValue(params, "sortBy", "sidx"));
    }

    private String extractSortOrder(Map<String, Object> params) {
        return SQLFilter.sqlInject(getParamValue(params, "sortOrder", "order"));
    }

    private void setupSorting(String sortBy, String sortOrder) {
        if (StringUtils.isNotBlank(sortBy)) {
            this.put("orderByField", sortBy);
            this.put("isAsc", "ASC".equalsIgnoreCase(sortOrder));
        }
    }


    private void setupQueryParams(int current, int size, String sortBy, String sortOrder, PageQuery request) {
        int offset = request != null ?
                PageHelper.calculateOffset(request) :
                (current - 1) * size;

        this.put("offset", offset);
        this.put("current", current);
        this.put("size", size);
        this.put("sortBy", sortBy);
        this.put("sortOrder", sortOrder);
    }

    private String getParamValue(Map<String, Object> params, String primaryKey, String fallbackKey) {
        if (params.get(primaryKey) != null) {
            return params.get(primaryKey).toString();
        }
        if (params.get(fallbackKey) != null) {
            return params.get(fallbackKey).toString();
        }
        return null;
    }

    /**
     * 构建排序条件（用于XML中的动态排序）
     */
    public String buildOrderBy() {
        String sortBy = (String) this.get("sortBy");
        String sortOrder = (String) this.get("sortOrder");

        if (StringUtils.isNotBlank(sortBy) && StringUtils.isNotBlank(sortOrder)) {
            return sortBy + " " + sortOrder;
        }
        return null;
    }

    /**
     * 添加查询条件
     */
    public Query<T> addCondition(String key, Object value) {
        if (value != null && StringUtils.isNotBlank(value.toString())) {
            this.put(key, value);
        }
        return this;
    }

    /**
     * 添加模糊查询条件
     */
    public Query<T> addLikeCondition(String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            this.put(key, "%" + value + "%");
        }
        return this;
    }

    /**
     * 添加时间范围查询
     */
    public Query<T> addTimeRange(String startTime, String endTime) {
        if (StringUtils.isNotBlank(startTime)) {
            this.put("startTime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            this.put("endTime", endTime);
        }
        return this;
    }

    // Getter方法
    public Page<T> getPage() {
        return mybatisPage;
    }

    public int getCurrent() {
        return (int) mybatisPage.getCurrent();
    }

    public int getSize() {
        return (int) mybatisPage.getSize();
    }

    public int getOffset() {
        return (getCurrent() - 1) * getSize();
    }
}