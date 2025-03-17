package com.TheLa.services.implement;

import androidx.lifecycle.ViewModel;

import com.TheLa.dao.implement.ProductSizeDao;
import com.TheLa.dao.implement.SizeDao;
import com.TheLa.models.ProductModel;
import com.TheLa.models.ProductSizeModel;
import com.TheLa.models.SizeModel;
import com.TheLa.services.IProductSizeService;
import com.TheLa.services.ISizeService;

import java.util.List;

public class ProductSizeService extends ViewModel implements IProductSizeService {
    private final ProductSizeDao productSizeDao;

    public ProductSizeService() {
        productSizeDao = new ProductSizeDao();
    }

    @Override
    public List<ProductSizeModel> findProductSizeByProduct(ProductModel product) {
        return productSizeDao.findProductSizeByProduct(product);
    }
}
