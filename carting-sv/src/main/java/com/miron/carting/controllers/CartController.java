package com.miron.carting.controllers;

import com.miron.carting.controllers.model.PageResponse;
import com.miron.carting.controllers.model.ProductsInCartResponse;
import com.miron.carting.services.ICartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/carts")
@RequiredArgsConstructor
public class CartController {
    private final ICartService cartService;

    @PostMapping("/buy")
    public ResponseEntity<PageResponse<ProductsInCartResponse>> buyAll(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication auth
    ){
        return ResponseEntity.ok().body(cartService.checkBalance(auth, page, size));
    }

    @PostMapping("/buy/{id}")
    public ResponseEntity<ProductsInCartResponse> buySingle(
            @PathVariable int id,
            Authentication auth
    ){
        return ResponseEntity.ok().body(cartService.checkBalance(auth, id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductsInCartResponse>> checkCart(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication auth
    ){
        return ResponseEntity.ok().body(cartService.findAllProductsInCart(auth, page, size));
    }

    @PostMapping("/hello")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello!");
    }
}
