package org.antonioxocoy.cecommerce.service;

import org.antonioxocoy.cecommerce.dto.UserDTO;
import org.antonioxocoy.cecommerce.entity.User;
import org.antonioxocoy.cecommerce.exception.UserNotValidToRegisterException;
import org.antonioxocoy.cecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    public User registerUser(UserDTO userDto) throws UserNotValidToRegisterException {
        Optional<User> res = this.userRepository.findFirstByEmail(userDto.getEmail());
        if (res.isEmpty()) {
            User newUser = User.parse(userDto);
            return this.userRepository.save(newUser);
        } else {
            if (res.get().getEmail().equals(userDto.getEmail())) {
                throw new UserNotValidToRegisterException("Already exist a user with the email " + userDto.getEmail());
            } else if (res.get().getIsBlocked()) {
                throw new UserNotValidToRegisterException("User with email " + userDto.getEmail() + " was blocked, contact to support");
            } else {
                User pastUser = res.get();
                pastUser.setDeleted(false);
                User.parseAndJoin(userDto, pastUser);
                return this.userRepository.save(pastUser);
            }
        }
    }
}
