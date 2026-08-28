package com.finance.finlog.global;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ViewController {

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "찬용");
        return "hello"; // 정해진 경로의 hello.html을 찾아서 적용
    }

    @GetMapping("/students")
    public String students(Model model) {
        List<String> names = List.of("철수", "영희", "민수");
        model.addAttribute("names",names);
        return "students";
    }

    @GetMapping("/view/oauth/callback")
    public String oauthCallback(@RequestParam String token,Model model){
        model.addAttribute("token",token);
                return "oauth-callback";
    }
}
