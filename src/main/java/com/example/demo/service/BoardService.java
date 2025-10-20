package com.example.demo.service;

import com.example.demo.constant.ErrorMessages;
import com.example.demo.dto.BoardRequest;
import com.example.demo.entity.Board;
import com.example.demo.entity.User;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 게시판 비즈니스 로직 서비스
 *
 * 게시판 관련 모든 비즈니스 로직을 처리합니다:
 * - 게시글 조회 (전체 목록, 단건)
 * - 게시글 생성
 * - 게시글 수정
 * - 게시글 삭제
 * - 조회수 증가
 * - 권한 검증
 * - 페이지네이션
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 기본 페이지 크기 (한 페이지에 표시할 게시글 수)
     */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 모든 게시글 조회 (페이지네이션)
     *
     * @param page 페이지 번호 (0부터 시작)
     * @return 게시글 페이지 객체
     */
    public Page<Board> getAllBoards(int page) {
        log.info("게시글 목록 조회 - 페이지: {}", page);
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return boardRepository.findAll(pageable);
    }

    /**
     * ID로 게시글 조회
     *
     * @param id 게시글 ID
     * @return 조회된 게시글
     * @throws ResourceNotFoundException 게시글을 찾을 수 없을 때
     */
    public Board getBoardById(Long id) {
        log.debug("게시글 조회: ID={}", id);
        return boardRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("게시글을 찾을 수 없음: ID={}", id);
                    return new ResourceNotFoundException(ErrorMessages.BOARD_NOT_FOUND);
                });
    }

    /**
     * 새로운 게시글 생성
     *
     * @param request 게시글 생성 요청 DTO
     * @param user    게시글 작성자
     * @return 생성된 게시글
     */
    @Transactional
    public Board createBoard(BoardRequest request, User user) {
        log.info("게시글 생성 - 작성자: {}, 제목: {}", user.getUsername(), request.getTitle());

        Board board = new Board();
        board.setUser(user);
        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        board.setViewCount(0);
        board.setCreatedAt(LocalDateTime.now());
        board.setUpdatedAt(LocalDateTime.now());

        Board saved = boardRepository.save(board);
        log.info("게시글 생성 완료: ID={}", saved.getId());
        return saved;
    }

    /**
     * 게시글 수정 (권한 검증 포함)
     *
     * @param id          수정할 게시글 ID
     * @param request     수정할 내용
     * @param currentUser 현재 로그인한 사용자
     * @param isAdmin     현재 사용자가 관리자인지 여부
     * @return 수정된 게시글
     * @throws ResourceNotFoundException 게시글을 찾을 수 없을 때
     * @throws AccessDeniedException     권한이 없을 때
     */
    @Transactional
    public Board updateBoard(Long id, BoardRequest request, User currentUser, boolean isAdmin) {
        Board board = getBoardById(id);

        // 권한 검증: 작성자이거나 관리자만 수정 가능
        if (!board.getUser().getId().equals(currentUser.getId()) && !isAdmin) {
            log.warn("게시글 수정 권한 없음 - 게시글 ID: {}, 사용자: {}",
                    id, currentUser.getUsername());
            throw new AccessDeniedException(ErrorMessages.BOARD_ACCESS_DENIED);
        }

        log.info("게시글 수정 - ID: {}, 작성자: {}, 수정 요청자: {}",
                id, board.getUser().getUsername(), currentUser.getUsername());

        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        board.setUpdatedAt(LocalDateTime.now());

        return boardRepository.save(board);
    }

    /**
     * 게시글 삭제 (권한 검증 포함)
     *
     * @param id          삭제할 게시글 ID
     * @param currentUser 현재 로그인한 사용자
     * @param isAdmin     현재 사용자가 관리자인지 여부
     * @throws ResourceNotFoundException 게시글을 찾을 수 없을 때
     * @throws AccessDeniedException     권한이 없을 때
     */
    @Transactional
    public void deleteBoard(Long id, User currentUser, boolean isAdmin) {
        Board board = getBoardById(id);

        // 권한 검증: 작성자이거나 관리자만 삭제 가능
        if (!board.getUser().getId().equals(currentUser.getId()) && !isAdmin) {
            log.warn("게시글 삭제 권한 없음 - 게시글 ID: {}, 사용자: {}",
                    id, currentUser.getUsername());
            throw new AccessDeniedException(ErrorMessages.BOARD_ACCESS_DENIED);
        }

        log.info("게시글 삭제 - ID: {}, 작성자: {}, 삭제 요청자: {}",
                id, board.getUser().getUsername(), currentUser.getUsername());
        boardRepository.delete(board);
    }

    /**
     * 조회수 증가
     *
     * @param id 게시글 ID
     * @return 조회수가 증가된 게시글
     */
    @Transactional
    public Board incrementViewCount(Long id) {
        Board board = getBoardById(id);
        board.setViewCount(board.getViewCount() + 1);
        log.debug("게시글 조회수 증가 - ID: {}, 조회수: {}", id, board.getViewCount());
        return boardRepository.save(board);
    }

    /**
     * 게시글 작성자 확인
     *
     * @param board       확인할 게시글
     * @param currentUser 현재 사용자
     * @return 작성자이면 true, 아니면 false
     */
    public boolean isAuthor(Board board, User currentUser) {
        boolean isAuthor = board.getUser().getId().equals(currentUser.getId());
        log.debug("게시글 작성자 확인 - 게시글 ID: {}, 작성자 여부: {}", board.getId(), isAuthor);
        return isAuthor;
    }
}
