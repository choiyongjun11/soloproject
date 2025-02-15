package com.springboot.board.mapper;

import com.springboot.board.dto.BoardDto;
import com.springboot.board.entity.Board;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    // Post DTO -> Board Entity 변환 (질문 등록)
    @Mapping(target = "questionStatus", constant = "QUESTION_REGISTERED") // 질문 상태 기본값 설정
    @Mapping(target = "viewCount", constant = "0L") // 조회수 기본값 설정
    Board PostDtoToBoard (BoardDto.Post postDto);
    // Patch DTO -> Board Entity 변환 (질문 수정)
    Board PatchDtoToBoard (BoardDto.Patch patchDto);
    // Board Entity -> Response DTO 변환 (질문 조회)
    BoardDto.Response boardToBoardDtoResponse(Board board);

}
