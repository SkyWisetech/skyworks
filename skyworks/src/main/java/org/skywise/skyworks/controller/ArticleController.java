package org.skywise.skyworks.controller;

import org.skywise.skyworks.common.DTO.ArticleDTO;
import org.skywise.skyworks.common.DTO.ArticleOptDTO;
import org.skywise.skyworks.common.DTO.Response;
import org.skywise.skyworks.common.VO.ArticlePageVO;
import org.skywise.skyworks.common.VO.ArticleVO;
import org.skywise.skyworks.common.constant.StrConstant;
import org.skywise.skyworks.common.utils.TokenUtil;
import org.skywise.skyworks.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Author: LIGHT
 * @Date: 2024/7/5 星期五 11:28
 * @Description: 文章管理
 */
@RestController
@RequestMapping("/skyworks/article/")
public class ArticleController {
    @Autowired
    private IArticleService articleService;

    @PostMapping("/getArticleById")
    public Response<ArticleVO> getArticleById(@RequestBody ArticleOptDTO articleOptDTO) {
        ArticleVO articleVO = articleService.getArticleById(articleOptDTO.getArticleId());
        return Response.success(articleVO);
    }

    @PostMapping("/listArticle")
    public Response<ArticlePageVO> listArticle(@RequestBody ArticleOptDTO articleOptDTO, HttpServletRequest request) {
        // 拦截器不拦截该接口，手动保存token信息
        TokenUtil.saveUserInfoByRequest(request);
        ArticlePageVO articlePageVO = articleService.listArticle(articleOptDTO);
        return Response.success(articlePageVO);
    }

    @PostMapping("/listArticleByLabel")
    public Response<ArticlePageVO> listArticleByLabel(@RequestBody ArticleOptDTO articleOptDTO) {
        ArticlePageVO articlePageVO = articleService.listArticleByLabel(articleOptDTO);
        return Response.success(articlePageVO);
    }

    @PostMapping("/insertArticle")
    public Response<String> insertArticle(@Valid @RequestBody ArticleDTO articleDTO) {
        articleService.insertArticle(articleDTO);
        return Response.successMsg(StrConstant.INSERT_ARTICLE_SUCCESS);
    }

    @PostMapping("/updateArticle")
    public Response<String> updateArticle(@RequestBody ArticleDTO articleDTO) {
        articleService.updateArticle(articleDTO);
        return Response.successMsg(StrConstant.UPDATE_ARTICLE_SUCCESS);
    }


    @PostMapping("/deleteArticle")
    public Response<String> deleteArticle(@RequestBody ArticleOptDTO articleOptDTO) {
        articleService.deleteArticle(articleOptDTO.getArticleId());
        return Response.successMsg(StrConstant.DELETE_ARTICLE_SUCCESS);
    }


}
