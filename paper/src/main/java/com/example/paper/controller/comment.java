package com.example.paper.controller;

import com.example.paper.bean.CommentItem;
import com.example.paper.entity.Comment;
import com.example.paper.entity.User;
import com.example.paper.service.ManageComment;
import com.example.paper.service.ManageUser;
import com.example.paper.utils.JwtUtil;
import com.example.paper.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/comment")
public class comment {
    private final ManageComment manageComment;
    private final ManageUser manageUser;

    @Autowired
    public comment(ManageComment manageComment, ManageUser manageUser) {
        this.manageComment = manageComment;
        this.manageUser = manageUser;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResultVO add(@RequestParam("articleId") Long articleId, @RequestParam("content") String content, HttpServletRequest request) {
        ResultVO resultVO = new ResultVO();
        String account = JwtUtil.validateToken(request.getHeader("Authorization")).get("account").toString();
        Comment comment = new Comment();
        comment.setAccount(account);
        comment.setArticleId(articleId);
        comment.setContent(content);
        manageComment.addComment(comment);
        resultVO.setData(null);
        resultVO.setMsg("评论成功");
        resultVO.setState(1);
        return resultVO;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultVO getList(@RequestParam("articleId") Long id) {
        ResultVO resultVO = new ResultVO();
        User user;
        List<CommentItem> resultlist = new ArrayList<>();
        List<Comment> list = manageComment.getList(id);
        for (Comment comment : list) {
            CommentItem resultComment = new CommentItem();
            user = manageUser.FindOne(comment.getAccount());
            if (user == null) {
                resultComment.setUsername("unkonown");
            } else {
                resultComment.setUsername(user.getUsername());
            }
            resultComment.setContent(comment.getContent());
            resultlist.add(resultComment);
        }
        resultVO.setState(1);
        resultVO.setMsg("成功");
        resultVO.setData(resultlist);
        return resultVO;
    }
}
