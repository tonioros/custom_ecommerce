package org.antonioxocoy.cecommerce.controller;

import org.antonioxocoy.cecommerce.dto.UserDTO;
import org.antonioxocoy.cecommerce.exception.UserNotValidToRegisterException;
import org.antonioxocoy.cecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    public UserService userService;

    @PostMapping("/global/login")
    public ResponseEntity<?> login(@RequestBody UserDTO tempUser) {
        try {
            return ResponseEntity.ok(
                    this.userService.registerUser(tempUser)
            );
        } catch (UserNotValidToRegisterException e) {
            return ResponseEntity.badRequest().body(
                    e.getMessage()
            );
        }
    }


    @PostMapping("/global/register")
    public ResponseEntity<?> register(@RequestBody UserDTO tempUser) {
        try {
            return ResponseEntity.ok(this.userService.registerUser(tempUser));
        } catch (UserNotValidToRegisterException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
