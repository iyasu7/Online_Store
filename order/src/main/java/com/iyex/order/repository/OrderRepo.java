package com.iyex.order.repository;

import com.iyex.order.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Orders, Long> {
}