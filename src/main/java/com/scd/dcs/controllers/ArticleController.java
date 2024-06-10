package com.scd.dcs.controllers;


import com.scd.dcs.config.security.domains.SecurityUser;
import com.scd.dcs.domains.entities.ArticleEntity;
import com.scd.dcs.domains.entities.CommentEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.results.CommonResult;
import com.scd.dcs.results.Result;
import com.scd.dcs.services.ArticleService;
import com.scd.dcs.services.BoardService;
import com.scd.dcs.services.CommentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
@RequestMapping("/article")
public class ArticleController {
    private final BoardService boardService;

    private final ArticleService articleService;

    private final CommentService commentService;

    @Autowired
    ArticleController(BoardService boardService, ArticleService articleService,CommentService commentService) {
        this.boardService = boardService;
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @RequestMapping(value = "write", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getWrite() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("boards", this.boardService.getBoards());
        modelAndView.setViewName("article/write");
        return modelAndView;
    }

    @RequestMapping(value = "write", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postWrite(Authentication authentication , ArticleEntity article ) {
        // 게시글 작성 겨로가가 SUCCESS 일 때,
        // "/article/read?index=" + article.getIndex()
        // 위 주소로 redirect 시킬 것. (당연히 404 뜸. ㄱㅊ)
        ModelAndView modelAndView = new ModelAndView();
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        article.setUserEmail(user.getUserEntity().getEmail());
        Result result = this.articleService.write(article);
        modelAndView.addObject("boards", this.boardService.getBoards());
        modelAndView.addObject("result", result.name());
        System.out.println(result);
        if (result == CommonResult.SUCCESS) {
            modelAndView.setViewName("redirect:/article/read?index=" + article.getIndex());
        } else {
            modelAndView.setViewName("article/write");
        }
        return modelAndView;
    }

    @RequestMapping(value = "read", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRead(@RequestParam(value = "index") int index, Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        UserEntity user = securityUser.getUserEntity();
        modelAndView.addObject("user", user);
        ArticleEntity dbArticle = this.articleService.getArticle(index);
        if (dbArticle == null) {
            modelAndView.addObject("result", "FAILURE");
        }
        boolean viewResult = this.articleService.updateArticle(dbArticle);
        if (viewResult == false) {
            modelAndView.addObject("result", "FAILURE");
        }
        modelAndView.addObject("result", "SUCCESS");
        modelAndView.addObject("article", dbArticle);
        modelAndView.setViewName("article/read");
        return modelAndView;
    }

    @RequestMapping(value = "modify", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getModify(Authentication authentication,
                                  @RequestParam("index") int index) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        UserEntity user = securityUser.getUserEntity();
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("article/modify");
        ArticleEntity dbArticle;
        if (user == null) {
            modelAndView.addObject("article", null);
            return modelAndView;
        } else {
            dbArticle = this.articleService.getArticle(index);
            if (dbArticle != null && !dbArticle.getUserEmail().equals(user.getEmail())) {
                dbArticle = null;
            }
            modelAndView.addObject("article", dbArticle);
        }
        return modelAndView;

    }

    @RequestMapping(value = "modify", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postModify(Authentication authentication,
                                  ArticleEntity article) {
        SecurityUser user=(SecurityUser) authentication.getPrincipal();
        ModelAndView modelAndView = new ModelAndView();
        Result result = this.articleService.modify(user.getUserEntity(), article);
        if (result == CommonResult.SUCCESS) {
            modelAndView.setViewName("redirect:/article/read?index=" + article.getIndex());
        } else {
            modelAndView.setViewName("article/modify");
            modelAndView.addObject("result", result.name());
        }
        return modelAndView;
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postDelete(Authentication authentication, ArticleEntity article) {
        SecurityUser user=(SecurityUser) authentication.getPrincipal();
        Result result = this.articleService.delete(user.getUserEntity(), article);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result.name().toLowerCase());
        if(result == CommonResult.SUCCESS){
            jsonObject.put("code", article.getBoardCode());
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "comment/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CommentEntity> getCommentIndex(@RequestParam("articleIndex") int articleIndex){
        List<CommentEntity> commentEntities = this.commentService.getComments(articleIndex);
        return commentEntities;
    }

    @RequestMapping(value = "comment/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postCommentIndex(Authentication authentication, CommentEntity comment){
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        UserEntity user = securityUser.getUserEntity();
        comment.setUserEmail(user.getEmail());
        Result result = this.commentService.write(comment);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result",result.name().toLowerCase());
        return jsonObject.toString();
    }

    @RequestMapping(value = "comment/", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteCommentIndex(@SessionAttribute(value = "user",required = false)UserEntity user, @RequestParam("index") int index){
        Result result = this.commentService.deleteComment(index,user);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result",result.name().toLowerCase());
        return jsonObject.toString();
    }

    @RequestMapping(value = "comment/", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchCommentIndex(@SessionAttribute(value = "user" ,required = false) UserEntity user,
                             @RequestParam("index") int index,
                             @RequestParam("newContent") String newContent){
        Result result = this.commentService.modifyComment(user,index,newContent);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result",result.name().toLowerCase());
        return jsonObject.toString();
    }
}
