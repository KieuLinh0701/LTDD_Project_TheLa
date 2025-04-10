package com.TheLa.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CartItemDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("cartItemId")
    private Long cartItemId;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("cart")
    private CartDto cart;
    @SerializedName("productSize")
    private ProductSizeDto productSize;

    public CartDto getCart() {
        return cart;
    }

    public void setCart(CartDto cart) {
        this.cart = cart;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public ProductSizeDto getProductSize() {
        return productSize;
    }

    public void setProductSize(ProductSizeDto productSize) {
        this.productSize = productSize;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
