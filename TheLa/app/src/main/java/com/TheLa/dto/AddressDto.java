package com.TheLa.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddressDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("addressId")
    private Long addressId;
    @SerializedName("city")
    private String city;
    @SerializedName("district")
    private String district;
    @SerializedName("commune")
    private String commune;
    @SerializedName("detail")
    private String detail;

    public AddressDto(String city, String district, String commune, String detail) {
        this.city = city;
        this.commune = commune;
        this.detail = detail;
        this.district = district;
    }

    public AddressDto(Long addressId, String city, String district, String commune, String detail) {
        this.addressId = addressId;
        this.city = city;
        this.commune = commune;
        this.detail = detail;
        this.district = district;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
