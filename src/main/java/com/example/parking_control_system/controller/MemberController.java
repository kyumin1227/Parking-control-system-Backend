package com.example.parking_control_system.controller;

import com.example.parking_control_system.dto.MemberLoginDto;
import com.example.parking_control_system.dto.MemberRegisterDto;
import com.example.parking_control_system.entity.Member;
import com.example.parking_control_system.response.ApiResponse;
import com.example.parking_control_system.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody MemberRegisterDto registerDto) {

        boolean checkNull = memberService.checkNull(registerDto);

        if (!checkNull) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(1, "빈 값이 있습니다.", null));
        }

        boolean existEmail = memberService.existEmail(registerDto.getEmail());

//        이미 가입된 이메일인 경우
        if (existEmail) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(1, "이미 가입된 이메일입니다.", null));
        }

        memberService.saveMember(registerDto);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(0, "회원가입이 완료되었습니다.", null));

    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody MemberLoginDto memberLoginDto) {

        Member loginMember = memberService.login(memberLoginDto);

        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(1, "이메일 또는 비밀번호가 틀렸습니다.", null));
        }

        String jwtToken = memberService.getJwtToken(loginMember.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(0, "로그인 되었습니다", jwtToken));
    }
}
