package com.dgu.wantToGraduate.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.dgu.wantToGraduate.domain.BaseTimeEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserReview extends BaseTimeEntity {

    @Id
    @Column(name= "userReview_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "User_Id")
    private User user;

    @Column
    private String comment;

    @Column
    private float grade;

}
