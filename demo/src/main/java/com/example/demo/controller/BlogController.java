package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;



//import com.example.demo.model.domain.Article;
import com.example.demo.model.service.TestService;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.BlogService;

import com.example.demo.model.domain.Board;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



@Controller // 컨트롤러 어노테이션 명시
public class BlogController {

    @Autowired //이거 추가했더니 오류 고쳐짐
    BlogService blogService;

    // @GetMapping("/article_list")
    // public String article_list() {
    // return "article_list";
    // }

    // @GetMapping("/article_list") // 게시판 링크 지정
    // public String article_list(Model model) {
    // List<Article> list = blogService.findAll(); // 게시판 리스트
    // model.addAttribute("articles", list); // 모델에 추가
    // return "article_list"; // .HTML 연결
    // }


    @Autowired 
    TestService testService; // DemoController 클래스 아래 객체 생성
    

    //페이지 리다이렉트
    // @PostMapping("/api/articles")
    // public String addArticle(@ModelAttribute AddArticleRequest request) {
    //     Article saveArticle = blogService.save(request);
    //     return "redirect:/article_list";
    // }
    
    @GetMapping("/favicon.ico")
    public void favicon() {
        // 아무 작업도 하지 않음
    }
    // @GetMapping("/article_edit/{id}") // 게시판링크지정
    // public String article_edit(Model model, @PathVariable Long id) {
    // Optional<Article> list = blogService.findById(id); // 선택한게시판글
    // if (list.isPresent()) {
    // model.addAttribute("article", list.get()); // 존재하면Article 객체를모델에추가
    // } else {
    // // 처리할로직추가(예: 오류페이지로리다이렉트, 예외처리등)
    // return"/error_page/article_error"; // 오류처리페이지로연결(이름수정됨)
    // }
    // return "article_edit"; // .HTML 연결
    // }
    
    // @PutMapping("/api/article_edit/{id}")
    // public String updateArticle(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
    //     blogService.update(id, request);
    //     return "redirect:/article_list"; // 글 수정 이후.html 연결
    // }

    // @DeleteMapping("/api/article_delete/{id}")
    // public String deleteArticle(@PathVariable Long id) {
    //     blogService.delete(id);
    //     return "redirect:/article_list";
    // }
    
    // @GetMapping("/board_list") // 새로운 게시판 링크 지정
    // public String board_list(Model model) {
    //     List<Board> list = blogService.findAll(); // 게시판 전체 리스트
    //     model.addAttribute("boards", list); // 모델에 추가
    //     return "board_list"; // .HTML 연결
    // }

    //11.20 주석처리
    @GetMapping("/board_view/{id}") // 게시판 링크 지정
    public String board_view(Model model, @PathVariable Long id) {
        Optional<Board> list = blogService.findById(id); // 선택한 게시판 글

        if (list.isPresent()) {
            model.addAttribute("boards", list.get()); // 존재할 경우 실제 Article 객체를 모델에 추가
        } else {
            // 처리할 로직 추가 (예: 오류 페이지로 리다이렉트, 예외 처리 등)
            return "/error_page/article_error"; // 오류 처리 페이지로 연결
        }
        return "board_view"; // .HTML 연결
    }


    //7주차 연습문제
    /*@GetMapping("/board_edit/{id}")
    public String board_edit(Model model, @PathVariable Long id) {
        Optional<Board> list = blogService.findById(id);
        if (list.isPresent()) {
        model.addAttribute("board", list.get());
        } else { // 처리할로직추가(예: 오류페이지로리다이렉트, 예외처리등)
        return"/error_page/article_error"; // 오류처리페이지로연결(이름수정됨)
        }
        return "board_edit"; // .HTML 연결
        }
*/
       /* @GetMapping("/board_edit") // 새로운 게시판 링크 지정
    public String board_edit(Model model) {
        List<Board> list = blogService.findAll(); // 게시판 전체 리스트
        model.addAttribute("boards", list); // 모델에 추가
        return "board_edit"; // .HTML 연결
    }*/

    /*
    @GetMapping("/board_edit")
    public String board_edit() {
        return "board_list";
    }
    */
    

    @PutMapping("/api/board_edit/{id}")
    public String updateBoard(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
    blogService.update(id, request);
    return "redirect:/board_list"; // 글 수정 이후 .html 연결
    }
            
    //

    @GetMapping("/board_write")
    public String board_write() {
        return "board_write";
    }


    //생략….
    @PostMapping("/api/boards") // 글쓰기 게시판 저장
    public String addboards(@ModelAttribute AddArticleRequest request) {
        blogService.save(request);
        return "redirect:/board_list"; // .HTML 연결
    }

    @GetMapping("/board_list") // 새로운 게시판 링크 지정
    public String board_list(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") String keyword) {
        PageRequest pageable = PageRequest.of(page, 3); // 한 페이지의 게시글 수
        Page<Board> list; // Page를 반환

        if (keyword.isEmpty()) {
            list = blogService.findAll(pageable); // 기본 전체 출력(키워드 x)
        } else {
            list = blogService.searchByKeyword(keyword, pageable); // 키워드로 검색
        }
        //8
        model.addAttribute("boards", list); // 모델에 추가
        model.addAttribute("totalPages", list.getTotalPages()); // 페이지 크기
        model.addAttribute("currentPage", page); // 페이지 번호
        model.addAttribute("keyword", keyword); // 키워드
        return "board_list"; // .HTML 연결
    }

    //삭제
    @DeleteMapping("/api/board_delete/{id}")
     public String deleteArticle(@PathVariable Long id) {
         blogService.delete(id);
         return "redirect:/board_list";
    }

    

}
