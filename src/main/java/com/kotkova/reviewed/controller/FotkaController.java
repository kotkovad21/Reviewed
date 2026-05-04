package com.kotkova.reviewed.controller;

import com.kotkova.reviewed.model.Fotka;
import com.kotkova.reviewed.repository.FotkaRepository;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Controller
public class FotkaController {

    private final FotkaRepository fotkaRepository;

    public FotkaController(FotkaRepository fotkaRepository) {
        this.fotkaRepository = fotkaRepository;
    }

    // Tento endpoint vrací obrázek místo HTML stránky
    @GetMapping("/image/fotka/{id}")
    public ResponseEntity<byte[]> ziskajObrazok(@PathVariable Long id) {
        // Použijeme findById, abychom zbytečně nenačítali nic jiného
        return fotkaRepository.findById(id)
                .map(fotka -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // Pokud máš i PNG, Spring to většinou přechroustá
                        // PŘIDÁNO: Kešování na 1 den (86400 sekund)
                        .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
                        .body(fotka.getData()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}