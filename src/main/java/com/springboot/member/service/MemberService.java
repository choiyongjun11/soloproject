package com.springboot.member.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.mapper.MemberMapper;
import com.springboot.member.repository.MemberRepository;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {
    //create,  find, update , delete
    //entity 처리

//
//    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, AuthorityUtils authorityUtils) {
//        this.memberRepository = memberRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.authorityUtils = authorityUtils;
//    }


    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    private final MemberRepository memberRepository;
    //private final PasswordEncoder passwordEncoder; //spring security jwt 토큰 인증을 위해
    //private final AuthorityUtils authorityUtils; //spring security jwt 토큰 인증을 위해


    public Member createMember(Member member) {
        verifyExistsEmail(member.getEmail());
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    public Member findMember(long memberId) {
        return findVerifiedMember(memberId);
    }

    public Member updateMember(Member member) {

        Member findMember = findVerifiedMember(member.getMemberId());
        Optional.ofNullable(member.getName())
                .ifPresent(name -> findMember.setName(name));
        Optional.ofNullable(member.getPhone())
                .ifPresent(phone -> findMember.setPhone(phone));

        return memberRepository.save(findMember);
    }

    public void deleteMember(long memberId) {
        Member findMember = findVerifiedMember(memberId);
        memberRepository.delete(findMember);

    }

    //검증
    public Member findVerifiedMember(long memberId) {
        Optional<Member> optionalMember =
                memberRepository.findById(memberId);

        Member findMember =
                optionalMember.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.NOT_FOUND));
        return findMember;
    }

    //password null 인지 검증 기능
    public void saveMember(Member member) {
        if (member.getPassword() == null || member.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty!");
        }
        memberRepository.save(member);
    }

    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent())
            throw new BusinessLogicException(ExceptionCode.NOT_FOUND);
    }


}
