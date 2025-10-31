package com.controller;

import com.annotation.APPLoginUser;
import com.common.enums.BusinessCodeEnum;
import com.converter.ArticleConverter;
import com.domain.dto.ArticleDto;
import com.domain.dto.TokenDto;
import com.domain.po.ArticlePo;
import com.domain.vo.ArticleVo;
import com.service.impl.ArticleServiceImpl;
import com.utils.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleServiceImpl articleService;
    @Operation(summary = "获取文章")
    @GetMapping("/{id}")
    public ResponseResult<ArticleVo> getArticle(@PathVariable Integer id) {
        ArticleVo articleVo= articleService.getArticleById(id);
        return ResponseResult.success(BusinessCodeEnum.QUERY_ARTICLE_SUCCESS, articleVo);
    }

    @Operation(summary = "添加文章")
    @PostMapping
    public ResponseResult<ArticleVo> addArticle(@RequestBody ArticleDto articleDto, @APPLoginUser TokenDto tokenDto) {
        ArticleVo articleVo = articleService.addArticle(articleDto, tokenDto);
        return ResponseResult.success(BusinessCodeEnum.ADD_ARTICLE_SUCCESS, articleVo);
    }

    @Operation(summary = "更新文章")
    @PutMapping("/{id}")
    public ResponseResult<ArticleVo> updateArticle(@PathVariable Integer id, @RequestBody ArticleDto articleDto) {
        articleService.updateArticle(id, articleDto);
        ArticleVo updatedVo = articleService.getArticleById(id);
        return ResponseResult.success(BusinessCodeEnum.UPDATE_ARTICLE_SUCCESS, updatedVo);
    }

    @Operation(summary = "删除文章")
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteArticle(@PathVariable Integer id) {
        articleService.removeById(id);
        return ResponseResult.success(BusinessCodeEnum.DELETE_ARTICLE_SUCCESS);
    }
}