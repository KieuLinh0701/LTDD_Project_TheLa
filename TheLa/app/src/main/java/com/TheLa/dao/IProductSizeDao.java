package com.TheLa.dao;

import com.TheLa.models.ProductModel;
import com.TheLa.models.ProductSizeModel;
import com.TheLa.models.SizeModel;

import java.util.List;

public interface IProductSizeDao {
    List<ProductSizeModel> findProductSizeByProduct(ProductModel product);
}
