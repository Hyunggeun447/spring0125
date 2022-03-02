package study.study.repository.orderRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.study.domain.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
}
