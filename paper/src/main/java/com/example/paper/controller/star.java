package com.example.paper.controller;

import com.example.paper.entity.Star;
import com.example.paper.service.ManageStar;
import com.example.paper.utils.JwtUtil;
import com.example.paper.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "/star")
@RestController
public class star {
    private final ManageStar manageStar;

    @Autowired
    public star(ManageStar manageStar) {
        this.manageStar = manageStar;
    }

    @RequestMapping(value = "/exist", method = RequestMethod.GET)
    public ResultVO exist(@RequestParam("articleId") String articleId, HttpServletRequest request) {
        ResultVO resultVO = new ResultVO();
        String account = JwtUtil.validateToken(request.getHeader("Authorization")).get("account").toString();
        resultVO.setState(1);
        resultVO.setMsg("成功");
        resultVO.setData(manageStar.exist(account, articleId));
        return resultVO;
    }

    @RequestMapping(value = "/getstar", method = RequestMethod.GET)
    public ResultVO getStar(@RequestParam("articleId") String articleId) {
        ResultVO resultVO = new ResultVO();
        resultVO.setState(1);
        resultVO.setMsg("成功");
        resultVO.setData(manageStar.average(articleId));
        return resultVO;
    }

    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public ResultVO add(@RequestParam("articleId") String articleId, @RequestParam("score") int score, HttpServletRequest request) {

        ResultVO resultVO = new ResultVO();
        String account = JwtUtil.validateToken(request.getHeader("Authorization")).get("account").toString();
        Star star = new Star();
        star.setAccount(account);
        star.setArticleId(articleId);
        star.setScore(score);
        manageStar.add(star);
        resultVO.setState(1);
        resultVO.setMsg("成功");
        resultVO.setData(null);
        return resultVO;
    }
}
