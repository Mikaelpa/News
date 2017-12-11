/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    
    List<Article> findBySubject(String subject);
    Article findByHeader(String header);
}