package servie.track_servie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import servie.track_servie.entity.User;
import servie.track_servie.repository.UserRepository;

@Service
public class UserService
{
    @Autowired
    private UserRepository userRepository;

    public User register(User user)
    {
        return userRepository.save(user);
    }
}
