package han.jpa.webdev.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import han.jpa.webdev.domain.Member;
import han.jpa.webdev.repository.MemberRepository;

@SpringBootTest
@Transactional
class MemberServiceTest {

	@Autowired
	MemberService memberService;

	@Autowired
	MemberRepository memberRepository;

	@Test
	void 회원가입() {
		Member member = new Member();
		member.setName("KYLE");
		final Long savedId = memberService.join(member);
		assertThat(memberRepository.findOne(savedId)).isEqualTo(member);
	}

	@Test
	void 중복_회원_예외() {
		final Member member = new Member();
		member.setName("KYLE");
		final Member member1 = new Member();
		member1.setName("KYLE");
		memberService.join(member);
		assertThatThrownBy(() -> memberService.join(member1)).isInstanceOf(IllegalStateException.class);
	}
}