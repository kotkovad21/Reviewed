package com.kotkova.reviewed.service;

import com.kotkova.reviewed.model.Stitek;
import com.kotkova.reviewed.repository.StitekRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StitekService{
    private final StitekRepository stitekRepository;

    public StitekService(StitekRepository stitekRepository) {
        this.stitekRepository = stitekRepository;
    }

    public List<Stitek> ziskejVsechnyStitky() {
        return stitekRepository.findAll();
    }
}
