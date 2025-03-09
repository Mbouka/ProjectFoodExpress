package com.masai.api;

import com.masai.model.UpdateUserRequest;
import com.masai.model.User;
import com.masai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserRestController {
    @Autowired
    private UserService userService;

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest updateUserRequest,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        User updatedUser = userService.updateUser(userDetails.getUsername(), updateUserRequest);
        return ResponseEntity.ok(updatedUser);
    }


}
