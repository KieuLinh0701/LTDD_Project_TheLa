package com.TheLa.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CartDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("cartId")
    private Long cartId;
    @SerializedName("cartItems")
    private List<CartItemDto> cartItems;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public List<CartItemDto> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDto> cartItems) {
        this.cartItems = cartItems;
    }
}
