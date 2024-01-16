package com.example.firstproject.service;

import com.example.firstproject.dto.ArticlesForm;
import com.example.firstproject.entity.Article;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleServiceTest {
    @Autowired
    ArticleService articleService;

    @Test
    void index() {
//        1. 예상 데이터
        Article a = new Article(1L, "가가가가", "1111");
        Article b = new Article(2L, "나나나나", "2222");
        Article c = new Article(3L, "다다다다", "3333");
        List<Article> expected = new ArrayList<>(Arrays.asList(a, b, c));

//        2. 실제 데이터
        List<Article> articles = articleService.index();


//        3. 비교 및 검증
        assertEquals(expected.toString(), articles.toString());
    }

    @Test
    void show_true() {
        // 1. Expected Data
        Long id = 1L;
        Article expected = new Article(id, "가가가가", "1111");

        // 2. Real Data
        Article article = articleService.show(id);

        // 3. Compare and Verification
        assertEquals(expected.toString(), article.toString());

    }

    @Test
    void show_false() {
        Long id = -1L;
        Article expected = null;
        Article article = articleService.show(id);
        assertEquals(expected, article);
    }

    @Test
    @Transactional
    void create_succesed() {
        String title = "라라라라";
        String content = "4444";
        ArticlesForm dto = new ArticlesForm(null, title, content);
        Article expected = new Article(4L, title, content);

        Article article = articleService.create(dto);

        assertEquals(expected.toString(), article.toString());
    }

    @Test
    @Transactional
    void create_failed() {
        Long id = 4L;
        String title = "라라라라";
        String content = "4444";
        ArticlesForm dto = new ArticlesForm(id, title, content);
        Article expected = null;

        Article article = articleService.create(dto);

        assertEquals(expected, article);
    }
}