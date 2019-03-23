package com.example.paper.controller;


import com.example.paper.entity.*;
import com.example.paper.service.ManageArticle;
import com.example.paper.service.ManageUser;
import com.example.paper.utils.JwtUtil;
import com.example.paper.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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


    @RequestMapping(value = "/out", method = RequestMethod.GET)
    public ResultVO LogOut() {
        ResultVO resultVO = new ResultVO();
        resultVO.setState(1);
        resultVO.setMsg("成功");
        resultVO.setData(null);
        return resultVO;
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
        userInfo.setSex(user.getSex());
        userInfo.setUsername(user.getUsername());
        resultVO.setState(1);
        resultVO.setMsg("成功");
        resultVO.setData(userInfo);
        return resultVO;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    public ResultVO Update(UserModified modified, HttpServletRequest request) {
        ResultVO resultVO = new ResultVO();
        User user = manageUser.FindOne(JwtUtil.validateToken(request.getHeader("Authorization")).get("account").toString());
        user.setUsername(modified.getUsername());
        user.setDescription(modified.getDescription());
        user.setSex(modified.getSex());
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
        List<entryArticle> entryArticleList = new ArrayList<>();
        for (Article a : articleList) {
            entryArticle article = new entryArticle();
            User user = manageUser.FindOne(a.getAccount());
            if (user == null) {
                article.setAuthor("unknown");
            } else {
                article.setAuthor(user.getUsername());
            }
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
