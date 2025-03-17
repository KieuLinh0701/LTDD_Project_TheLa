package com.TheLa.services;

import com.TheLa.models.ProductModel;
import com.TheLa.models.ProductSizeModel;

import java.util.List;

public interface IProductSizeService {
    List<ProductSizeModel> findProductSizeByProduct(ProductModel product);
}
