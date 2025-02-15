package com.springboot.board.service;

import com.springboot.auth.utils.AuthorityUtils;
import com.springboot.board.dto.BoardDto;
import com.springboot.board.entity.Board;
import com.springboot.board.repository.BoardRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//create, find, update, delete

@Slf4j
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final AuthorityUtils authorityUtils;

    public BoardService(BoardRepository boardRepository, MemberService memberService, AuthorityUtils authorityUtils) {
        this.boardRepository = boardRepository;
        this.memberService = memberService;
        this.authorityUtils = authorityUtils;
    }

    public Board createBoard(BoardDto.Post postDto, long memberId) {
        Member member = memberService.findVerifiedMember(memberId);

        // AuthorityUtils 에서 이메일을 기반으로 role 가져옴
        List<String> roles = authorityUtils.createRoles(member.getEmail());

        // ADMIN 권한이 있는 경우 예외 처리, USER 권한만 게시글을 작성 할 수 있음
        if (roles.contains("ADMIN")) {
            throw new BusinessLogicException(ExceptionCode.FORBIDDEN);
        }

        // 사용자가 질문 제목과 내용 필수 입력하였는지 확인
        if (postDto.getTitle() == null || postDto.getTitle().trim().isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.TITLE_REQUIRED);
        }

        if (postDto.getContent() == null || postDto.getContent().trim().isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.CONTENT_REQUIRED);
        }

        // 게시글 생성 및 저장
        Board board = Board.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .secret(postDto.isSecret())
                .questionStatus(Board.QuestionStatus.QUESTION_REGISTERED)
                .createdAt(LocalDateTime.now())
                .member(member)
                .build();

        return boardRepository.save(board);


    }



    public Board findBoard(long boardId, long memberId, boolean isAdmin) {
        Board board = findVerifiedBoard(boardId);

        return board;

    }





    private Board findVerifiedBoard(long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NOT_FOUND));
    }




}
