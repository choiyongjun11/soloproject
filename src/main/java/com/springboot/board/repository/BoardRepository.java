package com.springboot.board.repository;

import com.springboot.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

/*
 error code 발생. 25.02.15 23:03 - No property 'board' found for type 'Board'! Did you mean ''boardId''?
 핵심포인트: Board 엔티티에서 board라는 필드나 속성이 없기 때문에 이 쿼리를 만들수 없다.
 해결방법: Board 엔티티에서 boardId와 questionStatus라는 필드를 기준으로 조회하도록 메서드를 수정해야 합니다.

 findAllByBoard 메서드: findAllByBoard는 Board 엔티티에서 board라는 필드를 참조하고 있습니다.
 그러나 Board 클래스에서 board라는 필드가 없고, boardId라는 필드가 있습니다. 따라서 Board라는 필드 대신 boardId로 수정해야 합니다.

  findByBoard 메서드:
  마찬가지로 findByBoard 메서드도 Board 엔티티에 board라는 필드가 없어서 에러가 발생합니다. 이 역시 boardId로 변경해야 합니다.
 */
@Repository
public interface BoardRepository extends JpaRepository <Board,Long> {

    // 회원 ID와 상태 값으로 게시글을 페이징 조회
    Page<Board> findAllByMember_MemberIdAndQuestionStatusNot(Long memberId, Board.QuestionStatus questionStatus, Pageable pageable);

    // 관리자는 모든 질문을 조회할 수 있도록 별도 메서드 제공
    Page<Board> findAllByQuestionStatusNot(Board.QuestionStatus questionStatus, Pageable pageable);
}
