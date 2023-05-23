package org.antonioxocoy.cecommerce.service;

import org.antonioxocoy.cecommerce.config.LocalDBTesting;
import org.antonioxocoy.cecommerce.dto.UserDTO;
import org.antonioxocoy.cecommerce.entity.User;
import org.antonioxocoy.cecommerce.exception.UserNotValidToRegisterException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceTest extends LocalDBTesting {

    @Autowired
    private UserService userService;

    public UserServiceTest() {
        super();
        classes.add(User.class);
    }

    @Test
    public void testCanRegisterUser() throws UserNotValidToRegisterException {
        UserDTO userTemp = new UserDTO("Antonio Xocoy", "anxrosales@gmail.com", "123", "admin");
        User newUser = this.userService.registerUser(userTemp);
        Assert.assertEquals(userTemp.getEmail(), newUser.getEmail());
        Assert.assertTrue(newUser.getId() != null && !newUser.getId().isEmpty());
    }
}
