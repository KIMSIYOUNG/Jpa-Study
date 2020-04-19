package han.jpa.webdev;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

	@PersistenceContext
	private EntityManager manager;

	public Long save(Member member) {
		manager.persist(member);
		return member.getId();
	}

	public Member find(Long id) {
		return manager.find(Member.class, id);
	}
}
