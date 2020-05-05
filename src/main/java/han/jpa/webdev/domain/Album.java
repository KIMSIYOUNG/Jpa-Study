package han.jpa.webdev.domain;

import javax.persistence.DiscriminatorValue;

@DiscriminatorValue("A")
public class Album extends Item {
    private String artist;
    private String etc;
}
