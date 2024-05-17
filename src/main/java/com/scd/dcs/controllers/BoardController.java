package com.scd.dcs.controllers;


import com.scd.dcs.domains.vos.BoardVo;
import com.scd.dcs.services.ArticleService;
import com.scd.dcs.services.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final ArticleService articleService;

    @Autowired
    BoardController(BoardService boardService,
                    ArticleService articleService){
        this.boardService = boardService;
        this.articleService = articleService;
    }


    @RequestMapping(value = "/list",method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getList(@RequestParam(value = "page", required = false, defaultValue = "1") int page, BoardVo board){
        System.out.println(1);
        ModelAndView modelAndView = new ModelAndView();
        if(board.getCode() == null){
            board.setCode("free");
        }
        board.setRequestPage(page);
        modelAndView.setViewName("board/list");
        modelAndView.addObject("articles",this.articleService.getArticlesOf(board));
        System.out.println(Arrays.deepToString(this.articleService.getArticlesOf(board)));
        System.out.println(board);
        modelAndView.addObject("board",board);
        return modelAndView;
    }
}
