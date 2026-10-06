package com.bookstore.mapper;

import com.bookstore.domain.Book;
import com.bookstore.dto.request.CreateBookRequest;
import com.bookstore.dto.request.UpdateBookRequest;
import com.bookstore.dto.response.BookResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-06T15:42:58+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.29 (IBM Corporation)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public Book toEntity(CreateBookRequest request) {
        if ( request == null ) {
            return null;
        }

        Book.BookBuilder book = Book.builder();

        book.isbn( request.getIsbn() );
        book.title( request.getTitle() );
        book.author( request.getAuthor() );
        book.description( request.getDescription() );
        book.price( request.getPrice() );
        book.stockQuantity( request.getStockQuantity() );
        book.category( request.getCategory() );

        return book.build();
    }

    @Override
    public void updateEntityFromRequest(UpdateBookRequest request, Book book) {
        if ( request == null ) {
            return;
        }

        if ( request.getTitle() != null ) {
            book.setTitle( request.getTitle() );
        }
        if ( request.getAuthor() != null ) {
            book.setAuthor( request.getAuthor() );
        }
        if ( request.getDescription() != null ) {
            book.setDescription( request.getDescription() );
        }
        if ( request.getPrice() != null ) {
            book.setPrice( request.getPrice() );
        }
        if ( request.getStockQuantity() != null ) {
            book.setStockQuantity( request.getStockQuantity() );
        }
        if ( request.getCategory() != null ) {
            book.setCategory( request.getCategory() );
        }
    }

    @Override
    public BookResponse toResponse(Book book) {
        if ( book == null ) {
            return null;
        }

        BookResponse.BookResponseBuilder bookResponse = BookResponse.builder();

        bookResponse.id( book.getId() );
        bookResponse.isbn( book.getIsbn() );
        bookResponse.title( book.getTitle() );
        bookResponse.author( book.getAuthor() );
        bookResponse.description( book.getDescription() );
        bookResponse.price( book.getPrice() );
        bookResponse.stockQuantity( book.getStockQuantity() );
        bookResponse.category( book.getCategory() );
        bookResponse.createdAt( book.getCreatedAt() );
        bookResponse.updatedAt( book.getUpdatedAt() );

        bookResponse.inStock( book.hasStock(1) );

        return bookResponse.build();
    }
}
