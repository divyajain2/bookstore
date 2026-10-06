package com.bookstore.repository;

import com.bookstore.domain.Book;
import com.bookstore.domain.enums.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    Page<Book> findByCategory(BookCategory category, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Book> findByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')) AND b.category = :category")
    Page<Book> findByTitleAndCategory(@Param("title") String title, @Param("category") BookCategory category, Pageable pageable);

    List<Book> findByStockQuantityGreaterThan(int threshold);

    @Query("SELECT b FROM Book b WHERE b.stockQuantity = 0")
    List<Book> findOutOfStockBooks();

    boolean existsByIsbn(String isbn);

    long countByCategory(BookCategory category);
}