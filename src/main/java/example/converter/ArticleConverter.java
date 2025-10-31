package com.converter;

import com.domain.dto.ArticleDto;
import com.domain.po.ArticlePo;
import com.domain.vo.ArticleVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleConverter {
    ArticleConverter INSTANCE = Mappers.getMapper(ArticleConverter.class);

    // DTO 转 PO
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target="userId",source = "userId")
    ArticlePo articleDtoToArticlePo(ArticleDto dto,Long userId);

    // PO 转 VO
    @Mapping(target = "authorName", source = "authorName")
    ArticleVo articlePoToArticleVo(ArticlePo po,String authorName);
}