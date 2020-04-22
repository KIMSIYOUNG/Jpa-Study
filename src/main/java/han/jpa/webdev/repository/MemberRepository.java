package han.jpa.webdev.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import han.jpa.webdev.domain.Member;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
	private final EntityManager manager;

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
