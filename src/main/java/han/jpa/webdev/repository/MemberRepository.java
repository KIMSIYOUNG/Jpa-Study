package han.jpa.webdev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import han.jpa.webdev.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
