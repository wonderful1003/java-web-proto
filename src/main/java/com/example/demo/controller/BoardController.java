package com.example.demo.controller;

import com.example.demo.entity.Board;
import com.example.demo.entity.Menu;
import com.example.demo.entity.User;
import com.example.demo.repository.BoardRepository;
import com.example.demo.service.MenuService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 자유게시판 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final UserService userService;
    private final MenuService menuService;
    private final BoardRepository boardRepository;

    @GetMapping
    public String board(@RequestParam(defaultValue = "0") int page,
                       Authentication authentication,
                       Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        Pageable pageable = PageRequest.of(page, 10);
        Page<Board> boardPage = boardRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<Menu> menus = menuService.getMenusForUser(user);
        boolean isAdmin = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        model.addAttribute("user", user);
        model.addAttribute("menus", menus);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("boardPage", boardPage);
        model.addAttribute("currentPage", page);

        return "board";
    }

    @GetMapping("/write")
    public String writeForm(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        List<Menu> menus = menuService.getMenusForUser(user);
        boolean isAdmin = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        model.addAttribute("user", user);
        model.addAttribute("menus", menus);
        model.addAttribute("isAdmin", isAdmin);

        return "board-write";
    }

    @PostMapping("/write")
    public String write(@RequestParam String title,
                       @RequestParam String content,
                       Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        Board board = new Board();
        board.setUser(user);
        board.setTitle(title);
        board.setContent(content);
        board.setViewCount(0);
        board.setCreatedAt(LocalDateTime.now());
        board.setUpdatedAt(LocalDateTime.now());

        boardRepository.save(board);

        return "redirect:/board";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        // 조회수 증가
        board.setViewCount(board.getViewCount() + 1);
        boardRepository.save(board);

        List<Menu> menus = menuService.getMenusForUser(user);
        boolean isAdmin = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        model.addAttribute("user", user);
        model.addAttribute("menus", menus);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("board", board);

        return "board-view";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        boolean isAdmin = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // 본인 글이거나 관리자만 삭제 가능
        if (board.getUser().getUsername().equals(username) || isAdmin) {
            boardRepository.delete(board);
        }

        return "redirect:/board";
    }
}
