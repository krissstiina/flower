package com.example.demo.graphql;

import com.example.demo.Dto.OrderRequest;
import com.example.demo.Dto.OrderResponse;
import com.example.demo.Dto.PagedResponse;
import com.example.demo.Dto.UpdateOrderStatusRequest;
import com.example.demo.service.OrderService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@DgsComponent
public class OrderDataFetcher {

    private final OrderService orderService;

    public OrderDataFetcher(OrderService orderService) {
        this.orderService = orderService;
    }

    @DgsQuery
    public List<OrderResponse> getAllOrders(){
        return orderService.findAll();
    }

    @DgsQuery
    public OrderResponse orderById(@InputArgument Long id){
        return orderService.findById(id);
    }

    @DgsQuery
    public List<OrderResponse> ordersByStatus(@InputArgument String status){
        return orderService.findByStatus(status);
    }

    @DgsQuery
    public PagedResponse<OrderResponse> orders(@InputArgument int page, @InputArgument int size){
        return orderService.findAllOrders(page, size);
    }

    @DgsMutation
    public OrderResponse createOrder(@InputArgument("input")Map<String, Object> input){
        OrderRequest request = new OrderRequest(
                Long.parseLong(input.get("bouquetId").toString()),
                Integer.parseInt(input.get("quantity").toString()),
                (String) input.get("deliveryAddress")
        );
        return orderService.create(request);
    }

    @DgsMutation
    public OrderResponse updateOrderStatus(@InputArgument Long id, @InputArgument("input") Map<String,String> input ){
        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest(
                input.get("status")
        );
        return orderService.updateStatus(id, request);
    }

    @DgsMutation
    public OrderResponse updateDeliveryDate(@InputArgument Long id, @InputArgument String deliveryDate){
        LocalDateTime date = LocalDateTime.parse(deliveryDate, DateTimeFormatter.ISO_DATE_TIME);
        return orderService.updateDeliveryDate(id, date);
    }

    @DgsMutation
    public OrderResponse cancelOrder(@InputArgument Long id){
        return orderService.cancel(id);
    }

    @DgsMutation
    public Long deleteOrder(@InputArgument Long id) {
        orderService.delete(id);
        return id;
    }
}
