package org.userservice.userservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "users")
public class User extends BaseEntity{

    @Id
    @Column(name = "user_id")
    private String userId;  //oAuth2 provider + providerId

    @Column(name = "email")
    private String email;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "user_name")
    private String userName; //TODO: 필요여부 파악

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "total_score")
    private int totalScore; //사용자 코딩 총 점수

    @Column(name = "register_count")
    private int registerCount; //정식 등록된 문제 수

    @Column(name = "solved_count")
    private int solvedCount; //해결한 문제 수

    @Column(name = "day_of_birth")
    private LocalDate dateOfBirth; //생년월일

    @Enumerated(EnumType.STRING)
    private CodeLanguage codeLanguage;

    @Enumerated(EnumType.STRING)
    private AuthRole role;
}
