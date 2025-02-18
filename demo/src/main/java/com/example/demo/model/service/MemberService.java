package com.example.demo.model.service;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.model.domain.Member;
import lombok.*;
import com.example.demo.model.repository.Member_Repository;
import com.example.demo.model.service.AddMemberRequest;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // 트랜잭션 처리(클래스 내 모든 메소드 대상)
@RequiredArgsConstructor

public class MemberService {
    private final Member_Repository memberRepository;
    private final PasswordEncoder passwordEncoder; // 스프링 버전 5 이후, 단방향 해싱 알고리즘 지원

    private void validateDuplicateMember(AddMemberRequest request){
        Member findMember = memberRepository.findByEmail(request.getEmail()); // 이메일 존재 유무
    if(findMember != null){
        throw new IllegalStateException("이미 가입된 회원입니다."); // 예외처리
    }
}

public Member saveMember(AddMemberRequest request){
    validateDuplicateMember(request); // 이메일 체크
    
    String encodedPassword = passwordEncoder.encode(request.getPassword());
    request.setPassword(encodedPassword); // 암호화된 비밀번호 설정
    return memberRepository.save(request.toEntity());
    }

public Member loginCheck(String email, String rawPassword) {
    Member member= memberRepository.findByEmail(email); // 이메일조회
    if (member == null) {
        throw new IllegalArgumentException("등록되지않은이메일입니다.");
    }
    if (!passwordEncoder.matches(rawPassword, member.getPassword())) { // 비밀번호확인
        throw new IllegalArgumentException("비밀번호가일치하지않습니다.");
    }
    return member; // 인증성공시회원객체반환
    }
}
