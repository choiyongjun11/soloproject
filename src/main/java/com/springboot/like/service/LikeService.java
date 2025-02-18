package com.springboot.like.service;

import com.springboot.board.entity.Board;
import com.springboot.board.repository.BoardRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.like.entity.Like;
import com.springboot.like.repository.LikeRepository;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;

import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service

public class LikeService {
    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final MemberService memberService;

    public LikeService(LikeRepository likeRepository, BoardRepository boardRepository, MemberService memberService) {
        this.likeRepository = likeRepository;
        this.boardRepository = boardRepository;
        this.memberService = memberService;
    }


    @Transactional
    public int toggleLike(Long boardId, Long memberId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NOT_FOUND));

        Member member = memberService.findVerifiedMember(memberId);

        Optional<Like> existingLike = likeRepository.findByMemberAndBoard(member, board);

        if (existingLike.isPresent()) {
            //이미 좋아요를 눌렀다면 삭제
            likeRepository.delete(existingLike.get());
            board.decreaseLikeCount();
        }
        else {
            //좋아요가 없다면 추가

            Like newLike = new Like();
            newLike.setMember(member);
            newLike.setBoard(board);
            newLike.setLiked(true);
            board.increaseLikeCount();
            likeRepository.save(newLike);
        }

        return board.getLikeCount(); //현재 게시글의 좋아요 개수 반환 (0 or 1)

    }
}
