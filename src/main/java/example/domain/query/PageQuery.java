package com.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页查询请求参数（
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分页查询请求参数")
public class PageQuery {

    @Min(value = 1, message = "页码最小为1")
    @Schema(description = "当前页码，从1开始", example = "1")
    @Builder.Default
    private Integer page = 1;


    @Min(value = 1, message = "每页大小最小为1")
    @Max(value = 1000, message = "每页大小最大为1000")
    @Schema(description = "每页大小", example = "10")
    @Builder.Default
    private Integer size = 10;


    @Schema(description = "排序字段", example = "create_time")
    private String sortBy;


    @Schema(description = "排序方向：ASC-升序, DESC-降序", example = "DESC")
    @Builder.Default
    private String sortOrder = "DESC";
}