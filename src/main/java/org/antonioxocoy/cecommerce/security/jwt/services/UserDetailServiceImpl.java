package org.antonioxocoy.cecommerce.security.jwt.services;

import org.antonioxocoy.cecommerce.security.jwt.UserDetailsImpl;
import org.antonioxocoy.cecommerce.models.entity.User;
import org.antonioxocoy.cecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByEmail(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User with email " + username + " not found")
                );
        return new UserDetailsImpl(user);
    }
}
