package com.scd.dcs.services;


import com.scd.dcs.domains.entities.BoardEntity;
import com.scd.dcs.mappers.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    private BoardMapper boardMapper;

    @Autowired
    BoardService(BoardMapper boardMapper){
        this.boardMapper = boardMapper;
    }

    public BoardEntity getBoard(String code){
        return this.boardMapper.selectBoardByCode(code);
    }

    public BoardEntity[] getBoards(){
        return this.boardMapper.selectBoards();
    }
}
