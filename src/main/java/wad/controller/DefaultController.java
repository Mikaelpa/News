/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import wad.domain.Article;
import wad.domain.FileObject;
import wad.domain.Subject;
import wad.repository.ArticleRepository;
import wad.repository.FileRepository;
import wad.repository.SubjectRepository;
import wad.repository.WriterRepository;

@Controller
public class DefaultController {

    @Autowired
    public ArticleRepository articleRepo;

    @Autowired
    public WriterRepository writerRepo;

    @Autowired
    public SubjectRepository subjectRepo;

    @Autowired
    public FileRepository fileRepo;

    @PostConstruct
    public void init() {
        Subject world = new Subject("World");
        Subject weather = new Subject("Weather");
        Subject sports = new Subject("Sports");
        Subject entertainment = new Subject("Entertainment");
        subjectRepo.save(world);
        subjectRepo.save(sports);
        subjectRepo.save(entertainment);
        subjectRepo.save(weather);

    }

    @GetMapping("/")
    public String index(Model model) {
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "pdate");
        model.addAttribute("articles", articleRepo.findAll(pageable));
        model.addAttribute("subjects", subjectRepo.findAll());
        return "index";
    }

    @GetMapping("/popular")
    public String view(Model model) {
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "reloads");
        model.addAttribute("articles", articleRepo.findAll(pageable));
        model.addAttribute("subjects", subjectRepo.findAll());
        return "index";
    }

    @GetMapping("/{subjectName}")
    public String subject(Model model, @PathVariable("subjectName") String name) {
        model.addAttribute("subjects", subjectRepo.findAll());
        model.addAttribute("articles", articleRepo.findBySubject(name));
        return "subject";
    }

    @GetMapping("/articles/{articleId}")
    public String article(Model model, @PathVariable("articleId") Long id) {
        Article a = articleRepo.getOne(id);
        a.setReloads(a.getReloads() + 1);
        articleRepo.save(a);
        model.addAttribute("subjects", subjectRepo.findAll());
        model.addAttribute("articles", articleRepo.getOne(id));
        return "article";
    }

    @GetMapping(path = "/images/{id}/content", produces = "image/png")
    @ResponseBody
    public byte[] get(@PathVariable Long id) {
        return fileRepo.findByArt(id).getContent();
    }

    @PostMapping("/create")
    public String save(@RequestParam("file") MultipartFile file, @RequestParam("subject") List<String> subject, @RequestParam("writer") String writer, @RequestParam("header") String header, @RequestParam("ingress") String ingress, @RequestParam("content") String content) throws IOException {
        List<String> list = new ArrayList<>();
        for (String a : writer.split(";")) {
            list.add(a + " ");
        }
        if(articleRepo.findByHeader(header) != null) {
            articleRepo.delete(articleRepo.findByHeader(header));
        }
        Article article = new Article(header, ingress, content, subject, list, LocalDateTime.now(), 0);
        articleRepo.save(article);
        Long id = articleRepo.getOne(article.getId()).getId();
        FileObject fo = new FileObject();
        if (file.getContentType().equals("image/png")) {
            fo.setContent(file.getBytes());
            fo.setArt(id);
            fileRepo.save(fo);
        }

        return "redirect:/create";
    }

    @GetMapping("/create")
    public String create() {
        return "create";
    }
    
    @GetMapping("/edit")
    public String edit(Model model) {
        model.addAttribute("articles", articleRepo.findAll());
        model.addAttribute("subjects", subjectRepo.findAll());
        return "edit";
    }
    
    @GetMapping("/create/{articleId}")
    public String editA(Model model, @RequestParam("articleId") Long articleId) {
        String writers = "";
        for (String s : articleRepo.getOne(articleId).getWriterName()) {
            writers += s + ";";
        }
        model.addAttribute("headerp", articleRepo.getOne(articleId).getHeader());
        model.addAttribute("contentp", articleRepo.getOne(articleId).getContent());
        model.addAttribute("ingressp", articleRepo.getOne(articleId).getIngress());
        model.addAttribute("writerp", writers);
        model.addAttribute("subject", articleRepo.getOne(articleId).getSubject());
        model.addAttribute("articleId", articleId);
        return "create";
    }

}
