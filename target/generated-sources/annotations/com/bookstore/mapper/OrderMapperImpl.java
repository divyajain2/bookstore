package com.bookstore.mapper;

import com.bookstore.domain.Order;
import com.bookstore.domain.OrderItem;
import com.bookstore.dto.request.OrderItemRequest;
import com.bookstore.dto.response.OrderResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-06T15:42:58+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.29 (IBM Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderResponse toResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderResponse.OrderResponseBuilder orderResponse = OrderResponse.builder();

        orderResponse.items( toItemResponseList( order.getItems() ) );
        orderResponse.id( order.getId() );
        orderResponse.orderNumber( order.getOrderNumber() );
        orderResponse.status( order.getStatus() );
        orderResponse.totalAmount( order.getTotalAmount() );
        orderResponse.placedAt( order.getPlacedAt() );
        orderResponse.updatedAt( order.getUpdatedAt() );

        return orderResponse.build();
    }

    @Override
    public OrderItem toEntity(OrderItemRequest request) {
        if ( request == null ) {
            return null;
        }

        OrderItem.OrderItemBuilder orderItem = OrderItem.builder();

        orderItem.quantity( request.getQuantity() );

        return orderItem.build();
    }
}
