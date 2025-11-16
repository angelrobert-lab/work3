package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.common.enums.BusinessCodeEnum;
import com.converter.ArticleConverter;
import com.domain.dto.ArticleDto;
import com.domain.dto.TokenDto;
import com.domain.enums.ArticleVisibilityEnum;
import com.domain.po.ArticlePo;
import com.domain.po.EIException;
import com.domain.po.UserPo;
import com.domain.vo.ArticleVo;
import com.mapper.ArticleMapper;
import com.service.IArticleMetricService;
import com.service.IArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticlePo> implements IArticleService {

    private final IArticleMetricService articleMetricService;
    @Override
    public ArticleVo getArticleById(Integer id) {
        ArticlePo articlePo = getById(id);
        if (articlePo == null) {
            throw new EIException(BusinessCodeEnum.ARTICLE_NOT_EXIST);
        }
        articleMetricService.recordView(id, null);
        UserPo userPo = Db.lambdaQuery(UserPo.class)
                .eq(UserPo::getId, articlePo.getUserId()).one();
        String authorName = userPo != null ? userPo.getUsername() : null;
        return ArticleConverter.INSTANCE.articlePoToArticleVo(articlePo, authorName);
    }

    @Override
    public ArticleVo addArticle(ArticleDto articleDto, TokenDto tokenDto) {
        ArticlePo articlePo = ArticleConverter.INSTANCE.articleDtoToArticlePo(articleDto,tokenDto.getUserId());
        if (articlePo.getVisibility() == null) {
            articlePo.setVisibility(ArticleVisibilityEnum.PUBLIC);
        }
        save(articlePo);
        articleMetricService.initializeMetrics(articlePo.getId());
        UserPo userPo = Db.lambdaQuery(UserPo.class)
                .eq(UserPo::getId, tokenDto.getUserId()).one();
        return ArticleConverter.INSTANCE.articlePoToArticleVo(articlePo, userPo.getUsername());
    }

    @Override
    public void updateArticle(Integer id, ArticleDto articleDto) {
        LambdaUpdateWrapper<ArticlePo> updateWrapper = new LambdaUpdateWrapper<ArticlePo>()
                .eq(ArticlePo::getId, id)
                .set(articleDto.getTitle() != null, ArticlePo::getTitle, articleDto.getTitle())
                .set(articleDto.getContent() != null, ArticlePo::getContent, articleDto.getContent())
                .set(articleDto.getSummary() != null, ArticlePo::getSummary, articleDto.getSummary())
                .set(articleDto.getCoverImage() != null, ArticlePo::getCoverImage, articleDto.getCoverImage())
                .set(articleDto.getVisibility() != null, ArticlePo::getVisibility, articleDto.getVisibility())
                .set(ArticlePo::getUpdatedAt, LocalDateTime.now());
        update(null, updateWrapper);
    }


}