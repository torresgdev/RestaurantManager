package com.restaurant.ez_rest.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_table")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private RestaurantTable table;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "total_value", nullable = false)
    private BigDecimal totalValue = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDateTime openedAt;

    @Column
    private LocalDateTime closedAt;


    public Order(RestaurantTable table) {
        this.table = table;
        this.orderStatus = OrderStatus.OPEN;
        this.openedAt = LocalDateTime.now();
        this.totalValue = BigDecimal.ZERO;
    }


    @PostLoad // Chamado após a entidade ser carregada do banco
    public void initializeTotalValue() {
        if (this.totalValue == null) {
            this.totalValue = BigDecimal.ZERO;
        }

    }

}
