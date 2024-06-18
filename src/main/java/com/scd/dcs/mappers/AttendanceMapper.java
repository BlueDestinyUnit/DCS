package com.scd.dcs.mappers;

import com.scd.dcs.domains.entities.AttendanceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AttendanceMapper {
    int insertAttendance(AttendanceEntity attendance);

    AttendanceEntity selectAttendanceByDates(@Param("email") String email,
                                            @Param("firstDate") String firstDate,
                                            @Param("secondDate") String secondDate);

    AttendanceEntity selectAttendanceByUserEmail(@Param("userEmail") String userEmail);
}
