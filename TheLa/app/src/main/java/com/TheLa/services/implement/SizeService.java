package com.TheLa.services.implement;

import androidx.lifecycle.ViewModel;

import com.TheLa.dao.implement.CategoryDao;
import com.TheLa.dao.implement.SizeDao;
import com.TheLa.models.CategoryModel;
import com.TheLa.models.SizeModel;
import com.TheLa.services.ICategoryService;
import com.TheLa.services.ISizeService;

import java.util.List;

public class SizeService extends ViewModel implements ISizeService {
    private final SizeDao sizeDao;

    public SizeService() {
        sizeDao = new SizeDao();
    }

    @Override
    public SizeModel findSizeBySizeId(Long sizeId) {
        return sizeDao.findSizeBySizeId(sizeId);
    }
}
