package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(orderItem -> orderItem.getItem().getName());
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .toList();
        return result;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> orderV3() {
        return orderRepository.findAllWithItem().stream()
                .map(OrderDto::new)
                .toList();
    }



    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> orderV3_page(@RequestParam(value = "offeset", defaultValue = "0") int offset,
                                       @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<OrderDto> result = orderRepository.findAllWithMemberDelivery(offset, limit).stream()
                .map(OrderDto::new)
                .toList();
        return result;
    }
    // v4 - JPA에서 DTO 직접 조회
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> orderV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }
    // v5 - JPA에서 DTO 직접 조회 - 컬렉션 조회 최적화
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

    @Getter
    static class OrderDto {
        private Long orderId;
        private String name;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .toList();
        }
    }
    @Data
    static class OrderItemDto {
        private String itemName; // 상품명
        private int orderPrice; // 주문 가격
        private int count; // 주문 수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

}
