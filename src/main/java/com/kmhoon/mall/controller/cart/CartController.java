package com.kmhoon.mall.controller.cart;

import com.kmhoon.mall.dto.cart.CartItemDto;
import com.kmhoon.mall.dto.cart.CartItemListDto;
import com.kmhoon.mall.service.cart.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

//    @PreAuthorize("#itemDto.email == authentication.name")
    @PostMapping("/change")
    public List<CartItemListDto> changeCart(@RequestBody @Valid CartItemDto itemDto) {
        log.info(itemDto);

        if(itemDto.getQty() <= 0) {
            return cartService.remove(itemDto.getCino());
        }

        return cartService.addOrModify(itemDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/items")
    public List<CartItemListDto> getCartItems(Principal principal) {
        String email = principal.getName();
        log.info("email: " + email);
        return cartService.getCartItems(email);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{cino}")
    public List<CartItemListDto> removeFromCart(@PathVariable("cino") Long cino) {
        log.info("cart item no: " + cino);
        return cartService.remove(cino);
    }
}
