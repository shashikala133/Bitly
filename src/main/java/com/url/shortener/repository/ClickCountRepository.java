package com.url.shortener.repository;

import com.url.shortener.model.ClickCount;
import com.url.shortener.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickCountRepository extends JpaRepository<ClickCount,Long> {
    List<ClickCount> findByUrlMappingAndClickDateBetween(UrlMapping urlMapping, LocalDateTime startDate, LocalDateTime endDate);
    List<ClickCount> findByUrlMappingInAndClickDateBetween(List<UrlMapping> urlMappingList, LocalDateTime startDate, LocalDateTime endDate);
}
