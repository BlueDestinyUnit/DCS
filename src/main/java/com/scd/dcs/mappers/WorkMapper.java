package com.scd.dcs.mappers;


import com.scd.dcs.domains.entities.ImageCommentEntity;
import com.scd.dcs.domains.entities.SubmitImageEntity;
import com.scd.dcs.domains.entities.WorkEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface WorkMapper {
    void insertImageComment(ImageCommentEntity imageCommentEntity);

    void insertSubmitImage(SubmitImageEntity submitImageEntity);

    int insertWork(WorkEntity workEntity);

    WorkEntity selectWork(@Param("index")int index);

    SubmitImageEntity selectSubmitImage(@Param("index")int index);

    SubmitImageEntity[] selectSubmitImages(@Param("email")String email, @Param("workDate") String workDate);

    WorkEntity findWorkByDateAndUser(@Param("workDate") LocalDate workDate, @Param("userEmail") String userEmail);

    List<SubmitImageEntity> selectAllImages(@Param("index")int index);

}
