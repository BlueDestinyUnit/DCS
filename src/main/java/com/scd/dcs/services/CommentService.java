package com.scd.dcs.services;

import com.scd.dcs.domains.entities.CommentEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.mappers.CommentMapper;
import com.scd.dcs.results.CommonResult;
import com.scd.dcs.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    CommentMapper commentMapper;

    @Autowired
    CommentService(CommentMapper commentMapper){
        this.commentMapper = commentMapper;
    }

    public Result write(CommentEntity comment){
        return this.commentMapper.insertComment(comment) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public List<CommentEntity> getComments(int articleIndex){
        return this.commentMapper.selectCommentsByArticleIndex(articleIndex);
    }

    public Result deleteComment(int index, UserEntity user){
        if(user == null){
            return CommonResult.FAILURE_DENIED;
        }
        CommentEntity dbComment = commentMapper.selectCommentsByIndex(index);
        if(dbComment == null){
            return CommonResult.FAILURE;
        }
        if(!user.getEmail().equals(dbComment.getUserEmail())){
            return CommonResult.FAILURE_DENIED;
        }
        return this.commentMapper.deleteCommentByIndex(index) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public Result modifyComment(UserEntity user,int index, String newComment){
        if(user == null){
            return CommonResult.FAILURE_DENIED;
        }
        CommentEntity dbComment = this.commentMapper.selectCommentsByIndex(index);
        if(dbComment == null){
            return CommonResult.FAILURE;
        }
        if(!user.getEmail().equals(dbComment.getUserEmail())){
            return CommonResult.FAILURE_DENIED;
        }
        dbComment.setContent(newComment);
        return this.commentMapper.updateComment(dbComment) > 0  ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

}
