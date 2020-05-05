package han.jpa.webdev.dto.req;

import lombok.Getter;

@Getter
public class MemberRequestDto {
    private String name;

    public MemberRequestDto(String name) {
        this.name = name;
    }
}
