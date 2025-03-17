package com.TheLa.models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class ProductModel implements Serializable {
    private Long productId;
    private Long categoryId;
    private String name;
    private String description;
    private Timestamp createDate;
    private Boolean status;
    private Boolean isActive;
    private Boolean isDelete;
    private List<ProductImageModel> listProductImageModelList;

    public List<ProductImageModel> getListProductImageModelList() {
        return listProductImageModelList;
    }

    public void setListProductImageModelList(List<ProductImageModel> listProductImageModelList) {
        this.listProductImageModelList = listProductImageModelList;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
