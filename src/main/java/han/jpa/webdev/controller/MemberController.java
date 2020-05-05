package han.jpa.webdev.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import han.jpa.webdev.domain.Member;
import han.jpa.webdev.dto.req.MemberRequestDto;
import han.jpa.webdev.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/save")
    public Long save(@RequestBody MemberRequestDto request) {
        Member member = Member.builder().name(request.getName()).build();
        return memberService.save(member);
    }
}
