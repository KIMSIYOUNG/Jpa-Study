package han.jpa.webdev.domain;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Embeddable
@NoArgsConstructor
public class Address {
    private String city;
    private String street;
    private String zipCode;
}
