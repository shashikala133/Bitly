package com.url.shortener.controller;

import com.url.shortener.model.UrlMapping;
import com.url.shortener.service.UrlMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {

    @Autowired
    UrlMappingService urlMappingService;

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirect(@PathVariable String shortUrl){
        UrlMapping byShortUrl = urlMappingService.findByShortUrl(shortUrl);
        if(byShortUrl!=null){
            HttpHeaders headers=new HttpHeaders();
            headers.add("location",byShortUrl.getOriginalUrl());
            return ResponseEntity.status(302).headers(headers).build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
