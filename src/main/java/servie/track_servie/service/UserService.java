package servie.track_servie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import servie.track_servie.entity.User;
import servie.track_servie.repository.UserRepository;

@Service
public class UserService
{
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public void register(User user)
	{
		user.setRole("USER");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}

	public User findByUsername(String name)
	{
		return userRepository.findByEmail(name).orElse(null);
	}
}
