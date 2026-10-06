package com.bookstore.mapper;

import com.bookstore.domain.Order;
import com.bookstore.domain.OrderItem;
import com.bookstore.domain.Book;
import com.bookstore.dto.request.OrderItemRequest;
import com.bookstore.dto.response.OrderItemResponse;
import com.bookstore.dto.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "items", source = "items")
    OrderResponse toResponse(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    OrderItem toEntity(OrderItemRequest request);

    default OrderItemResponse toItemResponse(OrderItem item) {
        if (item == null) return null;

        OrderItemResponse response = OrderItemResponse.builder()
                .id(item.getId())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .lineTotal(item.getLineTotal())
                .build();

        if (item.getBook() != null) {
            response.setBookIsbn(item.getBook().getIsbn());
            response.setBookTitle(item.getBook().getTitle());
            response.setBookAuthor(item.getBook().getAuthor());
        }

        return response;
    }

    default List<OrderItemResponse> toItemResponseList(List<OrderItem> items) {
        return items.stream()
                .map(this::toItemResponse)
                .collect(java.util.stream.Collectors.toList());
    }
}