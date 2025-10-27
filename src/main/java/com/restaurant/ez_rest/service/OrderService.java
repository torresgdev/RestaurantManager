package com.restaurant.ez_rest.service;


import com.restaurant.ez_rest.repository.OrderItemRepository;
import com.restaurant.ez_rest.repository.OrderRepository;
import com.restaurant.ez_rest.repository.ProductRepository;
import com.restaurant.ez_rest.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
}
