package com.springboot.comment.mapper;

import com.springboot.board.dto.BoardDto;
import com.springboot.comment.dto.CommentDto;
import com.springboot.comment.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment PostDtoToComment (CommentDto.Post postDto);
    Comment PatchDtoToComment (CommentDto.Patch patchDto);
    CommentDto.Response commentToCommentDtoResponse(Comment comment);
}
