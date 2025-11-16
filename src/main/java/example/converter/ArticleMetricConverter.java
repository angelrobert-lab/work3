package com.converter;

import com.domain.po.ArticleMetricPo;
import com.domain.vo.ArticleMetricVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ArticleMetricConverter {
    ArticleMetricConverter INSTANCE = Mappers.getMapper(ArticleMetricConverter.class);

    @Mapping(target = "liked", source = "liked")
    ArticleMetricVo toVo(ArticleMetricPo po, boolean liked);
}
