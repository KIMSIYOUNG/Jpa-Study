package han.jpa.webdev.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import han.jpa.webdev.domain.Delivery;
import han.jpa.webdev.domain.DeliveryStatus;
import han.jpa.webdev.domain.Member;
import han.jpa.webdev.domain.Order;
import han.jpa.webdev.domain.OrderItem;
import han.jpa.webdev.domain.item.Item;
import han.jpa.webdev.repository.MemberRepository;
import han.jpa.webdev.repository.OrderRepository;
import han.jpa.webdev.repository.OrderSearch;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
	private final OrderRepository orderRepository;
	private final MemberRepository memberRepository;
	private final ItemService itemService;

	public Long order(Long memberId, Long itemId, int count) {
		//엔티티 조회
		Member member = memberRepository.findOne(memberId);
		Item item = itemService.findOne(itemId);
		//배송정보 생성
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());
		delivery.setStatus(DeliveryStatus.READY);
		//주문상품 생성
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
		//주문 생성
		Order order = Order.createOrder(member, delivery, orderItem);
		//주문 저장
		orderRepository.save(order);
		return order.getId();
	}

	public void cancelOrder(Long orderId) {
		Order order = orderRepository.findOne(orderId);
		//주문 취소
		order.cancel();
	}

	public List<Order> findOrders(OrderSearch orderSearch) {
		return orderRepository.findAllByCriteria(orderSearch);
	}
}
