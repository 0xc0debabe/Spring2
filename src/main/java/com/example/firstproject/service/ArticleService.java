package com.example.firstproject.service;

import com.example.firstproject.dto.ArticlesForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> index() {
        return (List<Article>) articleRepository.findAll();
    }

    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticlesForm dto) {
        Article article = dto.toEntity();
        if (article.getId() != null) { // 1번 값이 있을경우 이것은 생성이 아니라 수정이여서 조건문 사용
            return null; // Ex) 1번 값이 존재할 경우, id : 1, title : asdf content : 222
        }                              // 이렇게하면 값이 또 생성이됨
        return articleRepository.save(article);
    }

    public Article update(Long id, ArticlesForm dto) {
// 1. DTO -> Entity 변환
        Article article = dto.toEntity();
        log.info(article.toString());

// 2. 타깃 조회하기
        Article target = articleRepository.findById(id).orElse(null);

// 3. 잘못된 요청 처리하기
        if (target == null || id != article.getId()) {
            log.info("🔴잘못된 요청  : " + article);
            return null;
        }

// 4. 업데이트 및 정상 응답하기
        target.patch(article);
        Article update = articleRepository.save(target);
        return update;
    }

    public Article delete(Long id) {
        // 1. 대창찾기
        Article target = articleRepository.findById(id).orElse(null);

// 2. 잘못된 요청 처리하기
        if (target == null) {
            return null;
        }

// 3. 대상 삭제하기
        articleRepository.delete(target);
        return target;
    }

    @Transactional
    public List<Article> createArticles(List<ArticlesForm> dtos) {
//         1. dto 묶음을 엔티티 묶음으로 변환하기
        List<Article> articleList = dtos.stream()
                .map(dto -> dto.toEntity())
                .collect(Collectors.toList());

//         2. 엔티티 묶음을 DB에 저장하기
        articleList.stream()
                .forEach(article -> articleRepository.save(article));

//         3. 강제 예외 발생시키기
        articleRepository.findById(-1L)
                .orElseThrow(() -> new IllegalArgumentException("결제 실패!"));

//         4. 결과 값 반환하기
        return articleList;
    }
}
