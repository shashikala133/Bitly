package com.url.shortener.dto;

import java.time.LocalDate;

public class ClickEventDTO {

    private Long count;
    private LocalDate clickDate;

    public ClickEventDTO(Long count, LocalDate clickDate) {
        this.count = count;
        this.clickDate = clickDate;
    }

    public ClickEventDTO() {
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public LocalDate getClickDate() {
        return clickDate;
    }

    public void setClickDate(LocalDate clickDate) {
        this.clickDate = clickDate;
    }

    @Override
    public String toString() {
        return "ClickEventDTO{" +
                "count=" + count +
                ", clickDate=" + clickDate +
                '}';
    }
}
