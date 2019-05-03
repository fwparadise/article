package com.example.paper.controller;


import com.example.paper.bean.ArticleItem;
import com.example.paper.bean.UserInfo;
import com.example.paper.bean.UserModified;
import com.example.paper.entity.Article;
import com.example.paper.entity.User;
import com.example.paper.service.ManageArticle;
import com.example.paper.service.ManageUser;
import com.example.paper.utils.JwtUtil;
import com.example.paper.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/user")

public class user {
    private final ManageUser manageUser;
    private final ManageArticle manageArticle;

    @Autowired
    public user(ManageUser manageUser, ManageArticle manageArticle) {
        this.manageUser = manageUser;
        this.manageArticle = manageArticle;
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResultVO GetInfo(HttpServletRequest request) {
        User user;
        ResultVO resultVO = new ResultVO();
        String account = JwtUtil.validateToken(request.getHeader("Authorization")).get("account").toString();
        user = manageUser.FindOne(account);
        UserInfo userInfo=new UserInfo();
        userInfo.setAccount(user.getAccount());
        userInfo.setDescription(user.getDescription());
        userInfo.setGender(user.getGender());
        userInfo.setUsername(user.getUsername());
        resultVO.setState(1);
        resultVO.setMsg("成功");
        resultVO.setData(userInfo);
        return resultVO;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResultVO Update(UserModified modified, HttpServletRequest request) {
        ResultVO resultVO = new ResultVO();
        User user = manageUser.FindOne(JwtUtil.validateToken(request.getHeader("Authorization")).get("account").toString());
        user.setUsername(modified.getUsername());
        user.setDescription(modified.getDescription());
        user.setGender(modified.getGender());
        manageUser.UpdateUser(user);
        resultVO.setData(null);
        resultVO.setMsg("成功");
        resultVO.setState(1);
        return resultVO;
    }

    @RequestMapping(value = "/created", method = RequestMethod.GET)
    public ResultVO Created(HttpServletRequest request) {
        ResultVO resultVO = new ResultVO();
        String account = JwtUtil.validateToken(request.getHeader("Authorization")).get("account").toString();
        List<Article> articleList = manageArticle.getCreated(account);
        User user = manageUser.FindOne(account);
        List<ArticleItem> entryArticleList = new ArrayList<>();
        for (Article a : articleList) {
            ArticleItem article = new ArticleItem();
            article.setAuthor(user.getUsername());
            article.setCreateTime(a.getCreateTime());
            article.setTitle(a.getTitle());
            article.setArticleId(a.getArticleId());
            entryArticleList.add(article);
        }
        resultVO.setState(1);
        resultVO.setMsg("成功");
        resultVO.setData(entryArticleList);
        return resultVO;
    }

    @RequestMapping(value = "/profile/update", method = RequestMethod.POST)
    public ResultVO UpdateProfile( @RequestParam("profile") MultipartFile profile, HttpServletRequest request) throws IOException {
        String account = JwtUtil.validateToken(request.getHeader("Authorization")).get("account").toString();
        byte[] data=profile.getBytes();
        User user=manageUser.FindOne(account);
        user.setProfile(data);
        manageUser.UpdateUser(user);
        ResultVO resultVO=new ResultVO();
        resultVO.setMsg("成功");
        resultVO.setState(1);
        resultVO.setData(null);
        return resultVO;
    }
}
