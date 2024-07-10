package com.scd.dcs.services;


import com.scd.dcs.domains.entities.SubmitImageEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.domains.entities.WorkEntity;
import com.scd.dcs.domains.vos.Progress;
import com.scd.dcs.mappers.WorkMapper;
import com.scd.dcs.results.CommonResult;
import com.scd.dcs.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;

@Service
public class WorkService {
    private final WorkMapper workMapper;


    @Autowired
    public WorkService(WorkMapper workMapper) {
        this.workMapper = workMapper;
    }


    @Transactional
    public String saveImage(UserEntity user, MultipartFile[] images, String date) throws IOException {

        // 작업 엔티티 생성 및 저장
        WorkEntity workEntity = workMapper.findWorkByDateAndUser(LocalDate.parse(date), user.getEmail());
        if (workEntity == null) {
            System.out.println(1);
            System.out.println(date);
            workEntity = new WorkEntity();
            workEntity.setUserEmail(user.getEmail());
            workEntity.setDate(LocalDate.parse(date));
            workMapper.insertWork(workEntity);
        }

        // 이미지 엔티티 생성 및 작업 엔티티와 매핑하여 저장
        for (MultipartFile image : images) {
            SubmitImageEntity submitImageEntity = new SubmitImageEntity();
            submitImageEntity.setWorkIndex(workEntity.getIndex());
            submitImageEntity.setContentType(image.getContentType());
            submitImageEntity.setImageData(image.getBytes());
            submitImageEntity.setOriginalName(image.getOriginalFilename());
            workMapper.insertSubmitImage(submitImageEntity);
        }


        return "{\"result\": \"success\"}";
    }


    public SubmitImageEntity[] imageList(String email, String date) {
        return this.workMapper.selectSubmitImages(email, date);
    }

    public SubmitImageEntity getImage(int index) {
//        if (index < 1) return null;
        return this.workMapper.selectSubmitImage(index);
    }

    public Progress countSubmitImage() {
        return this.workMapper.countSubmitImage();
    }

    public Progress countSubmitImage(String date) {
        return this.workMapper.countSubmitImageOfYear(date);
    }

    public double averageSubmitImage(String date) {
        Progress[] progressList = this.workMapper.countSubmitImageOfDayList(date);
        if (progressList.length == 0) {
            return 0;
        } else {
            int sum = 0;
            for (int i = 0; i < progressList.length; i++) {
                Progress progress = progressList[i];
                sum += progress.getCount();
            }
            System.out.println("sum : " + sum);
            System.out.println("len : " + progressList.length);
            System.out.println(sum / progressList.length);
            double number = Math.round((double)sum / (double)progressList.length * 100) / 100.0;
            System.out.println(number);
            return number;
        }
    }

    public Progress[] countSubmitImageOfDay(String date, UserEntity user) {
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        YearMonth yearMonth = YearMonth.of(year, month);
        int lastDayOfMonth = yearMonth.lengthOfMonth();
        Progress[] progressList = new Progress[lastDayOfMonth];

        // 각 날짜별로 처리
        for (int day = 1; day <= lastDayOfMonth; day++) {
            // 날짜 포맷을 "yyyy-MM-dd"로 지정하여 문자열 생성
            String dateString = String.format("%04d-%02d-%02d", year, month, day);
            // 해당 날짜로 작업 처리
            progressList[day - 1] = this.workMapper.countSubmitImageOfDay(dateString, user.getEmail());
            System.out.println(progressList[day - 1].getSignCount());
        }
        return progressList;
    }

    @Transactional
    public Result<?> updateImage(SubmitImageEntity submitImageEntity) {
        return workMapper.updateImage(submitImageEntity) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }


    @Transactional
    public Result<?> delete(int[] indexArray) {
        try {
            for (int index : indexArray) {
                this.workMapper.deleteIndex(index);
            }
        } catch (Exception e) {
            return CommonResult.FAILURE;
        }
        return CommonResult.SUCCESS;
    }
}
