package com.scd.dcs.services;


import com.scd.dcs.domains.entities.SubmitImageEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.domains.entities.WorkEntity;
import com.scd.dcs.mappers.WorkMapper;
import com.scd.dcs.results.CommonResult;
import com.scd.dcs.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class WorkService {
    private final WorkMapper workMapper;



    @Autowired
    public WorkService(WorkMapper workMapper) {
        this.workMapper = workMapper;
    }



    @Transactional
    public String saveImage(UserEntity user, MultipartFile[] images,String date) throws IOException {

        // 작업 엔티티 생성 및 저장
        WorkEntity workEntity = workMapper.findWorkByDateAndUser(LocalDate.parse(date), user.getEmail());
        if (workEntity == null) {
            workEntity = new WorkEntity();
            workEntity.setUserEmail(user.getEmail());
            workEntity.setWorkDate(LocalDate.parse(date));
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
        // 이미지 서비스 호출
//        imageService.processImages(images);

        // 성공 결과 반환
        return "{\"result\": \"success\"}";
    } // 24-05-29 수정


    public SubmitImageEntity[] imageList(String email, String date) {
        return this.workMapper.selectSubmitImages(email,date);
    }

    public SubmitImageEntity getImage(int index) {
//        if (index < 1) return null;
        return this.workMapper.selectSubmitImage(index);
    }

    @Transactional
    public Result<?> updateImage(SubmitImageEntity submitImageEntity) {
        return workMapper.updateImage(submitImageEntity) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public Result<?> postComplete(SubmitImageEntity submitImageEntity) {
        return workMapper.insertSubmitImage(submitImageEntity) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
//        return null;
    }


}
