package com.example.practice_241115.dto;

import com.example.practice_241115.constant.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberShipDTO {

    private Long num;

    //빈값허용안함 최소 2~10글자
    //빈공간을 포함한 빈칸 or 범위를 벗어가면 메시지
    @NotBlank(message = "이름을 꼭 쓰셔야 합니다.")
    @Size(min = 2, max = 10, message = "이름을 2~10글자로 작성하세요 ex)홍길동")
    private String name;

    @NotBlank(message = "이메일을 꼭 입력해주세요")
    @Size(max = 50, message = "최대 50글자 입니다.")
    @Email(message = "이메일 형식에 맞춰서 작성하시오")
    private String email;

    @NotBlank(message = "비밀번호는 꼭 작성해야 합니다.")
    @Size(min = 10, max = 16, message = " 비밀번호는 10~16글자 사이로 입력해주세요")
    private String password;

    @NotBlank(message = "주소는 꼭 작성해야 합니다.")
    private String address;

    //권한

    private Role role;
}
