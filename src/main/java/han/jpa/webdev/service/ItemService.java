package han.jpa.webdev.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import han.jpa.webdev.domain.item.Item;
import han.jpa.webdev.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
	private final ItemRepository itemRepository;

	@Transactional
	public void saveItem(Item item) {
		itemRepository.save(item);
	}

	public List<Item> findItems() {
		return itemRepository.findAll();
	}

	public Item findOne(Long itemId) {
		return itemRepository.findOne(itemId);
	}

	@Transactional
	public void update(Long itemId, String name, int price, int stockQuantity) {
		Item item = itemRepository.findOne(itemId);
		item.change(name, price, stockQuantity);
	}
}
