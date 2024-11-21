package tech.farchettiensis.lms.service;

import tech.farchettiensis.lms.model.User;

public interface UserService {
    User registerUser(User user);

    User findByEmail(String email);
}
