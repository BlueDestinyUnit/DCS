package com.scd.dcs.mappers;

import com.scd.dcs.domains.entities.AttendanceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AttendanceMapper {
    int insertAttendance(AttendanceEntity attendance);

    AttendanceEntity selectAttendanceByUserEmail(@Param("userEmail") String userEmail);
}
