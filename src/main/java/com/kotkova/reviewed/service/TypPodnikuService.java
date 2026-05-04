package com.kotkova.reviewed.service;

import com.kotkova.reviewed.model.TypPodniku;
import com.kotkova.reviewed.repository.TypPodnikuRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypPodnikuService {

    private final TypPodnikuRepository typPodnikuRepository;

    public TypPodnikuService(TypPodnikuRepository typPodnikuRepository) {
        this.typPodnikuRepository = typPodnikuRepository;
    }

    public List<TypPodniku> ziskejVsechnyTypy() {
        return typPodnikuRepository.findAll();
    }
}