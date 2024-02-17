package com.kmhoon.mall.service.cart;

import com.kmhoon.mall.domain.cart.Cart;
import com.kmhoon.mall.domain.cart.CartItem;
import com.kmhoon.mall.domain.member.Member;
import com.kmhoon.mall.domain.product.Product;
import com.kmhoon.mall.dto.cart.CartItemDto;
import com.kmhoon.mall.dto.cart.CartItemListDto;
import com.kmhoon.mall.repository.cart.CartItemRepository;
import com.kmhoon.mall.repository.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j2
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public List<CartItemListDto> addOrModify(CartItemDto cartItemDto) {
        String email = cartItemDto.getEmail();
        Long pno = cartItemDto.getPno();
        int qty = cartItemDto.getQty();
        Long cino = cartItemDto.getCino();

        // 장바구니 아이템 번호가 있음 = 수량 변경
        if (cino != null) {
            CartItem cartItem = cartItemRepository.findById(cino).orElseThrow();
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);
            return getCartItems(email);
        }

        // 장바구니 아이템 번호가 없음
        Cart cart = getCart(email);
        // 해당 pno 아이템이 담겼는지 확인
        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);
        if(cartItem == null) {
            Product product = Product.builder().pno(pno).build();
            cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();
        } else {
            cartItem.changeQty(qty);
        }
        cartItemRepository.save(cartItem);

        return getCartItems(email);
    }

    private Cart getCart(String email) {
        return cartRepository.getCartOfMember(email).orElseGet(() -> {
            log.info("Cart of the member is not exist!!");
            Member member = Member.builder().email(email).build();
            Cart tempCart = Cart.builder().owner(member).build();
            return cartRepository.save(tempCart);
        });
    }

    public List<CartItemListDto> getCartItems(String email) {
        return cartItemRepository.getItemsOfCartDtoByEmail(email);
    }

    public List<CartItemListDto> remove(Long cino) {
        Long cno = cartItemRepository.getCartFromItem(cino);
        log.info("cart no: " + cno);
        cartItemRepository.deleteById(cino);
        return cartItemRepository.getItemsOfCartDtoByCart(cno);
    }
}
