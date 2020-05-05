package han.jpa.webdev.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import han.jpa.webdev.dto.req.MemberRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberController memberController;

    @Test
    void createTest() {
        MemberRequestDto dto = new MemberRequestDto("DDD");
        mockMvc.perform(post("http://localhost:8080/member/save")
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect()

        Long save = memberController.save(dto);
        assertThat(save).isEqualTo(1);
    }
}