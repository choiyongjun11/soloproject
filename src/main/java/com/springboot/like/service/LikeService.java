package com.springboot.like.service;

import com.springboot.board.entity.Board;
import com.springboot.board.repository.BoardRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.like.entity.Like;
import com.springboot.like.repository.LikeRepository;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final MemberService memberService;

    @Transactional
    public void toggleLike(Long boardId, Long memberId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NOT_FOUND));

        Member member = memberService.findVerifiedMember(memberId);

        Optional<Like> existingLike = likeRepository.findByMemberAndBoard(member, board);

        if (existingLike.isPresent()) {
            Like like = existingLike.get();
            if (like.isLiked()) {
                like.setLiked(false);
                board.decreaseLikeCount();
            } else {
                like.setLiked(true);
                board.increaseLikeCount();
            }
            likeRepository.save(like);
        } else {
            Like newLike = new Like();
            newLike.setMember(member);
            newLike.setBoard(board);
            newLike.setLiked(true);
            board.increaseLikeCount();
            likeRepository.save(newLike);
        }
    }
}
