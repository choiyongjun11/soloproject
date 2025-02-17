package com.springboot.comment.mapper;

import com.springboot.board.dto.BoardDto;
import com.springboot.comment.dto.CommentDto;
import com.springboot.comment.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment PostDtoToComment (CommentDto.Post postDto);
    //Comment PatchDtoToComment (CommentDto.Patch patchDto);
    /*
    patch 요청의 핵심은 일부 필드만 수정하는 것입니다.
    patchDtoToComment를 사용하면 새로운 comment 객체를 생성해서 반환하는 방식이 된다.
    서비스 계층에서는 기존 comment 엔티티를 조회한 후,
    특정 필드만 업데이트 하는 방식이므로 굳이 새로운 객체로 반환할 필요가 없습니다.
     */
    CommentDto.Response commentToCommentDtoResponse(Comment comment);
}
