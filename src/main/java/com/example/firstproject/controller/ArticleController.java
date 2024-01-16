package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticlesForm;
import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import com.example.firstproject.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentService commentService;

    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticlesForm form) {
        log.info(form.toString());
;//        System.out.println(form.toString());
        //DTO를 엔티티로 변환
        Article article = form.toEntity();
        System.out.println(article.toString());

        //리파지터리로 엔티티를 DB에 저장
        Article saved = articleRepository.save(article);
        log.info(form.toString());
//        System.out.println(saved.toString());
        return "redirect:/articles/" + saved.getId();
    }

    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model) {
        log.info("id = " + id);
//        1. id 조회해서 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);
        List<CommentDto> commentDtos = commentService.comments(id);

//        2.  모델에 데이터 등록하기
        model.addAttribute("article", articleEntity);
        model.addAttribute("commentDtos", commentDtos);

//        3. 뷰 페이지 반환하기
        return "articles/show";
    }

    @GetMapping("/articles")
    public String index(Model model) {
        //1. 모든 데이터 가져오기
        List<Article> ariclesEntityList = (List<Article>) articleRepository.findAll();
        //2. 모델에 데이터 등록하기
        model.addAttribute("articleList", ariclesEntityList);
        //3. 뷰 페이지 설정하기
        return "articles/index";
    }

    @GetMapping("articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        //수정할 데이터 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);
        //모델에 데이터 등록하기
        model.addAttribute("article", articleEntity);
        //뷰 페이지 설정
        return "articles/edit";
    }

    @PostMapping("articles/update")
    public String update(ArticlesForm form) {
        log.info(form.toString());
        // DTO는 저장할 객체 엔티티는 DTO의 객체를 테이블로 저장하는 곳
        //1. DTO를 엔티티로 변환
        Article articleEntity = form.toEntity();
        //2. 엔티티를 DB에 저장
        //2-1. DB에서 기존 데이터 가져오기
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        //2-2. 기존데이터 값을 갱신하기
        if (target != null) {
            articleRepository.save(articleEntity);
        }
        //3. 수정결과페이지로 리다이렉트
        return "redirect:/articles/" + articleEntity.getId();
    }

    @GetMapping("articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        log.info("삭제 요청이 들어왔습니다.");
//        1. 삭제 대상가져오기
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());
//        2.대상 엔티티 삭제하기
        if (target != null) {
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제되었습니다!");
        }
//        3. 결과페이질 리다이렉트하기
        return "redirect:/articles";
    }
}
