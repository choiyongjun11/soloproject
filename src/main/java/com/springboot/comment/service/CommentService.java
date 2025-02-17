package com.springboot.comment.service;

import com.springboot.auth.utils.AuthorityUtils;
import com.springboot.board.entity.Board;
import com.springboot.board.repository.BoardRepository;
import com.springboot.comment.dto.CommentDto;
import com.springboot.comment.entity.Comment;
import com.springboot.comment.mapper.CommentMapper;
import com.springboot.comment.repository.CommentRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final AuthorityUtils authorityUtils;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository, MemberRepository memberRepository, AuthorityUtils authorityUtils, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
        this.authorityUtils = authorityUtils;
        this.commentMapper = commentMapper;
    }

    /*
    **질문에 대한 답변 등록 - 회원(고객)이 등록한 1건의 질문에 관리자가 답변을 등록하는 기능 (Create)**

    - 답변은 관리자만 등록할 수 있다.
    - 답변은 관리자가 한 건만 등록할 수 있다.
    - 답변 등록시 답변 등록 날짜가 생성 되어야 한다.
    - 답변이 등록되면 , 질문의 상태 값은 QUESTION_ANSWERED로 변경되어야 한다.
    - 답변 내용은 필수입력 사항이다.
    - 답변의 경우 질문이 비밀글이면 답변도 비밀글이 되어야 하고, 질문이 공개글이면 답변도 공개글이 되어야 한다.
     */
    //등록, 수정, 삭제 만 기능 만들기,
    //게시글 조회 후 -> content(댓글) 작성하기, 작성은 admin 권한을 가진 사용자만 가능합니다.

    public Comment createComment(Long boardId, String email, CommentDto.Post postDto) {
        // 1. 게시글 조회
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NOT_FOUND));

        // 2. 회원 조회 (email 기반)
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NOT_FOUND));

        // 3. 관리자 권한 확인
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        if (!roles.contains("ADMIN")) {
            throw new BusinessLogicException(ExceptionCode.METHOD_NOT_ALLOWED);
        }

        // 4. 이미 답변이 등록되었는지 확인
        if (commentRepository.existsByBoard(board)) {
            throw new BusinessLogicException(ExceptionCode.NOT_EXTENDED);
        }

        // 5. 댓글 생성 -> commentDto -> Response 클래스 참고
        Comment comment = commentMapper.PostDtoToComment(postDto);
        comment.setBoard(board);
        comment.setMember(member);
        comment.setSecret(postDto.isSecret()); //dto에서 가져옴
        comment.setCreatedAt(LocalDateTime.now());


        // 6. 질문 상태 변경
        board.setQuestionStatus(Board.QuestionStatus.QUESTION_ANSWERED);
        boardRepository.save(board);

        // 7. 댓글 저장 후 반환
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long commentId, String email, CommentDto.Patch patchDto) {
        //**질문에 대한 답변 수정 - 회원(고객)이 등록한 1건의 질문에 관리자가 등록한 답변을 수정하는 기능 (Update)**
        //- 등록된 답변의 내용은 답변을 등록한 관리자만 수정할 수 있어야 한다.
        // 1. 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NOT_FOUND));

        // 2. 회원 조회 (email 기반)
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NOT_FOUND));

        // 3. 댓글 작성자(관리자)만 수정 가능
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        if (!roles.contains("ADMIN")) {
            throw new BusinessLogicException(ExceptionCode.METHOD_NOT_ALLOWED);
        }

        // 4. 수정할 내용이 비어 있으면 예외 발생
        if (patchDto.getContent() == null || patchDto.getContent().trim().isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.NOT_FOUND);
        }

        // 5. 댓글 업데이트
        comment.setContent(patchDto.getContent());
        comment.setSecret(patchDto.isSecret()); // 비밀 여부 업데이트

        // 6. 변경 사항 저장 후 엔티티 반환
        return commentRepository.save(comment);

    }

    public void deleteComment(Long commentId, String email) {
        //**질문에 대한 답변 삭제 - 회원(고객)이 등록한 1건의 질문에 관리자가 등록한 답변을 삭제하는 기능**
        //- 답변은 관리자만 삭제할 수 있다.
        //- 답변 삭제 시, 테이블에서 row 가 완전히 삭제되도록 한다.(기능 난이도를 고려한 요구사항)

        // 1. 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NOT_FOUND));

        // 2. 회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NOT_FOUND));

        // 3. 관리자 권한 확인
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        if (!roles.contains("ADMIN")) {
            throw new BusinessLogicException(ExceptionCode.METHOD_NOT_ALLOWED);
        }

        // 4. 댓글 삭제
        commentRepository.delete(comment);

    }

}
