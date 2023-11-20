package com.dgu.wantToGraduate.domain.matching.service;

import com.dgu.wantToGraduate.domain.brand.entity.Brand;
import com.dgu.wantToGraduate.domain.brand.repository.BrandRepository;
import com.dgu.wantToGraduate.domain.matching.dto.MatchingDto;
import com.dgu.wantToGraduate.domain.matching.dto.MatchingDto.RequestDto;
import com.dgu.wantToGraduate.domain.matching.dto.MatchingDto.RequestDto.SelectDto;
import com.dgu.wantToGraduate.domain.matching.entity.PreferBrand;
import com.dgu.wantToGraduate.domain.matching.repository.BrandQueue;
import com.dgu.wantToGraduate.domain.matching.repository.PreferBrandRepository;
import com.dgu.wantToGraduate.domain.user.entity.User;
import com.dgu.wantToGraduate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Select;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingService {

    private final BrandQueue brandQueue;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final PreferBrandRepository preferBrandRepository;

    public void matching(RequestDto selectInfo){
        log.info("[MatchingService] 진입");
        // PreferBrand saving 작업 필요 (; insertIntoBrandQueue()에서 정렬 기준으로써 저장된 데이터 사용)
        User userEntity = userRepository.findById(selectInfo.getUserId()).orElseThrow(()
                -> new IllegalArgumentException("[예외발생]존재하지 않는 유저입니다."));
        log.info("[MatchingService] userEntity: {}", userEntity);



        Map<Integer,Brand> brandList = new HashMap();
        /*매장 대기열 생성*/
        for(SelectDto selectDto : selectInfo.getPreferBrandList()){
            //TODO: 3개의 선호 정보 모두 저장하고 매칭이 시작되어야해. 이것 때문에 postman에서 요청을 3번째로 해야 정상으로api가200을 리턴함.
            log.info("[MatchingService] selectDto: {}", selectDto);
            PreferBrand build = PreferBrand.builder()
                    .user(userEntity)
                    .brand(brandRepository.findByBrandName(selectDto.getBrandName()).get())
                    .priority(selectDto.getPriority())
                    .build();
            log.info("[MatchingService] build: {}", build);
            preferBrandRepository.save(build);
        }
        for(SelectDto selectDto : selectInfo.getPreferBrandList()){
            generateBrandQueue(selectDto); // 매장 큐 생성
            log.info("[MatchingService] brandQueue: {}", brandQueue);
            insertIntoBrandQueue(selectDto,selectInfo.getUserId()); // 매장 큐에 유저 삽입
            log.info("[MatchingService] brandQueue: {}", brandQueue);
        }
    }
//    //TODO: 네이밍 다시 해야할듯?
//    public Map<Integer,Brand>  of(SelectDto selectDto, Map<Integer,Brand> brandList){
//
//        Brand brand = brandRepository.findByBrandName(selectDto.getBrandName()).get();
//        int priority = selectDto.getPriority();
//
//        brandList.put(priority,brand);
//
//        return brandList;
//    }

    private void insertIntoBrandQueue(SelectDto selectDto, Long userId) {
        log.info("[MatchingService] insertIntoBrandQueue 진입");
        Long brandId = brandRepository.findBrandIdByBrandName(selectDto.getBrandName())
                .orElseThrow(() -> new IllegalArgumentException("[예외발생]해당 브랜드가 없습니다."));
        brandQueue.addUser(userId, brandId);
    }

    private void generateBrandQueue(SelectDto brand){
        log.info("[MatchingService] generateBrandQueue 진입");
        Optional<Brand> brandEntity = brandRepository.findByBrandName(brand.getBrandName());
        log.info("[MatchingService] brandEntity: {}", brandEntity);
        if(brandEntity.isPresent()){
            brandQueue.addBrand(brandEntity.get()); //처음 선택된 매장은 대기열을 생성합니다. (이미 대기열이 있는 매장은 생성되지 않습니다.)
        }else {
            throw new IllegalArgumentException("[예외발생]해당 브랜드가 없습니다.");
        }
    }
}
