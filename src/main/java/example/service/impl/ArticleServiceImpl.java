package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.converter.ArticleConverter;
import com.domain.dto.ArticleDto;
import com.domain.dto.TokenDto;
import com.domain.enums.ArticleVisibilityEnum;
import com.domain.po.ArticlePo;
import com.domain.po.UserPo;
import com.domain.vo.ArticleVo;
import com.mapper.ArticleMapper;
import com.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticlePo> implements IArticleService {
    @Override
    public ArticleVo getArticleById(Integer id) {
        UserPo userPo = Db.lambdaQuery(UserPo.class)
                .eq(UserPo::getId, id).one();
        return ArticleConverter.INSTANCE.articlePoToArticleVo(getById(id), userPo.getUsername());
    }

    @Override
    public ArticleVo addArticle(ArticleDto articleDto, TokenDto tokenDto) {
        ArticlePo articlePo = ArticleConverter.INSTANCE.articleDtoToArticlePo(articleDto,tokenDto.getUserId());
        if (articlePo.getVisibility() == null) {
            articlePo.setVisibility(ArticleVisibilityEnum.PUBLIC);
        }
        save(articlePo);
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