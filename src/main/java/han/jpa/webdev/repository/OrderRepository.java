package han.jpa.webdev.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import han.jpa.webdev.domain.Member;
import han.jpa.webdev.domain.Order;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
	private final EntityManager manager;

	public void save(Order order) {
		manager.persist(order);
	}

	public Order findOne(Long orderId) {
		return manager.find(Order.class, orderId);
	}

	public List<Order> findAllByCriteria(OrderSearch orderSearch) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		Root<Order> o = cq.from(Order.class);
		Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
		List<Predicate> criteria = new ArrayList<>();
		//주문 상태 검색
		if (orderSearch.getOrderStatus() != null) {
			Predicate status = cb.equal(o.get("status"),
				orderSearch.getOrderStatus());
			criteria.add(status);
		}
		//회원 이름 검색
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			Predicate name =
				cb.like(m.<String>get("name"), "%" +
					orderSearch.getMemberName() + "%");
			criteria.add(name);
		}
		cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
		TypedQuery<Order> query = manager.createQuery(cq).setMaxResults(1000);
		return query.getResultList();
	}
}
