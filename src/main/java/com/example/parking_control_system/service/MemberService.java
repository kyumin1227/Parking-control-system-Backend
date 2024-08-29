package com.example.parking_control_system.service;

import com.example.parking_control_system.dto.MemberLoginDto;
import com.example.parking_control_system.dto.MemberRegisterDto;
import com.example.parking_control_system.entity.Member;
import com.example.parking_control_system.entity.Role;
import com.example.parking_control_system.repository.MemberRepository;
import com.example.parking_control_system.repository.RoleRepository;
import com.example.parking_control_system.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    /**
     * 회원 가입시 NULL 값이 있는지 조회
     * @param memberRegisterDto
     * @return
     */
    public boolean checkNull(MemberRegisterDto memberRegisterDto) {
        if (memberRegisterDto.getName().isEmpty()) {
            return false;
        }

        if (memberRegisterDto.getEmail().isEmpty()) {
            return false;
        }

        if (memberRegisterDto.getPassword().isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * 이미 가입된 메일인지 조회
     * @param email
     * @return
     */
    public boolean existEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    /**
     * 유저 저장
     * @param memberRegisterDto
     */
    public void saveMember(MemberRegisterDto memberRegisterDto) {

        Member member = new Member();
        String encodedPassword = passwordEncoder.encode(memberRegisterDto.getPassword());
        Role userRole = roleRepository.findRoleByRoleName("USER");

        member.setMemberName(memberRegisterDto.getName());
        member.setEmail(memberRegisterDto.getEmail());
        member.setPassword(encodedPassword);
        member.addRole(userRole);

        memberRepository.save(member);
    }

    /**
     * 이메일을 통해 유저를 반환
     * @param email
     * @return
     */
    public Member getMemberByEmail(String email) {

        Member memberByEmail = memberRepository.getMemberByEmail(email);

        return memberByEmail;
    }

    public Member login(MemberLoginDto memberLoginDto) {

        if (memberLoginDto.getEmail().isEmpty()) {
            return null;
        }

        if (memberLoginDto.getPassword().isEmpty()) {
            return null;
        }

        Member member = memberRepository.getMemberByEmail(memberLoginDto.getEmail());

        boolean matches = passwordEncoder.matches(memberLoginDto.getPassword(), member.getPassword());

        System.out.println("matches = " + matches);
        if (matches) {
            return member;
        } else {
            return null;
        }

    }


    public String getJwtToken(String email) {

        String secretKey = "my123123-secret-key123123-123123";
        long expireTimeMs = 1000 * 60 * 60;

        String token = JwtTokenUtil.createToken(email, secretKey, expireTimeMs);

        return token;

    }


}
