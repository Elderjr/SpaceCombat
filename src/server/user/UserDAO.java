package server.user;

public class UserDAO {

	
	public static User login(String username, String password){
		return new User(username.length(), username);
	}
}
