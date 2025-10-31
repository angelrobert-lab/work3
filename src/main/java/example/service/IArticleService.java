package com.service;

import com.domain.dto.ArticleDto;
import com.domain.dto.TokenDto;
import com.domain.po.ArticlePo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.domain.vo.ArticleVo;

public interface IArticleService extends IService<ArticlePo> {

    ArticleVo addArticle(ArticleDto articleDto, TokenDto tokenDto);

    void updateArticle(Integer id, ArticleDto articleDto);

    ArticleVo getArticleById(Integer id);
}
