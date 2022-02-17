package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {

        //given
        Member member = createMember();
        Book book = createBook("도서", 10000, 10);

        int orderCount = 10;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order result = orderRepository.findById(orderId);

        assertThat(result.getId()).isEqualTo(orderId);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(result.getOrderItems().size()).isEqualTo(1);
        assertThat(result.getTotalPrice()).isEqualTo(book.getPrice() * orderCount);
        assertThat(book.getStockQuantity()).isEqualTo(0);


    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","도로","123123"));
        em.persist(member);
        return member;
    }

    @Test
    public void 상품주문_재고수량_초과() throws Exception {

        //given
        Member member = createMember();
        Book book = createBook("도서", 10000, 10);


        //when
        int orderCount = 11;

        //then
        org.junit.jupiter.api.Assertions.assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), book.getId(), orderCount));
    }

    @Test
    public void 주문취소() throws Exception {

        //given
        Book book = createBook("adsf", 10000, 10);
        Member member = createMember();

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findById(orderId);

        assertThat(book.getStockQuantity()).isEqualTo(10);
        assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);

    }

}