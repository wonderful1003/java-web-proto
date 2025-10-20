package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 게시글 생성/수정 요청 DTO
 *
 * 클라이언트로부터 게시글 정보를 받을 때 사용합니다.
 * Bean Validation을 통해 입력값을 검증합니다.
 *
 * @author JAVA-WEB-PROTO
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardRequest {

    /**
     * 게시글 제목
     * 필수 입력값, 최소 1자 ~ 최대 200자
     */
    @NotBlank(message = "제목은 필수입니다.")
    @Size(min = 1, max = 200, message = "제목은 1자 이상 200자 이하여야 합니다.")
    private String title;

    /**
     * 게시글 내용
     * 필수 입력값
     */
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
}
