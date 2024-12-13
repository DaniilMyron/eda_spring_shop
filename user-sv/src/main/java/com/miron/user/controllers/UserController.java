package com.miron.user.controllers;

import com.miron.user.controllers.api.RegistrationRequest;
import com.miron.user.controllers.api.UserResponse;
import com.miron.user.services.IListenerService;
import com.miron.user.services.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @PostMapping("/auth")
    public ResponseEntity<String> authorization(){
        return ResponseEntity.ok("Authorization confirmed");
    }

    @GetMapping("/get-auth")
    public ResponseEntity<Object> getAuthorizationToken(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(auth.getPrincipal());
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registration(
            @Valid @RequestBody RegistrationRequest request
    ){
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/replenish")
    public ResponseEntity<UserResponse> replenishBalance(
            @RequestParam(name = "sum", required = true) int sum,
            Authentication auth
    ){
        return ResponseEntity.ok().body(userService.replenishBalance(auth, sum));
    }
}
