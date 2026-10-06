package com.bookstore.dto.response;

import com.bookstore.domain.enums.BookCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private BookCategory category;
    private boolean inStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}