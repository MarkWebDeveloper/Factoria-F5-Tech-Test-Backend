package dev.mark.factoria_tech_test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "DATASOURCE_PASSWORD=1234",
	"DATASOURCE_USERNAME=name",
	"DEFAULT_ADMIN_NAME=name",
	"DEFAULT_ADMIN_PASSWORD=1234"
})
class FactoriaTechTestApplicationTests {

	@Test
	void contextLoads() {
	}

}
