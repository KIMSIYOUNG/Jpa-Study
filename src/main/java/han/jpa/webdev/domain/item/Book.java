package han.jpa.webdev.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;

@DiscriminatorValue("B")
@Getter
@Entity
public class Book extends Item {
    private String author;
    private String isbn;
}
