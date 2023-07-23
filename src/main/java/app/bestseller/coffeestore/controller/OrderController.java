package app.bestseller.coffeestore.controller;

import app.bestseller.coffeestore.service.OrderService;
import app.bestseller.coffeestore.service.dto.OrderDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/orders", produces = "application/json")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Create an order for customer.")
    @PostMapping("/finalize/{id}/user/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@PathVariable("id") Long userId, @Valid @RequestBody List<OrderDTO> requests) {
        log.info(" ==> Request to register an order {} .", requests);
        orderService.createOrder(userId, requests);
    }

}
