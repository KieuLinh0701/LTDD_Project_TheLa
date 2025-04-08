package com.TheLa.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductSizeDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("productSizeId")
    private Long productSizeId;
    @SerializedName("size")
    private SizeDto size;
    @SerializedName("price")
    private BigDecimal price;
    @SerializedName("product")
    private ProductDto product;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getProductSizeId() {
        return productSizeId;
    }

    public void setProductSizeId(Long productSizeId) {
        this.productSizeId = productSizeId;
    }

    public SizeDto getSize() {
        return size;
    }

    public void setSize(SizeDto size) {
        this.size = size;
    }

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }
}
