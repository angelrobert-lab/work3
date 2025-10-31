package com.domain.vo;

import com.utils.PageHelper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询响应结果
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分页查询响应结果")
public class PageVo<T> {
    @Schema(description = "数据列表")
    private List<T> records;

    @Schema(description = "总记录数", example = "100")
    private Long total;

    @Schema(description = "当前页码", example = "1")
    private Integer current;

    @Schema(description = "每页大小", example = "10")
    private Integer size;

    @Schema(description = "总页数", example = "10")
    private Integer pages;

    @Schema(description = "是否有上一页")
    private Boolean hasPrevious;

    @Schema(description = "是否有下一页")
    private Boolean hasNext;

    public static <T> PageVo<T> of(List<T> records, Long total, Integer current, Integer size) {
        int pages = PageHelper.calculatePages(total, size);
        return PageVo.<T>builder()
                .records(records)
                .total(total)
                .current(current)
                .size(size)
                .pages(pages)
                .hasPrevious(current != null && current > 1)
                .hasNext(current != null && pages > 0 && current < pages)
                .build();
    }

}