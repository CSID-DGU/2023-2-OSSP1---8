package com.dgu.wantToGraduate.domain.Q_matching.repository;

import com.dgu.wantToGraduate.domain.Q_matching.entity.PreferBrand;
import com.dgu.wantToGraduate.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PreferBrandRepository extends JpaRepository<PreferBrand, Long>{
    PreferBrand findByUser(User user);

    PreferBrand findByUserAndBrandId(User user, Long brandId);

    @Query("select p from PreferBrand p where p.user.nickname = ?1")
    List<PreferBrand> findByUserNickname(String nickname);
}
