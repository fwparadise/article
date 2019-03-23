package com.example.paper.controller;

import com.example.paper.converter.ComposeFormToArticle;
import com.example.paper.entity.*;
import com.example.paper.form.ComposeForm;
import com.example.paper.service.ManageArticle;
import com.example.paper.service.ManageCollect;
import com.example.paper.service.ManageComment;
import com.example.paper.service.ManageUser;
import com.example.paper.utils.JwtUtil;
import com.example.paper.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/article")
public class article {
    private final ManageArticle manageArticle;
    private final ManageUser manageUser;
    private final ManageCollect manageCollect;
    private final ManageComment manageComment;

    @Autowired
    public article(ManageArticle manageArticle, ManageUser manageUser, ManageCollect manageCollect, ManageComment manageComment) {
        this.manageArticle = manageArticle;
        this.manageUser = manageUser;
        this.manageCollect = manageCollect;
        this.manageComment = manageComment;
    }

    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public ResultVO addArticle(ComposeForm composeForm, HttpServletRequest request) {
        ResultVO resultVO = new ResultVO();
        String account = JwtUtil.validateToken(request.getHeader("Authorization")).get("account").toString();
        Article article = ComposeFormToArticle.convert(composeForm);
        article.setAccount(account);
        if (manageArticle.addArticle(article)!=null) {
            resultVO.setState(1);
            resultVO.setMsg("发表成功");
            resultVO.setData(null);
        } else {
            resultVO.setState(0);
            resultVO.setMsg("发表失败");
            resultVO.setData(null);
        }
        return resultVO;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultVO listArticle(@RequestParam("kind") String kind, @RequestParam("current") int current, @RequestParam("size") int size) {
        Page<Article> page = manageArticle.getList(kind, current, size);
        List<entryArticle> result = new ArrayList<>();
        List<Article> list = page.getContent();
        for (Article article:list){
            entryArticle a = new entryArticle();
            a.setArticleId(article.getArticleId());
            a.setCreateTime(article.getCreateTime());
            a.setTitle(article.getTitle());
            User user = manageUser.FindOne(article.getAccount());
            if (user == null) {
                a.setAuthor("unknown");
            } else {
                a.setAuthor(user.getUsername());
            }
            result.add(a);
        }
        ResultArticle resultArticle = new ResultArticle();
        resultArticle.setList(result);
        resultArticle.setTotal(page.getTotalElements());
        ResultVO resultVO = new ResultVO();
        resultVO.setState(1);
        resultVO.setMsg("success");
        resultVO.setData(resultArticle);
        return resultVO;
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResultVO getArticle(@RequestParam("id") String id) {
        Article article = manageArticle.findOne(id);
        ResultVO resultVO = new ResultVO();
        if (article == null) {
            resultVO.setState(0);
            resultVO.setMsg("文章不存在");
            System.out.println("hello");
            resultVO.setData(null);
        } else {
            resultVO.setState(1);
            resultVO.setMsg("成功");
            articleDetail articleDetail = new articleDetail();
            articleDetail.setArticle(article);
            User user;
            user = manageUser.FindOne(article.getAccount());
            if (user == null) {
                articleDetail.setAuthor("unknown");
            } else {
                articleDetail.setAuthor(user.getUsername());
            }
            resultVO.setData(articleDetail);
        }
        return resultVO;
    }

    @RequestMapping(value = "/update",method = RequestMethod.PATCH)
    public ResultVO update(ArticleModified articleModified){
        Article article=manageArticle.findOne(articleModified.getArticleId());
        article.setTitle(articleModified.getTitle());
        article.setKind(articleModified.getKind());
        article.setContent(articleModified.getContent());
        manageArticle.updateArticle(article);
        ResultVO resultVO=new ResultVO();
        resultVO.setMsg("成功");
        resultVO.setState(1);
        resultVO.setData(null);
        return resultVO;
    }
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public ResultVO delete(@RequestParam("id")String articleId){
        manageCollect.delete(articleId);
        manageComment.deleteComment(articleId);
        manageArticle.deleteOne(articleId);
        ResultVO resultVO=new ResultVO();
        resultVO.setState(1);
        resultVO.setMsg("成功");
        resultVO.setData(null);
        return resultVO;
    }
}
