package han.jpa.webdev.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import han.jpa.webdev.domain.Member;
import han.jpa.webdev.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	@Transactional
	public Long join(Member member) {
		validateDuplicatedMember(member);
		memberRepository.save(member);
		return member.getId();
	}

	private void validateDuplicatedMember(Member member) {
		List<Member> members = memberRepository.findByName(member.getName());
		if (!members.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}

	public List<Member> findMembers() {
		return memberRepository.findAll();
	}

	public Member findOne(Long id) {
		return memberRepository.findOne(id);
	}
}
