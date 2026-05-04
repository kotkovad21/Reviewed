package com.kotkova.reviewed.service;

import com.kotkova.reviewed.model.Viditelnost;
import com.kotkova.reviewed.repository.ViditelnostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViditelnostService {
    private final ViditelnostRepository viditelnostRepository;

    public ViditelnostService(ViditelnostRepository viditelnostRepository) {
        this.viditelnostRepository = viditelnostRepository;
    }

    public List<Viditelnost> ziskejVsechnyViditelnosti() {
        return viditelnostRepository.findAll();
    }
}
