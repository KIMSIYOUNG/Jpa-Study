package han.jpa.webdev.domain;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Embeddable
@RequiredArgsConstructor
public class Address {
    private final String city;
    private final String street;
    private final String zipCode;
}
