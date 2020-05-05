package han.jpa.webdev.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequestDto {
    private String name;
    
    public MemberRequestDto(String name) {
        this.name = name;
    }
}
