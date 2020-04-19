package han.jpa.webdev.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import han.jpa.webdev.domain.Member;

@Repository
public class MemberRepository {

	@PersistenceContext
	private EntityManager manager;

	public void save(Member member) {
		manager.persist(member);
	}

	public Member findOne(Long id) {
		return manager.find(Member.class, id);
	}

	public List<Member> findAll() {
		return manager.createQuery("select m from Member m", Member.class)
			.getResultList();
	}

	public List<Member> findByName(String name) {
		return manager.createQuery("select m from Member m where m.name = :name", Member.class)
			.setParameter("name", name)
			.getResultList();
	}
}
