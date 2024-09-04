package org.uma.uma.service;


import org.uma.uma.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public void saveUser(User user) {
        // Save user to database
    }

    public User getUserById(Long userId) {
        // Retrieve user from database by id
        return null;
    }

    public void deleteUser(Long userId) {
        // Delete user from database by id
    }

    public void updateUser(User user) {
        // Update user in database
    }

    public User getUserByUsername(String username) {
        // Retrieve user from database by username
        return null;
    }
}
