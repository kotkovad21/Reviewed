package com.kotkova.reviewed.service;

import com.kotkova.reviewed.model.Fotka;
import com.kotkova.reviewed.repository.FotkaRepository;
import org.springframework.stereotype.Service;

@Service
public class FotkaService {

    private final FotkaRepository fotkaRepository;

    public FotkaService(FotkaRepository fotkaRepository) {
        this.fotkaRepository = fotkaRepository;
    }

    public void ulozFotku(Fotka fotka) {
        fotkaRepository.save(fotka);
    }
}