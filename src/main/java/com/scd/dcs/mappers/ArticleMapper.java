package com.scd.dcs.mappers;

import com.scd.dcs.controllers.BoardController;
import com.scd.dcs.domains.entities.ArticleEntity;

import com.scd.dcs.domains.vos.BoardVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleMapper {
    int insertArticle(ArticleEntity article);

    int updateArticle(ArticleEntity article);
    ArticleEntity selectArticleByIndex(@Param(value = "index") int index);

    ArticleEntity[] selectArticlesByBoardVo(BoardVo boardVO);

    int selectArticlesCountByBoardVo(BoardVo board);

    int deleteArticle(ArticleEntity article);
}
