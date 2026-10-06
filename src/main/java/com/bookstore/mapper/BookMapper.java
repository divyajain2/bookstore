package com.bookstore.mapper;

import com.bookstore.domain.Book;
import com.bookstore.domain.enums.BookCategory;
import com.bookstore.dto.request.CreateBookRequest;
import com.bookstore.dto.request.UpdateBookRequest;
import com.bookstore.dto.response.BookResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    Book toEntity(CreateBookRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    void updateEntityFromRequest(UpdateBookRequest request, @MappingTarget Book book);

    @Mapping(target = "inStock", expression = "java(book.hasStock(1))")
    BookResponse toResponse(Book book);

    default BookResponse toResponseWithStockCheck(Book book) {
        return toResponse(book);
    }
}