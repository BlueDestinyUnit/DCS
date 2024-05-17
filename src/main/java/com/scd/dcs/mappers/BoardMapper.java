package com.scd.dcs.mappers;


import com.scd.dcs.domains.entities.BoardEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BoardMapper {
    BoardEntity[] selectBoards();

    BoardEntity selectBoardByCode(@Param("code") String code);
}
