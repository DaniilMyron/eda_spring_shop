package com.miron.product.controllers;

import com.miron.product.controllers.api.ProductResponse;
import com.miron.product.services.IListenerService;
import com.miron.product.services.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @PostMapping("/cart/{product-id}")
    public ResponseEntity<ProductResponse> cartProduct(
            @PathVariable("product-id") int id,
            @RequestParam(name = "count", required = true) int count,
            Authentication authentication
    ){
        return ResponseEntity.ok().body(productService.findProductAndCart(id, count, authentication));
    }


    @PostMapping("/hello")
    public ResponseEntity<Object> sayHello(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JSONObject userJsonObject = new JSONObject(auth.getPrincipal());
        return ResponseEntity.ok(userJsonObject.getString("username"));
    }
    @PostMapping("/secured-hello")
    public ResponseEntity<Object> saySecuredHello(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(auth.getPrincipal());
    }
    @GetMapping("/hello2")
    public ResponseEntity<String> sayGetHello(){
        return ResponseEntity.ok("Hello!");
    }

}
