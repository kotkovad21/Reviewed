package com.kotkova.reviewed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.kotkova.reviewed.service.PodnikService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class WebController {

    private final PodnikService podnikService;

    // Konstruktor pro vložení služby
    public WebController(PodnikService podnikService) {
        this.podnikService = podnikService;
    }

    // Tato metoda zachytí požadavek, když půjdeš na hlavní stránku ("/")
    @GetMapping("/")
    public String showHomepage(Model model) {
        // Vytáhneme podniky z Oracle
        var seznamPodniku = podnikService.ziskejVsechnyPodniky();

        // Pošleme je do HTML pod jménem "podnikyZDatabaze"
        model.addAttribute("podnikyZDatabaze", seznamPodniku);

        return "homepage";
    }
    @GetMapping("/place")
    public String showPlacePage() {
        return "place"; // Toto hledá soubor src/main/resources/templates/place.html
    }

    @GetMapping("/insert")
    public String showInsertPage() {
        return "insert";
    }
    @GetMapping("/profile")
    public String showProfilePage() {
        return "profile";
    }
    @GetMapping("/review")
    public String showReviewPage() {
        return "review";
    }
    @GetMapping("/visits")
    public String showVisitsPage() {
        return "visits";
    }
}