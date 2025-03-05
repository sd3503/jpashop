package jpabook.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * 총 주문 2개가 만들어져야 함
 * // 주문1
 *  * userA
 *      * JPA1 BOOK
 *      * JPA2 BOOK
 * // 주문2
 *  * userB
 *      * SPRING1 BOOK
 *      * SPRING2 BOOK
 * */

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        public void dbInit1() {
            Member member = createMember("userA", "서울", "1", "11111");
            em.persist(member);

            Book book1 = new Book();
            book1.setName("JPA1 Book");
            book1.setAuthor("JPA1 Author");
            book1.setPrice(10000);
            book1.setStockQuantity(100);
            em.persist(book1);

            Book book2 = new Book();
            book2.setName("JPA2 Book");
            book2.setAuthor("JPA2 Author");
            book2.setPrice(20000);
            book2.setStockQuantity(100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());

            Order  order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);



        }
        private static Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }
        public void dbInit2() {
            Member member = createMember("userB", "부산", "2", "22222");
            em.persist(member);

            Book book1 = new Book();
            book1.setName("SPRING1 Book");
            book1.setAuthor("SPRING1 Author");
            book1.setPrice(20000);
            book1.setStockQuantity(200);
            em.persist(book1);

            Book book2 = new Book();
            book2.setName("SPRING2 Book");
            book2.setAuthor("SPRING2 Author");
            book2.setPrice(40000);
            book2.setStockQuantity(300);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());

            Order  order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);



        }

    }
}
