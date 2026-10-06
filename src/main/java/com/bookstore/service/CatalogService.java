package com.bookstore.service;

import com.bookstore.domain.Book;
import com.bookstore.domain.enums.BookCategory;
import com.bookstore.dto.request.CreateBookRequest;
import com.bookstore.dto.request.UpdateBookRequest;
import com.bookstore.dto.response.BookResponse;
import com.bookstore.dto.response.PageResponse;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.InsufficientInventoryException;
import com.bookstore.mapper.BookMapper;
import com.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatalogService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public PageResponse<BookResponse> searchBooks(String title, BookCategory category, Pageable pageable) {
        Page<Book> books;
        if (title != null && category != null) {
            books = bookRepository.findByTitleAndCategory(title, category, pageable);
        } else if (title != null) {
            books = bookRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else if (category != null) {
            books = bookRepository.findByCategory(category, pageable);
        } else {
            books = bookRepository.findAll(pageable);
        }
        return PageResponse.from(books.map(bookMapper::toResponseWithStockCheck));
    }

    public BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));
        return bookMapper.toResponseWithStockCheck(book);
    }

    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return bookMapper.toResponseWithStockCheck(book);
    }

    @Transactional
    public BookResponse createBook(CreateBookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + request.getIsbn() + " already exists");
        }
        Book book = bookMapper.toEntity(request);
        Book saved = bookRepository.save(book);
        return bookMapper.toResponseWithStockCheck(saved);
    }

    @Transactional
    public BookResponse updateBook(String isbn, UpdateBookRequest request) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));
        bookMapper.updateEntityFromRequest(request, book);
        Book saved = bookRepository.save(book);
        return bookMapper.toResponseWithStockCheck(saved);
    }

    @Transactional
    public BookResponse adjustStock(String isbn, int quantityChange) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));

        if (quantityChange > 0) {
            book.increaseStock(quantityChange);
        } else {
            if (!book.hasStock(Math.abs(quantityChange))) {
                throw new InsufficientInventoryException(isbn, Math.abs(quantityChange), book.getStockQuantity());
            }
            book.decreaseStock(Math.abs(quantityChange));
        }

        Book saved = bookRepository.save(book);
        return bookMapper.toResponseWithStockCheck(saved);
    }

    @Transactional
    public void deleteBook(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));
        bookRepository.delete(book);
    }

    public List<BookResponse> getLowStockBooks(int threshold) {
        return bookRepository.findByStockQuantityGreaterThan(threshold).stream()
                .map(bookMapper::toResponseWithStockCheck)
                .collect(Collectors.toList());
    }

    public List<BookResponse> getOutOfStockBooks() {
        return bookRepository.findOutOfStockBooks().stream()
                .map(bookMapper::toResponseWithStockCheck)
                .collect(Collectors.toList());
    }

    public long countByCategory(BookCategory category) {
        return bookRepository.countByCategory(category);
    }
}