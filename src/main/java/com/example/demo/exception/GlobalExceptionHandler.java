package com.example.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 전역 예외 처리 핸들러
 *
 * 애플리케이션 전체에서 발생하는 예외를 중앙에서 처리합니다.
 * 각 예외 타입에 따라 적절한 에러 페이지나 메시지를 반환합니다.
 *
 * @ControllerAdvice: 모든 컨트롤러에 적용되는 전역 설정
 * @Slf4j: Lombok 로깅 애노테이션
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 리소스를 찾을 수 없을 때 처리
     *
     * @param ex                 발생한 예외
     * @param redirectAttributes 리다이렉트 시 전달할 속성
     * @return 에러 페이지 경로
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(
            ResourceNotFoundException ex,
            RedirectAttributes redirectAttributes) {

        log.error("리소스를 찾을 수 없음: {}", ex.getMessage(), ex);

        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/dashboard";
    }

    /**
     * 접근 권한이 없을 때 처리
     *
     * @param ex                 발생한 예외
     * @param redirectAttributes 리다이렉트 시 전달할 속성
     * @return 에러 페이지 경로
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(
            AccessDeniedException ex,
            RedirectAttributes redirectAttributes) {

        log.error("접근 권한 거부: {}", ex.getMessage(), ex);

        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/dashboard";
    }

    /**
     * 비즈니스 로직 예외 처리
     *
     * @param ex                 발생한 예외
     * @param redirectAttributes 리다이렉트 시 전달할 속성
     * @return 에러 페이지 경로
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBusinessException(
            BusinessException ex,
            RedirectAttributes redirectAttributes) {

        log.error("비즈니스 로직 오류: {}", ex.getMessage(), ex);

        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/dashboard";
    }

    /**
     * 입력 검증 실패 시 처리 (Bean Validation)
     *
     * @param ex    발생한 예외
     * @param model 뷰에 전달할 모델
     * @return 에러 페이지 경로
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBindException(BindException ex, Model model) {

        log.error("입력 검증 실패: {}", ex.getMessage(), ex);

        // 첫 번째 검증 오류 메시지를 가져옴
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("입력값이 올바르지 않습니다.");

        model.addAttribute("error", errorMessage);
        return "error";
    }

    /**
     * 예상하지 못한 모든 예외 처리
     *
     * @param ex                 발생한 예외
     * @param redirectAttributes 리다이렉트 시 전달할 속성
     * @return 에러 페이지 경로
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(
            Exception ex,
            RedirectAttributes redirectAttributes) {

        log.error("예상하지 못한 오류 발생: {}", ex.getMessage(), ex);

        redirectAttributes.addFlashAttribute("error",
                "시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        return "redirect:/dashboard";
    }
}
