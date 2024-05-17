package com.scd.dcs.mappers;


import com.scd.dcs.domains.entities.CommentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    int insertComment(CommentEntity comment);

    List<CommentEntity> selectCommentsByArticleIndex(@Param("articleIndex") int articleIndex);

    CommentEntity selectCommentsByIndex(@Param("index") int index);

    int deleteCommentByIndex(@Param("index") int index);

    int updateComment(CommentEntity comment);
}
