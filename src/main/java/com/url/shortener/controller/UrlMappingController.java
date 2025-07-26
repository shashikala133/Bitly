package com.url.shortener.controller;

import com.url.shortener.dto.ClickEventDTO;
import com.url.shortener.dto.UrlMappingDto;
import com.url.shortener.model.User;
import com.url.shortener.service.UrlMappingService;
import com.url.shortener.service.UserDetailsImpl;
import com.url.shortener.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class UrlMappingController {
    @Autowired
    private UrlMappingService urlMappingService;
    @Autowired
    private UserService userService;


    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDto> createShortUrl(@RequestBody Map<String,String> request, Principal principal){
     String originalUrl=request.get("originalUrl");
     User user=userService.findUser(principal.getName());
     UrlMappingDto urlMappingDto=urlMappingService.createShortUrl(originalUrl,user);
     return ResponseEntity.ok(urlMappingDto);
    }

    @GetMapping("/my-urls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDto>> getMyUrls(Principal principal){
        User user=userService.findUser(principal.getName());
        List<UrlMappingDto> result=urlMappingService.getUrlsByUser(user);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClickEventDTO>> getAnalytics(@PathVariable String shortUrl, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        DateTimeFormatter formatter=DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime start=LocalDateTime.parse(startDate,formatter);
        LocalDateTime end=LocalDateTime.parse(endDate,formatter);
        List<ClickEventDTO> result=urlMappingService.getAnalytics(shortUrl,start,end);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/analytics")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<LocalDate,Long>> getAnalyticsByUserAndDate(Principal principal, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        DateTimeFormatter formatter=DateTimeFormatter.ISO_DATE;
        User user=userService.findUser(principal.getName());
        LocalDate start=LocalDate.parse(startDate,formatter);
        LocalDate end=LocalDate.parse(endDate,formatter);
        Map<LocalDate,Long> result=urlMappingService.getAnalyticsByUserAndDate(user,start,end);
        return ResponseEntity.ok(result);
    }


}
