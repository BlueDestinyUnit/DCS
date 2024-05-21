package com.scd.dcs.controllers;


import com.scd.dcs.config.security.domains.SecurityUser;
import com.scd.dcs.domains.entities.ArticleEntity;
import com.scd.dcs.results.CommonResult;
import com.scd.dcs.results.Result;
import com.scd.dcs.services.ArticleService;
import com.scd.dcs.services.BoardService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("/article")
public class ArticleController {
    private final BoardService boardService;

    private final ArticleService articleService;

    @Autowired
    ArticleController(BoardService boardService, ArticleService articleService) {
        this.boardService = boardService;
        this.articleService = articleService;
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
    public ModelAndView getRead(
            Authentication authentication
            ,@RequestParam(value = "index") int index) {
        SecurityUser loginUser = (SecurityUser) authentication.getPrincipal();

        ModelAndView modelAndView = new ModelAndView();
        ArticleEntity dbArticle = this.articleService.getArticle(index);
        if (dbArticle == null) {
            modelAndView.addObject("result", "FAILURE");

        }else{
            modelAndView.addObject("result", "SUCCESS");
        }
        if(dbArticle.getUserEmail().equals(loginUser.getUserEntity().getEmail())) {
            modelAndView.addObject("mine", true);
        }else{
            modelAndView.addObject("mine", false);
        }
        boolean viewResult = this.articleService.updateArticle(dbArticle);
        if (viewResult == false) {
            modelAndView.addObject("result", "FAILURE");
        }

        modelAndView.addObject("article", dbArticle);
        modelAndView.setViewName("article/read");
        return modelAndView;
    }

    @RequestMapping(value = "modify", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getModify(Authentication authentication,
                                  @RequestParam("index") int index) {
        SecurityUser user=(SecurityUser) authentication.getPrincipal();
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("article/modify");
        ArticleEntity dbArticle;
        if (authentication == null) {
            modelAndView.addObject("article", null);
            return modelAndView;
        } else {
            dbArticle = this.articleService.getArticle(index);
            if (dbArticle != null && !dbArticle.getUserEmail().equals(user.getUserEntity().getEmail())) {
                dbArticle = null;
            }
            modelAndView.addObject("article", dbArticle);
        }
        return modelAndView;

    }

    @RequestMapping(value = "modify", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getModify(Authentication authentication,
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
}
