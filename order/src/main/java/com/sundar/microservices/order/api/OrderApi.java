package com.sundar.microservices.order.api;

import com.sundar.microservices.order.api.model.ErrorResponse;
import com.sundar.microservices.order.api.model.Order;
import com.sundar.microservices.order.common.Constants;
import com.sundar.microservices.order.persistence.Schema.OrderSchema;
import com.sundar.microservices.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = Constants.API_ORDER_PATH)
@Api(tags="Orders", description="Operations pertaining to order services")
@Slf4j
public class OrderApi {

    @Autowired
    private OrderService orderService;

    @ApiOperation("Get orders by id")
    @ApiResponses( value = {
            @ApiResponse(code = 200, response = OrderSchema.class, message = "Fetched order successfully."),
            @ApiResponse(code = 404, response = ErrorResponse.class, message = "Order not found error."),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Bad Request."),
            @ApiResponse(code = 401, response = ErrorResponse.class, message = "Unauthorized."),
            @ApiResponse(code = 403, response = ErrorResponse.class, message = "Forbidden."),
            @ApiResponse(code = 500, response = ErrorResponse.class, message = "System error.")
    }
    )
    @GetMapping(path = "/{orderId}")
    public OrderSchema getByOrderId(@PathVariable("orderId") String id){

        return orderService.load(id);
    }


    @GetMapping
    public List<OrderSchema> getByOrderCorrelationId(@RequestParam ("correlationId") String correlationId){

        return orderService.loadByCorrelationId(correlationId);
    }


    @ApiOperation("Adding new order upon a correlation id")
    @ApiResponses( value = {
            @ApiResponse(code = 200, response = OrderSchema.class, message = "Order has been added successfully"),
            @ApiResponse(code = 404, response = ErrorResponse.class, message = "Order not found error."),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Bad Request."),
            @ApiResponse(code = 401, response = ErrorResponse.class, message = "Unauthorized."),
            @ApiResponse(code = 403, response = ErrorResponse.class, message = "Forbidden."),
            @ApiResponse(code = 500, response = ErrorResponse.class, message = "System error.")
    }
    )
    @PostMapping(path = "/{correlationId}")
    public OrderSchema add(@PathVariable("correlationId") String correlationId,
                           @RequestBody Order order){
        return orderService.add(correlationId, order);
    }

}
