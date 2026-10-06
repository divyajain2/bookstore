package com.bookstore.controller;

import com.bookstore.domain.enums.BookCategory;
import com.bookstore.dto.request.CreateBookRequest;
import com.bookstore.dto.request.UpdateBookRequest;
import com.bookstore.dto.response.BookResponse;
import com.bookstore.dto.response.PageResponse;
import com.bookstore.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/catalog")
@RequiredArgsConstructor
@Tag(name = "Catalog", description = "Book catalog management APIs")
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/books")
    @Operation(summary = "Search books with optional filters and pagination")
    public ResponseEntity<PageResponse<BookResponse>> searchBooks(
            @Parameter(description = "Filter by title (partial match, case-insensitive)")
            @RequestParam(required = false) String title,
            @Parameter(description = "Filter by category")
            @RequestParam(required = false) BookCategory category,
            @PageableDefault(size = 20, sort = "title") Pageable pageable) {
        PageResponse<BookResponse> response = catalogService.searchBooks(title, category, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/books/{isbn}")
    @Operation(summary = "Get book by ISBN")
    public ResponseEntity<BookResponse> getBookByIsbn(
            @Parameter(description = "Book ISBN") @PathVariable String isbn) {
        BookResponse response = catalogService.getBookByIsbn(isbn);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/books")
    @Operation(summary = "Create a new book")
    public ResponseEntity<BookResponse> createBook(
            @Valid @RequestBody CreateBookRequest request) {
        BookResponse response = catalogService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/books/{isbn}")
    @Operation(summary = "Update book details")
    public ResponseEntity<BookResponse> updateBook(
            @Parameter(description = "Book ISBN") @PathVariable String isbn,
            @Valid @RequestBody UpdateBookRequest request) {
        BookResponse response = catalogService.updateBook(isbn, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/books/{isbn}/stock")
    @Operation(summary = "Adjust book stock quantity (positive to add, negative to remove)")
    public ResponseEntity<BookResponse> adjustStock(
            @Parameter(description = "Book ISBN") @PathVariable String isbn,
            @Parameter(description = "Quantity change (positive or negative)") @RequestParam int quantityChange) {
        BookResponse response = catalogService.adjustStock(isbn, quantityChange);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/books/{isbn}")
    @Operation(summary = "Delete a book")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "Book ISBN") @PathVariable String isbn) {
        catalogService.deleteBook(isbn);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/books/low-stock")
    @Operation(summary = "Get books with stock below threshold")
    public ResponseEntity<java.util.List<BookResponse>> getLowStockBooks(
            @Parameter(description = "Stock threshold") @RequestParam(defaultValue = "10") int threshold) {
        return ResponseEntity.ok(catalogService.getLowStockBooks(threshold));
    }

    @GetMapping("/books/out-of-stock")
    @Operation(summary = "Get out of stock books")
    public ResponseEntity<java.util.List<BookResponse>> getOutOfStockBooks() {
        return ResponseEntity.ok(catalogService.getOutOfStockBooks());
    }
}