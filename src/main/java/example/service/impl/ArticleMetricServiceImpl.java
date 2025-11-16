package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.common.enums.BusinessCodeEnum;
import com.converter.ArticleMetricConverter;
import com.domain.dto.ArticleMetricUpdateDto;
import com.domain.po.ArticleLikePo;
import com.domain.po.ArticleMetricPo;
import com.domain.po.CommentsPo;
import com.domain.po.EIException;
import com.domain.po.ArticlePo;
import com.domain.vo.ArticleMetricVo;
import com.mapper.ArticleMetricMapper;
import com.service.IArticleMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ArticleMetricServiceImpl extends ServiceImpl<ArticleMetricMapper, ArticleMetricPo> implements IArticleMetricService {

    @Override
    public void initializeMetrics(Integer articleId) {
        ensureMetrics(articleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleMetricVo recordView(Integer articleId, Long userId) {
        ensureMetrics(articleId);
        LambdaUpdateWrapper<ArticleMetricPo> wrapper = new LambdaUpdateWrapper<ArticleMetricPo>()
                .eq(ArticleMetricPo::getArticleId, articleId)
                .setSql("view_count = view_count + 1")
                .set(ArticleMetricPo::getUpdatedAt, LocalDateTime.now());
        update(wrapper);
        return getMetrics(articleId, userId);
    }

    @Override
    public ArticleMetricVo getMetrics(Integer articleId, Long userId) {
        ArticleMetricPo metric = ensureMetrics(articleId);
        boolean liked = false;
        if (userId != null) {
            Long count = Db.lambdaQuery(ArticleLikePo.class)
                    .eq(ArticleLikePo::getArticleId, articleId)
                    .eq(ArticleLikePo::getUserId, userId)
                    .count();
            liked = count != null && count > 0;
        }
        return ArticleMetricConverter.INSTANCE.toVo(metric, liked);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleMetricVo toggleLike(Integer articleId, Long userId) {
        if (userId == null) {
            throw new EIException(BusinessCodeEnum.TOKEN_MISSING);
        }
        ensureMetrics(articleId);
        ArticleLikePo existing = Db.lambdaQuery(ArticleLikePo.class)
                .eq(ArticleLikePo::getArticleId, articleId)
                .eq(ArticleLikePo::getUserId, userId)
                .one();
        LambdaUpdateWrapper<ArticleMetricPo> wrapper = new LambdaUpdateWrapper<ArticleMetricPo>()
                .eq(ArticleMetricPo::getArticleId, articleId)
                .set(ArticleMetricPo::getUpdatedAt, LocalDateTime.now());
        if (existing == null) {
            ArticleLikePo newLike = ArticleLikePo.builder()
                    .articleId(articleId)
                    .userId(userId)
                    .createdAt(LocalDateTime.now())
                    .build();
            Db.save(newLike);
            wrapper.setSql("like_count = like_count + 1");
        } else {
            Db.removeById(existing.getId(), ArticleLikePo.class);
            wrapper.setSql("like_count = CASE WHEN like_count > 0 THEN like_count - 1 ELSE 0 END");
        }
        update(wrapper);
        return getMetrics(articleId, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshCommentCount(Integer articleId) {
        ensureMetrics(articleId);
        long total = Db.lambdaQuery(CommentsPo.class)
                .eq(CommentsPo::getArticleId, articleId)
                .count();
        LambdaUpdateWrapper<ArticleMetricPo> wrapper = new LambdaUpdateWrapper<ArticleMetricPo>()
                .eq(ArticleMetricPo::getArticleId, articleId)
                .set(ArticleMetricPo::getCommentCount, total)
                .set(ArticleMetricPo::getUpdatedAt, LocalDateTime.now());
        update(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleMetricVo updateBaseCounters(Integer articleId, ArticleMetricUpdateDto dto) {
        ArticleMetricUpdateDto payload = dto == null ? new ArticleMetricUpdateDto() : dto;
        ensureMetrics(articleId);
        LambdaUpdateWrapper<ArticleMetricPo> wrapper = new LambdaUpdateWrapper<ArticleMetricPo>()
                .eq(ArticleMetricPo::getArticleId, articleId);
        boolean needUpdate = false;
        if (payload.getCommentCount() != null) {
            wrapper.set(ArticleMetricPo::getCommentCount, payload.getCommentCount());
            needUpdate = true;
        }
        if (payload.getFavoriteCount() != null) {
            wrapper.set(ArticleMetricPo::getFavoriteCount, payload.getFavoriteCount());
            needUpdate = true;
        }
        if (needUpdate) {
            wrapper.set(ArticleMetricPo::getUpdatedAt, LocalDateTime.now());
            update(wrapper);
        }
        return getMetrics(articleId, null);
    }

    private ArticleMetricPo ensureMetrics(Integer articleId) {
        if (articleId == null) {
            throw new EIException(BusinessCodeEnum.ARTICLE_NOT_EXIST);
        }
        ArticleMetricPo metric = getById(articleId);
        if (metric == null) {
            Long articleCount = Db.lambdaQuery(ArticlePo.class)
                    .eq(ArticlePo::getId, articleId)
                    .count();
            if (articleCount == null || articleCount == 0) {
                throw new EIException(BusinessCodeEnum.ARTICLE_NOT_EXIST);
            }
            metric = ArticleMetricPo.builder()
                    .articleId(articleId)
                    .viewCount(0L)
                    .likeCount(0L)
                    .commentCount(0L)
                    .favoriteCount(0L)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            save(metric);
        }
        return metric;
    }
}
