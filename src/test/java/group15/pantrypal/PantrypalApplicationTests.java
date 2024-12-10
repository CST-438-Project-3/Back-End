package group15.pantrypal;

import group15.pantrypal.auth.UserAuthRepository;
import group15.pantrypal.auth.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@SpringBootTest
class PantrypalApplicationTests {

	@Test
	void contextLoads() {
	}
	@Test
	public void testPasswordMatch() {
		String rawPassword = "mypassword";
		String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);

		UserService userService = new UserService(mock(UserAuthRepository.class));
		boolean matches = userService.passwordMatch(rawPassword, encodedPassword);

		assertTrue(matches); // This should pass if the password encoding and matching work correctly
	}


}
