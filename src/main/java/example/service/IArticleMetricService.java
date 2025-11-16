package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.domain.dto.ArticleMetricUpdateDto;
import com.domain.po.ArticleMetricPo;
import com.domain.vo.ArticleMetricVo;

public interface IArticleMetricService extends IService<ArticleMetricPo> {

    /** 初始化文章互动指标 */
    void initializeMetrics(Integer articleId);

    /** 记录文章浏览 */
    ArticleMetricVo recordView(Integer articleId, Long userId);

    /** 获取文章互动指标 */
    ArticleMetricVo getMetrics(Integer articleId, Long userId);

    /** 切换点赞状态 */
    ArticleMetricVo toggleLike(Integer articleId, Long userId);

    /** 刷新评论总数 */
    void refreshCommentCount(Integer articleId);

    /** 更新评论/收藏等静态指标 */
    ArticleMetricVo updateBaseCounters(Integer articleId, ArticleMetricUpdateDto dto);
}
