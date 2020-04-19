package han.jpa.webdev.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import han.jpa.webdev.domain.item.Item;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ItemRepository {
	private final EntityManager manager;

	public void save(Item item) {
		saveWIthIdNull(item);
		manager.merge(item);
	}

	private void saveWIthIdNull(Item item) {
		if (item.getId() == null) {
			manager.persist(item);
		}
	}

	public Item findOne(Long id) {
		return manager.find(Item.class, id);
	}

	public List<Item> findAll() {
		return manager.createQuery("select i from Item i", Item.class)
			.getResultList();
	}
}
