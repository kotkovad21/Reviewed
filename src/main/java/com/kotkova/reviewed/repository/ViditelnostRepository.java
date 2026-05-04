package com.kotkova.reviewed.repository;

import com.kotkova.reviewed.model.Viditelnost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViditelnostRepository extends JpaRepository<Viditelnost, Long>{
}
