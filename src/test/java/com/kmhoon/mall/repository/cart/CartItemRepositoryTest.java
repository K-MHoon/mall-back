package com.kmhoon.mall.repository.cart;

import com.kmhoon.mall.domain.cart.Cart;
import com.kmhoon.mall.domain.cart.CartItem;
import com.kmhoon.mall.domain.member.Member;
import com.kmhoon.mall.domain.product.Product;
import com.kmhoon.mall.dto.cart.CartItemListDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class CartItemRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    @Commit
    @Test
    void testInsertByProduct() {
        String email = "user1@aaa.com";
        Long pno = 5L;
        int qty = 2;

        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);

        if(cartItem != null) {
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);
            return;
        }

        Optional<Cart> result = cartRepository.getCartOfMember(email);

        Cart cart = null;

        if(result.isEmpty()) {
            log.info("MemberCart is not exist!!");

            Member member = Member.builder().email(email).build();
            Cart tempCart = Cart.builder().owner(member).build();
            cart = cartRepository.save(tempCart);
        } else {
            cart = result.get();
        }
        log.info(cart);

        if(cartItem == null) {
            Product product = Product.builder().pno(pno).build();
            cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();
        }
        cartItemRepository.save(cartItem);
    }

    @Test
    @Commit
    void testUpdateByCino() {
        Long cino = 1L;
        int qty = 4;

        CartItem cartItem = cartItemRepository.findById(cino).orElseThrow();
        cartItem.changeQty(qty);
        cartItemRepository.save(cartItem);
    }

    @Test
    void testListOfMember() {
        String email = "user1@aaa.com";
        List<CartItemListDto> cartItemList = cartItemRepository.getItemsOfCartDtoByEmail(email);

        for (CartItemListDto dto : cartItemList) {
            log.info(dto);
        }
    }

    @Test
    void testDeleteThenList() {
        Long cino = 1L;

        // 장바구니 번호
        Long cno = cartItemRepository.getCartFromItem(cino);

        // 삭제는 임시로 주석처리
        // cartItemRepository.deleteById(cino);

        List<CartItemListDto> cartItemList = cartItemRepository.getItemsOfCartDtoByCart(cno);

        cartItemList.forEach(log::info);
    }
}