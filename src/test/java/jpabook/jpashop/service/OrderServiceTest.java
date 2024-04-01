package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository repository;


    @Test
    public void order() throws Exception{
        Member member = new Member();
        member.setName("UserA");
        member.setAddress(new Address("Auckland","CityCentre","1010"));
        em.persist(member);

        Item book = new Book();
        book.setName("Harry Potter");
        book.setPrice(24);
        book.setStockQuantity(10);

        int orderCount = 2;

        int overQuantityOrder = 11;

        em.persist(book);

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        Long failedOrderId = orderService.order(member.getId(), book.getId(), overQuantityOrder);

        Order getOrder = repository.findOne(orderId);
        Order failedOrder = repository.findOne(failedOrderId);

        assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(getOrder.getOrderItems().size()).isEqualTo(1);
        assertThat(getOrder.getTotalPrice()).isEqualTo(24*orderCount);
        assertThat(book.getStockQuantity()).isEqualTo(8);

        //expected fail to test
        assertThat(failedOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
    }

    @Test
    public void cancel() throws Exception{
        Member member = new Member();
        member.setName("UserA");
        member.setAddress(new Address("Auckland","CityCentre","1010"));
        em.persist(member);

        Item book = new Book();
        book.setName("Harry Potter");
        book.setPrice(24);
        book.setStockQuantity(10);

        int orderCount = 2;
        em.persist(book);


        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        orderService.cancelOrder(orderId);

        Order getOrder = repository.findOne(orderId);

        assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(book.getStockQuantity()).isEqualTo(10);
    }
}