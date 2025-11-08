package com.example.ea_beadando;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {

    public MyController() {

    }

    @GetMapping("/fooldal")
    public String testController(Model model) {
        return "fooldal";
    }

    @GetMapping("/soap")
    public String soap(Model model) {
        return "soap";
    }

    @GetMapping("/facc")
    public String facc(Model model) {
        return "facc";
    }

    @GetMapping("/faktar")
    public String faktar(Model model) {
        return "faktar";
    }

    @GetMapping("/fhistar")
    public String fhistar(Model model) {
        return "fhistar";
    }

    @GetMapping("/fnyit")
    public String fnyit(Model model) {
        return "fnyit";
    }

    @GetMapping("/fpoz")
    public String fpoz(Model model) {
        return "fpoz";
    }

    @GetMapping("/fzar")
    public String fzar(Model model) {
        return "fzar";
    }
}
