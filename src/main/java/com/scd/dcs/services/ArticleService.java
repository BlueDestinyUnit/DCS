package com.scd.dcs.services;

import com.scd.dcs.domains.entities.ArticleEntity;
import com.scd.dcs.domains.entities.BoardEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.domains.vos.BoardVo;
import com.scd.dcs.mappers.ArticleMapper;
import com.scd.dcs.mappers.BoardMapper;
import com.scd.dcs.results.CommonResult;
import com.scd.dcs.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class ArticleService {
    private final ArticleMapper articleMapper;
    private final BoardMapper boardMapper;

    @Autowired
    ArticleService(ArticleMapper articleMapper,
                   BoardMapper boardMapper){
        this.articleMapper = articleMapper;
        this.boardMapper = boardMapper;
    }

    public Result write(ArticleEntity article){
        if(article== null){
            return CommonResult.FAILURE;
        }
        article.setCreatedAt(LocalDateTime.now());
        article.setView(0);
        return this.articleMapper.insertArticle(article) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public ArticleEntity getArticle(int index){
        ArticleEntity dbArticle = this.articleMapper.selectArticleByIndex(index);
        return dbArticle;
    }

    public boolean updateArticle(ArticleEntity article){
        article.setView(article.getView()+1);
        return this.articleMapper.updateArticle(article) > 0 ? true : false;
    }

    public Result modify(UserEntity user, ArticleEntity article){
        if(user == null){
            return CommonResult.FAILURE_DENIED;
        }
        ArticleEntity dbArticle = this.articleMapper.selectArticleByIndex(article.getIndex());
        if(dbArticle == null){
            return CommonResult.FAILURE;
        }
        if(!user.getEmail().equals(dbArticle.getUserEmail())){
            return CommonResult.FAILURE_DENIED;
        }
        dbArticle.setContent(article.getContent());
        dbArticle.setTitle(article.getTitle());
        return this.articleMapper.updateArticle(dbArticle) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public Result delete(UserEntity user, ArticleEntity article){
        if(user == null){
            return CommonResult.FAILURE;
        }
        ArticleEntity dbArticle = this.articleMapper.selectArticleByIndex(article.getIndex());
        if(dbArticle == null){
            return CommonResult.FAILURE;
        }
        if(!user.getEmail().equals(dbArticle.getUserEmail())){
            return CommonResult.FAILURE_DENIED;
        }
        article.setBoardCode(dbArticle.getBoardCode());
        return this.articleMapper.deleteArticle(dbArticle) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public ArticleEntity[] getArticlesOf(BoardVo board){
        BoardEntity dbBoard = this.boardMapper.selectBoardByCode(board.getCode());
        if(dbBoard == null){
            return null;
        }
        System.out.println(dbBoard);
        board.setText(dbBoard.getText());
        board.setTotalCount(this.articleMapper.selectArticlesCountByBoardVo(board));
        board.setMinPage(1);

        board.setMaxPage(board.getTotalCount() / board.getCountPerPage() + (board.getTotalCount() % board.getCountPerPage() == 0 ? 0 : 1));
        board.setOffset(board.getCountPerPage() * (board.getRequestPage() - 1));

        System.out.println("요청 받은 페이지 번호 : " + board.getRequestPage());
        System.out.println("전체 게시글 개수 : " + board.getTotalCount());
        System.out.println("이동 가능한 최소 페이지 : " + board.getMinPage());
        System.out.println("이동 가능한 최대 페이지 : " + board.getMaxPage());

        System.out.println("get " + board);

        return this.articleMapper.selectArticlesByBoardVo(board);
    }
}
