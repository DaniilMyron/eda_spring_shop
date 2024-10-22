package com.miron.carting.controllers;

import com.miron.carting.domain.Cart;
import com.miron.carting.services.impl.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/get-cart")
    public ResponseEntity<Cart> getCart(@RequestBody int cartId){
        Cart cart = cartService.findCart(cartId);
        return ResponseEntity.ok().body(cart);
    }

    @GetMapping("/get-auth")
    public ResponseEntity<Object> getAuthorizationToken(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(auth.getPrincipal());
    }

    @PostMapping("/hello")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello!");
    }
}
