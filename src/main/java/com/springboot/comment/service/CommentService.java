package com.springboot.comment.service;

import com.springboot.board.entity.Board;
import com.springboot.board.repository.BoardRepository;
import com.springboot.comment.entity.Comment;
import com.springboot.comment.repository.CommentRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
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

    public Comment createComment(Long boardId, String content, boolean secret) {
        //게시글 조회
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NOT_FOUND));

        //admin 권한을 가졌는지 확인

        //댓글 이미 작성했는지 확인

        //댓글 작성하기



    }

    public Comment updateComment() {

    }

    public Comment deleteComment() {

    }
}
