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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


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

        /*
        - **질문은 회원(고객)만 등록할 수 있다.**
        - 질문 등록시 등록 날짜가 생성 되어야 한다. (day)
        - 질문은 질문의 상태 값이 필요하다.
        - QUESTION_REGISTERED - 질문 등록 상태
        - QUESTION_ANSWERED - 답변 완료 상태
        - QUESTION_DELETED - 질문 삭제 상태
        - QUESTION_DEACTIVED - 질문 비활성화 상태: 회원 탈퇴 시, 질문 비활성화 상태
        - **질문 등록 시, 초기 상태 값은 QUESTION_REGISTERED 이어야 합니다.**
        - 질문 제목과 내용은 필수입력 사항이다.
        - 질문은 비밀글과 공개글 둘 중에 하나로 설정되어야 한다.
        - public (공개글 상태)   OR   secret (비밀글 상태)
         */

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

    @Transactional
    public Board findBoard(long boardId, long memberId ) {
        Board board = findVerifiedBoard(boardId);

        /*
        - 1건의 특정 질문은 회원(고객)과 관리자 모두 조회할 수 있습니다. (admin, user) 조회 권한
        - 비밀글 상태인 질문은 질문을 등록한 회원(고객)과 관리자만 조회할 수 있습니다.
        - 1건의 질문 조회 시, 해당 질문에 대한 답변이 존재한다면 답변도 함께 조회되어야 합니다.
        - 이미 삭제 상태인 질문은 조회할 수 없다 (1)
         */

        //질문 삭제 상태인지 확인, 삭제 상태이면 not_found 에러 보내기
        if (board.getQuestionStatus() == Board.QuestionStatus.QUESTION_DELETED) {
            throw new BusinessLogicException(ExceptionCode.NOT_FOUND);
        }

        //비밀글에 대해서 작성한 user, admin 만 조회하도록 하기 -> authorityUtils의 권한(role)을 이용해야함
        Member member = memberService.findVerifiedMember(memberId);
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        boolean isAdmin = roles.contains("ADMIN");

        if (board.isSecret() && !isAdmin && board.getMember().getMemberId() != memberId) {
            throw new BusinessLogicException(ExceptionCode.FORBIDDEN);
        }

        return board;

    }

    @Transactional
    public Page<Board> findBoards(Long memberId, String sortBy, int page, int size) {

        /*
        **여러 건의 질문 조회 - 회원(고객)이 등록한 여러 건의 질문을 조회하는 기능 (Read - SelectAll)**
        - 1. 여러 건의 질문 목록은 회원(고객)과 관리자 모두 조회할 수 있다.
        - 2. 삭제 상태가 아닌 질문만 조회할 수 있다.
        - 3. 여러 건의 질문 목록에서 각각의 질문에 답변이 존재한다면 답변도 함께 조회할 수 있어야 한다.
        - 4. 여러 건의 질문 목록은 페이지네이션 처리가 되어 일정 건수 만큼의 데이터만 조회할 수 있어야 한다.
        - 5. 여러 건의 질문 목록은 아래의 조건으로 정렬해서 조회할 수 있어야 한다.
            ㄴ 최신글 순으로
            ㄴ 오래된 글 순으로
            ㄴ 좋아요가 많은 순으로(좋아요 구현 이후 적용)
            ㄴ 좋아요가 적은 순으로(좋아요 구현 이후 적용)
            ㄴ 조회수가 많은 순으로(조회수 구현 이후 적용)
            ㄴ 조회수가 적은 순으로(조회수 구현 이후 적용)
         */

        // 삭제된 질문을 제외
        Board.QuestionStatus deletedStatus = Board.QuestionStatus.QUESTION_DELETED;

        // Pageable 객체 생성 (페이지 번호, 페이지 크기, 정렬 기준)
        Pageable pageable = PageRequest.of(page, size, getSortBy(sortBy));

        // 회원(고객) 또는 관리자 여부 확인
        List<String> roles = authorityUtils.createRoles(memberService.findVerifiedMember(memberId).getEmail());
        boolean isAdmin = roles.contains("ADMIN");

        // 관리자: 모든 질문 조회, 일반 회원: 본인의 질문만 조회
        if (isAdmin) {
            return boardRepository.findAllByQuestionStatusNot(deletedStatus, pageable);
        } else {
            return boardRepository.findAllByMember_MemberIdAndQuestionStatusNot(memberId, deletedStatus, pageable);
        }
    }

    // 정렬 기준을 받는 메서드
    private Sort getSortBy(String sortBy) {
        switch (sortBy) {
            case "latest":
                return Sort.by(Sort.Order.desc("createdAt")); // 최신글 순
            case "oldest":
                return Sort.by(Sort.Order.asc("createdAt")); // 오래된 글 순
            case "likesDesc":
                return Sort.by(Sort.Order.desc("likeCount")); // 좋아요 많은 순
            case "likesAsc":
                return Sort.by(Sort.Order.asc("likeCount")); // 좋아요 적은 순
            case "viewCountDesc":
                return Sort.by(Sort.Order.desc("viewCount")); // 조회수 많은 순
            case "viewCountAsc":
                return Sort.by(Sort.Order.asc("viewCount")); // 조회수 적은 순
            default:
                return Sort.by(Sort.Order.desc("createdAt")); // 기본적으로 최신글 순
        }
    }


    @Transactional
    public Board updateBoard(long boardId, long memberId, BoardDto.Patch patchDto){

   /*
    ** 질문 수정 - 회원(고객)이 등록한 질문을 수정하는 기능 (Update) **
    - 등록된 질문의 제목과 내용은 질문을 등록한 회원(고객)만 수정할 수 있어야 한다.
    - 회원이 등록한 질문을 비밀글로 변경할 경우, QUESTION_SECRET 상태로 수정되어야 한다.
    - 질문 상태 중에서 QUESTION_ANSWERED 로의 변경은 관리자만 가능하다.
    - 회원이 등록한 질문을 회원이 삭제할 경우, QUESTION_DELETED 상태로 수정되어야 한다.
    - 답변 완료된 질문은 수정할 수 없다
     */

        // 1. 수정할 질문 찾기
        Board board = findVerifiedBoard(boardId);

        // 2. 답변 완료된 질문은 수정 불가
        if (board.getQuestionStatus() == Board.QuestionStatus.QUESTION_ANSWERED) {
            throw new BusinessLogicException(ExceptionCode.NOT_FOUND);
        }

        // 3. 질문을 등록한 회원인지 확인
        Member member = memberService.findVerifiedMember(memberId);
        if (board.getMember().getMemberId() != memberId) {
            throw new BusinessLogicException(ExceptionCode.FORBIDDEN);
        }

        // 4. 제목과 내용 업데이트
        if (patchDto.getTitle() != null) {
            board.setTitle(patchDto.getTitle());
        }
        if (patchDto.getContent() != null) {
            board.setContent(patchDto.getContent());
        }

        // 5. 비밀글로 변경할 경우 상태 업데이트
        if (patchDto.isSecret()) {
            board.setSecret(true);
            board.setQuestionStatus(Board.QuestionStatus.QUESTION_SECRET);
        } else {
            board.setSecret(false);
            board.setQuestionStatus(Board.QuestionStatus.QUESTION_PUBLIC); // 공개글 상태로 변경
        }

        return boardRepository.save(board);


    }

    @Transactional
    public void deleteBoard(long boardId, long memberId) {
    /*
        **1건의 질문 삭제 - 회원(고객)이 등록한 1건의 질문을 삭제하는 기능 (Delete)**
        요구 사항(제한 사항)
        - 1건의 질문은 회원(고객)만 삭제할 수 있다.
        - 1건의 질문 삭제는 질문을 등록한 회원만 가능하다.
        - 질문 삭제 시, 테이블에서 row 자체가 삭제되는 것이 아니라 질문 상태 값이(QUESTION_DELETE)으로 변경되어야 한다.
        - 이미 삭제 상태인 질문은 삭제할 수 없다.
     */

        // 1. 삭제할 질문 찾기
        Board board = findVerifiedBoard(boardId);

        // 2. 이미 삭제된 질문인지 확인
        if (board.getQuestionStatus() == Board.QuestionStatus.QUESTION_DELETED) {
            throw new BusinessLogicException(ExceptionCode.NOT_FOUND);
        }

        // 3. 본인이 작성한 질문인지 확인
        if (board.getMember().getMemberId() != memberId) {
            throw new BusinessLogicException(ExceptionCode.FORBIDDEN);
        }

        // 4. 상태를 `QUESTION_DELETED`로 변경
        board.setQuestionStatus(Board.QuestionStatus.QUESTION_DELETED);

        boardRepository.save(board);


    }

    //관리자만 답변 완료(QUESTION_ANSWERED) 상태로 변경 가능
    @Transactional
    public Board answered(long boardId, long memberId) {
        Board board = findVerifiedBoard(boardId);

        //이미 답변이 완료된 질문인지 확인
        if (board.getQuestionStatus() == Board.QuestionStatus.QUESTION_ANSWERED) {
            throw new BusinessLogicException(ExceptionCode.NOT_FOUND);
        }

        //관리자 권한 확인
        Member member = memberService.findVerifiedMember(memberId);
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        if (!roles.contains("ADMIN")) {
            throw new BusinessLogicException(ExceptionCode.FORBIDDEN);
        }

        board.setQuestionStatus(Board.QuestionStatus.QUESTION_ANSWERED);
        return boardRepository.save(board);
    }

    private Board findVerifiedBoard(long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NOT_FOUND));
        //삭제된 질문은 조회가 안됩니다.
        if (board.getQuestionStatus() == Board.QuestionStatus.QUESTION_DELETED) {
            throw new BusinessLogicException(ExceptionCode.NOT_FOUND);
        }

        return board;

    }


}
