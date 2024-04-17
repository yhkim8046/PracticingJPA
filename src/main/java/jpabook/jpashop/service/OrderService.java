package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * Order
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){

        //Entity Search
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //Creating delivering information
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //Creating item to Order
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //Creating Order
        Order order = Order.createOrder(member,delivery,orderItem);

        //Storing order
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * Order cancellation
     */
    @Transactional
    public void cancelOrder(Long orderId){
        //search entity
        Order order = orderRepository.findOne(orderId);
        //cancel
        order.cancel();
    }

    //Order searching
    public List<Order> findOrders(OrderSearch orderSearch){
       return orderRepository.findAll(orderSearch);
    }


}
