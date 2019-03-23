package com.example.paper.controller;


import com.example.paper.entity.entryArticle;
import com.example.paper.service.ManageCollect;
import com.example.paper.utils.JwtUtil;
import com.example.paper.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/collect")
public class collect {
    private final ManageCollect manageCollect;

    @Autowired
    public collect(ManageCollect manageCollect) {
        this.manageCollect = manageCollect;
    }

    @RequestMapping(value = "/exist", method = RequestMethod.GET)
    public ResultVO exist(@RequestParam("articleId") String articleId, HttpServletRequest request) {
        ResultVO resultVO = new ResultVO();
        String account = JwtUtil.validateToken(request.getHeader("Authorization")).get("account").toString();
        resultVO.setState(1);
        resultVO.setMsg("成功");
        resultVO.setData(manageCollect.exist(account, articleId));
        return resultVO;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultVO getList(HttpServletRequest request) {
        ResultVO resultVO = new ResultVO();
        String account = JwtUtil.validateToken(request.getHeader("Authorization")).get("account").toString();
        List<entryArticle> entryArticleList = manageCollect.getCollect(account);
        resultVO.setState(1);
        resultVO.setMsg("成功");
        resultVO.setData(entryArticleList);
        return resultVO;
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.DELETE)
    public ResultVO delete(@RequestParam("articleId") String articleId, HttpServletRequest request) {
        ResultVO resultVO = new ResultVO();
        String account = JwtUtil.validateToken(request.getHeader("Authorization")).get("account").toString();
        if (manageCollect.delete(account, articleId)) {
            resultVO.setState(1);
            resultVO.setMsg("取消成功");
            resultVO.setData(true);
        } else {
            resultVO.setState(0);
            resultVO.setMsg("取消失败");
            resultVO.setData(false);
        }
        return resultVO;
    }

    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public ResultVO add(@RequestParam("articleId") String articleId, HttpServletRequest request) {
        ResultVO resultVO = new ResultVO();
        resultVO.setState(1);
        resultVO.setMsg("成功");
        resultVO.setData(manageCollect.add(JwtUtil.validateToken(request.getHeader("Authorization")).get("account").toString(), articleId));
        return resultVO;
    }
}
