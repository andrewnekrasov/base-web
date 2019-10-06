package ru.ithex.baseweb.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
@CrossOrigin("*")
public class ReDoc {
    private final String title;

    public ReDoc(@Value("${app.name}") String title) {
        this.title = title;
    }

    @GetMapping("/documentation")
    public String documentation(
            @RequestHeader("host") String host,
            Model model){
        model.addAttribute("host", new StringBuilder("http://").append(host).append("/v2/api-docs")); //"host" - описан в doc.html
        model.addAttribute("title", title);
        return "doc";
    }
}
