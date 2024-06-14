package com.scd.dcs.mappers;


import com.scd.dcs.domains.entities.SubmitImageEntity;
import com.scd.dcs.domains.entities.WorkEntity;
import com.scd.dcs.results.Result;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface WorkMapper{

    int insertSubmitImage(SubmitImageEntity submitImageEntity);

    int insertWork(WorkEntity workEntity);

    int countNonMosaicImages(@Param("email") String email, @Param("date") String date);

    int mosaicImages(@Param("date") String date);


    SubmitImageEntity selectSubmitImage(@Param("index")int index);

    SubmitImageEntity[] selectSubmitImages(@Param("email")String email, @Param("date") String date);

    WorkEntity findWorkByDateAndUser(@Param("date") LocalDate date, @Param("userEmail") String userEmail);

    int updateImage(SubmitImageEntity submitImageEntity);


}
