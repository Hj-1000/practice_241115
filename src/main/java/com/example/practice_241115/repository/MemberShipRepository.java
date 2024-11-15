package com.example.practice_241115.repository;

import com.example.practice_241115.entity.MemberShip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberShipRepository extends JpaRepository<MemberShip, Long> {
    //jparepository에는 findbyid가 있다.
    //하지만 session 값에 email을 등록하려면 email 값이 필요
    // email은 pk를 대체하여 사용하기 위해
    // 유니크 + notnull임

    public MemberShip findByEmail(String email);
    //select * from MemberShip where name : email


}
