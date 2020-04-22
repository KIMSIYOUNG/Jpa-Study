package han.jpa.webdev.repository;

import han.jpa.webdev.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {
	private String memberName;
	private OrderStatus orderStatus;
}
