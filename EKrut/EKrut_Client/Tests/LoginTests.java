import static org.junit.jupiter.api.Assertions.*;
import controllerGui.LoginController;
import entity.UserEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginTests {
	UserEntity user1;
	UserEntity user2;

	@BeforeEach
	void setUp() throws Exception {

//		public UserEntity(int id, String username, String password, String first_name, String last_name, String email,
//				String phone_number, String role_type, boolean logged_in) {
		user1 = new UserEntity(0, "", "", "", "", "", "", "");
	}

	// ----invalid username, valid pass -----
	
	
	@Test
	void SuccessLogin() {
		user1.setUsername("my_user");
		user1.setPassword("12345678");
		AssertTrue()
	}
	
	
	@Test
	void test() {
		fail("Not yet implemented");
	}

}
