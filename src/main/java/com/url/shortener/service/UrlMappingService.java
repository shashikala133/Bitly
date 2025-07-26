package com.url.shortener.service;

import com.url.shortener.dto.ClickEventDTO;
import com.url.shortener.dto.UrlMappingDto;
import com.url.shortener.model.ClickCount;
import com.url.shortener.model.UrlMapping;
import com.url.shortener.model.User;
import com.url.shortener.repository.ClickCountRepository;
import com.url.shortener.repository.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UrlMappingService {
    @Autowired
    private UrlMappingRepository urlMappingRepository;

    @Autowired
    private ClickCountRepository clickCountRepository;

    public UrlMappingDto createShortUrl(String originalUrl, User user) {
     String shortUrl=generateShortUrl(originalUrl);
        UrlMapping urlMapping=new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedDate(LocalDateTime.now());
        UrlMapping savedUrlMapping=urlMappingRepository.save(urlMapping);
        return convertToDto(savedUrlMapping);
    }

    private String generateShortUrl(String originalUrl) {
        String characters="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder url=new StringBuilder(8);
        Random random=new Random();
        for(int i=0;i<8;i++){
            url.append(characters.charAt(random.nextInt(characters.length())));
        }
        return url.toString();
    }

    public UrlMappingDto convertToDto(UrlMapping urlMapping){
        UrlMappingDto urlMappingDto=new UrlMappingDto();
        urlMappingDto.setId(urlMapping.getId());
        urlMappingDto.setOriginalUrl(urlMapping.getOriginalUrl());
        urlMappingDto.setShortUrl(urlMapping.getShortUrl());
        urlMappingDto.setCreatedAt(urlMapping.getCreatedDate());
        urlMappingDto.setClickCount(urlMapping.getClickCount());
        urlMappingDto.setUsername(urlMapping.getUser().getUsername());
        return urlMappingDto;
    }

    public List<UrlMappingDto> getUrlsByUser(User user) {
        return urlMappingRepository.findByUser(user).stream().map(url->this.convertToDto(url)).toList();
    }

    public List<ClickEventDTO> getAnalytics(String shortUrl, LocalDateTime start, LocalDateTime end) {
        UrlMapping urlMapping= urlMappingRepository.findByShortUrl(shortUrl);
       if(urlMapping!=null){
           List<ClickEventDTO> list = clickCountRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end).stream()
                   .collect(Collectors.groupingBy(clickCount -> clickCount.getClickDate(), Collectors.counting()))
                   .entrySet().stream().map(
                           event -> {
                               ClickEventDTO dto = new ClickEventDTO();
                               dto.setClickDate(event.getKey().toLocalDate());
                               dto.setCount(event.getValue());
                               return dto;
                           }
                   ).toList();
           return list;
       }
       return null;
    }

    public Map<LocalDate, Long> getAnalyticsByUserAndDate(User user, LocalDate start, LocalDate end) {
        List<UrlMapping> urlMappingList=urlMappingRepository.findByUser(user);
        Map<LocalDate, Long> collected = clickCountRepository.findByUrlMappingInAndClickDateBetween(urlMappingList, start.atStartOfDay(), end.plusDays(1).atStartOfDay()).stream().
                collect(Collectors.groupingBy(clickCount -> clickCount.getClickDate().toLocalDate(), Collectors.counting()));
        return collected;
    }

    public UrlMapping findByShortUrl(String shortUrl) {
        UrlMapping byShortUrl = urlMappingRepository.findByShortUrl(shortUrl);
        if(byShortUrl!=null){
            byShortUrl.setClickCount(byShortUrl.getClickCount()+1);
            urlMappingRepository.save(byShortUrl);
            ClickCount clickCount=new ClickCount();
            clickCount.setClickDate(LocalDateTime.now());
            clickCount.setUrlMapping(byShortUrl);
            clickCountRepository.save(clickCount);
        }
        return byShortUrl;
    }
}
