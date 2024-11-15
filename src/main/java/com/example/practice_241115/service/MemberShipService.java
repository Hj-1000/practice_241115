package com.example.practice_241115.service;

import com.example.practice_241115.constant.Role;
import com.example.practice_241115.dto.MemberShipDTO;
import com.example.practice_241115.entity.MemberShip;
import com.example.practice_241115.repository.MemberShipRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MemberShipService implements UserDetailsService {

    private final MemberShipRepository memberShipRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    // 회원가입 : 컨트롤러에서 dto를 입력받아 entity로 변환하여 repository의
    // save를 이용
    // 반환값은 dto 전체를 반환할것임
    // 만약 회원이 이미 가입되어 있다면
    // 그것에 대한 예외처리를 해아함

    public MemberShipDTO signUp(MemberShipDTO memberShipDTO){

        // 기존 글 등록과 다르게 회원가입은
        // 우선 회원으로 등록되어있는지 확인하는 절차가 우선적으로 필요
        // 리포지토리 쿼리문으로 만든 findbyEmail을 사용해서
        // 찾아온다
        MemberShip memberShip =
        memberShipRepository.findByEmail(memberShipDTO.getEmail());

        //가져온 이메일이 이미 있는 회원인지 확인한다

        if (memberShip != null){ // 빈값이 아니라면 = 즉 기존 회원이라면
            // IllegalStateException :
            // 대상 객체의 상태가 호출된 메서드를 수행하기에
            // 적절하지 않을 때 발생시킬 수 있는 예외
            throw new IllegalStateException("이미 가입된 회원입니다");

        }
        // 해당 예외처리에 걸리지 않았다면 새로운 회원임
        // 컨트롤러로 들어온 DTO타입을 저장하기 전 엔티티로 변환한다
        memberShip =
        modelMapper.map(memberShipDTO, MemberShip.class);

        // 일반유저
        memberShip.setRole(Role.ADMIN);

        // 비밀번호를 암호화해서 저장한다.(저장할 때 이상한 문자로 전환되서 저장됨 데이터베이스에서도 볼수없음)
        memberShip.setPassword(passwordEncoder.encode(memberShipDTO.getPassword()));

        //리포지토리에 저장한다
        memberShip =
                memberShipRepository.save(memberShip);

        //저장한 값을 컨트롤러에서 반환하기 위해
        // 리턴 타입은 dto로 전환한다.

        return modelMapper.map(memberShip, MemberShipDTO.class);
    }
    // 로그인
    // UserDetailsService를 구현해서 사용한다.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 사용자가 입력한 email을 db에서 검색한다.
        // email과 password를 포함한 entity를 받는다.
        // 로그인은 기본적으로 email 과 password를 찾거나
        // email로 검색하여 가져온 데이터가 !null 이면
        // 그 안에 password를 가지고
        // 비교하여 맞다면 로그인한다.
        log.info("유저디테일 서비스로 들어온 이메일 : " + email);
        MemberShip memberShip =
                this.memberShipRepository.findByEmail(email);
        // 이메일로 검색해서 가져온 값이 없다면
        // try catch 문으로 다른 화면으로 달린 메시지를 가지고
        // 로그인창으로 보내면
        // 컨트롤러 창에서 알아서 처리하게됨

        if(memberShip == null){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        // 예외처리가 되지 않았다면
        log.info("현재 찾은 회원정보 " +memberShip);

        // 권한 처리
        String role = "";
        if ("ADMIN".equals(memberShip.getRole().name())){
            log.info("관리자");
            role = Role.ADMIN.name();
        }else {
            log.info("일반유저");
            role = Role.USER.name();
        }

        return User.builder()
                .username(memberShip.getEmail())
                .password(memberShip.getPassword())
                .roles(role)
                .build();
    }



}
