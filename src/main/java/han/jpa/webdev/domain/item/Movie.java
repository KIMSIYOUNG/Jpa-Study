package han.jpa.webdev.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;

@DiscriminatorValue("M")
@Getter
@Entity
public class Movie extends Item {
    private String director;
    private String actor;
}
