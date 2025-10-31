package com.interceptor;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.common.enums.BusinessCodeEnum;
import com.common.enums.HttpStatusEnum;
import com.domain.enums.ArticleVisibilityEnum;
import com.domain.po.ArticlePo;
import com.domain.po.EIException;
import com.domain.dto.TokenDto;
import com.service.impl.FriendServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 文章访问权限拦截器
 */
@Component
public class ArticleInterceptor implements HandlerInterceptor {

    @Autowired
    private FriendServiceImpl friendService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        // 只拦截文章详情查询接口
        if (!requestURI.startsWith("/article/") || requestURI.split("/").length != 3) {
            return true;
        }
        // 从URL中提取文章ID
        Integer articleId = getArticleIdFromUrl(requestURI);
        if (articleId == null) {
            throw new EIException(BusinessCodeEnum.ARTICLE_NOT_EXIST);
        }
        // 查询文章信息
        ArticlePo article = Db.lambdaQuery(ArticlePo.class)
                .select(ArticlePo::getVisibility)
                .eq(ArticlePo::getId, articleId)
                .one();
        if (article == null) {
            throw new EIException(BusinessCodeEnum.ARTICLE_NOT_EXIST);
        }
        // 获取当前登录用户信息
        TokenDto tokenDto = (TokenDto) request.getAttribute("tokenDto");
        Long currentUserId = tokenDto != null ? tokenDto.getUserId() : null;

        ArticleVisibilityEnum visibility = article.getVisibility();

        if (visibility == ArticleVisibilityEnum.ONESELF) {
            if (!currentUserId.equals(article.getUserId())) {
                throw new EIException(HttpStatusEnum.BAD_REQUEST);
            }
        } else if (visibility == ArticleVisibilityEnum.FRIEND_ONLY) {
            if (!currentUserId.equals(article.getUserId())) {
                if (!friendService.isMutualFriend(currentUserId, article.getUserId())) {
                    throw new EIException(HttpStatusEnum.BAD_REQUEST);
                }
            }
        } else if (visibility == ArticleVisibilityEnum.PUBLIC) {

        } else {
            throw new EIException(HttpStatusEnum.BAD_REQUEST);
        }

        return true;
    }

    /**
     * 从URL中提取文章ID
     */
    private Integer getArticleIdFromUrl(String uri) {
        String[] parts = uri.split("/");
        if (parts.length < 3) {
            throw new EIException(HttpStatusEnum.BAD_REQUEST);
        }
        return Integer.parseInt(parts[2]);
    }
}