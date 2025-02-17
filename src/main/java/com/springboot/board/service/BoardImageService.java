package com.springboot.board.service;

import com.springboot.board.entity.Board;
import com.springboot.board.entity.BoardImage;
import com.springboot.board.repository.BoardImageRepository;
import com.springboot.board.repository.BoardRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


//이미지 파일 저장 및 board 와 연결 할 수 있도록 하자.
@Service
public class BoardImageService {

    private final BoardImageRepository boardImageRepository;
    private final BoardRepository boardRepository;
    private final FileUploadService fileUploadService;

    public BoardImageService(BoardImageRepository boardImageRepository, BoardRepository boardRepository, FileUploadService fileUploadService) {
        this.boardImageRepository = boardImageRepository;
        this.boardRepository = boardRepository;
        this.fileUploadService = fileUploadService;
    }

    /**
     * 게시글에 이미지 추가
     */
    public void addImagesToBoard(Long boardId, List<MultipartFile> images) {
        // 1. 게시글 조회
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NOT_FOUND));

        // 2. 이미지 저장
        List<BoardImage> boardImages = images.stream()
                .map(image -> {
                    try {
                        // 파일 저장 및 경로 반환
                        String imagePath = fileUploadService.storeFile(image);
                        return new BoardImage(null , imagePath, board);
                    } catch (RuntimeException e) {
                        throw new BusinessLogicException(ExceptionCode.NOT_FOUND);
                    }
                })
                .collect(Collectors.toList());

        // 3. 이미지 정보 저장
        boardImageRepository.saveAll(boardImages);
    }


    /**
     * 게시글의 이미지 목록 조회
     */
    public List<String> getBoardImages(Long boardId) {
        // 1. 게시글 찾기
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NOT_FOUND));

        // 2. 게시글에 연결된 이미지 가져오기
        List<BoardImage> boardImages = boardImageRepository.findByBoard(board);

        // 3. 이미지 경로만 리스트로 변환
        return boardImages.stream()
                .map(BoardImage::getImagePath)
                .collect(Collectors.toList());
    }







}
