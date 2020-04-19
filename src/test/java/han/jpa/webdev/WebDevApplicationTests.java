package han.jpa.webdev;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebDevApplicationTests {

	@Test
	void contextLoads() {
		final Hello hello = new Hello();
		hello.setName("hello");
		assertThat(hello.getName()).isEqualTo("hello");
	}

}
