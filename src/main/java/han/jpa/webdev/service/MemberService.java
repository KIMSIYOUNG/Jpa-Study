package han.jpa.webdev.service;

import org.springframework.stereotype.Service;

import han.jpa.webdev.domain.Member;
import han.jpa.webdev.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Long save(Member member) {
        return memberRepository.save(member).getId();
    }
}
