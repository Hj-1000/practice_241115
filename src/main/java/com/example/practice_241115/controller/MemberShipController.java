package com.example.practice_241115.controller;

import com.example.practice_241115.dto.MemberShipDTO;
import com.example.practice_241115.service.MemberShipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/user")
public class MemberShipController {
    private final MemberShipService memberShipService;

    // 회원가입 겟
    @GetMapping("/signup")
    public String signup(MemberShipDTO memberShipDTO){
        // 파라미터는  유효성 검사를 하면 다시 보내줄 것이기 때문에
        // dto 타입을 우선 가지고 있기로 함

        return "user/signup";
    }

    // 회원가입 포스트
    // 유효성 검사를 위해 valid 어노테이션을 추가
    // *주의* valid의 bindingresult는 attribute 타입들 보다 앞에 써줘야 오류가 안남
    @PostMapping("/signup")
    public String signup(@Valid MemberShipDTO memberShipDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){
        log.info("컨트롤러로 들어온 가입 정보 : " + memberShipDTO);

        if (bindingResult.hasErrors()){
            log.info("컨트롤러_유효성검사로 들어온 유저 정보 : " +memberShipDTO);
            // 오류가 있다면 회원가입 창으로 다시 보낸다
            return "user/signup";
        }

        try {
            // 서비스의 signup메서드로 들어온 memberShipDTO를
            // 새로 담아준다
            memberShipDTO =
            memberShipService.signUp(memberShipDTO);
        }catch (IllegalStateException e){
            // 동일한 이메일이 있다면 해당 오류 메시지를 반환한다.
            log.info("동일한 이메일이 있습니다.");
            log.info("동일한 이메일이 있습니다.");
            log.info("동일한 이메일이 있습니다.");
            log.info("동일한 이메일이 있습니다.");
            // 유저가 해당하는 오류를 발생시키는 행위를 했을 때
            // 홈페이지 화면에서 띄워주기 위해 html로
            // 해당 값을 직접 찍어주기 위해 model을 사용함
            model.addAttribute("msg", e.getMessage());
            // 역시 오류이기 때문에 해당 창으로 다시 돌려 보낸다.
            return "user/signup";
        }
        // html에서 p구문으로 작성란 오류를 바로 보여줄 것임
        // 리다이렉트로 넘어가기 때문에 redirectAttributes 를 사용함
        // addflash는 임시저장임(한번)
        redirectAttributes.addFlashAttribute("memberShipDTO",memberShipDTO);

        // 위 예외를 모두 피하고 정상적으로 회원가입을 하였다면
        // 유저 편의성을 위해 로그인 화면으로 이동 시키는 것이 적합하다고 생각함


//        return "redirect:/user/signup";
        return "redirect:/user/login";
    }
    // 로그인
    // Post 로그인은 시큐리티에서 처리할 것이기 때문에
    // 컨트롤러에서 만들지 않는다.
    @GetMapping("/login")
    public String login(MemberShipDTO memberShipDTO, Principal principal){
        if (principal != null){ // principal 로그인이 되었을 때 값을 가지게 된다.
                                // 현재는 email을 username 으로 가졌기 때문에
                                // 로그인한 email을 가지고 있음.

            log.info("==============================");
            log.info("||" + principal.getName()+ "||");
            log.info("||" + principal.getName()+ "||");
            log.info("||" + principal.getName()+ "||");
            log.info("||" + principal.getName()+ "||");
            log.info("||" + principal.getName()+ "||");
            log.info("||" + principal.getName()+ "||");
            log.info("||" + principal.getName()+ "||");
            log.info("||" + principal.getName()+ "||");
            log.info("==============================");
            // 로그인에 성공하였으므로
            // 유저를 게시판 목록으로 이동시킨다.
            // *아직 리스트 화면을 구현하지 않아서 이동시 오류*
//            return "redirect:/board/list";

        }

        return "user/login";
    }


}
