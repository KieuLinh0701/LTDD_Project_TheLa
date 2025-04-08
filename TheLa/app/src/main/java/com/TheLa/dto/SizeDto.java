package com.TheLa.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SizeDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("sizeId")
    private Long sizeId;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSizeId() {
        return sizeId;
    }

    public void setSizeId(Long sizeId) {
        this.sizeId = sizeId;
    }
}
