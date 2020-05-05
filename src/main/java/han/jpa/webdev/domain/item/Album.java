package han.jpa.webdev.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;

@DiscriminatorValue("A")
@Getter
@Entity
public class Album extends Item {
    private String artist;
    private String etc;
}
