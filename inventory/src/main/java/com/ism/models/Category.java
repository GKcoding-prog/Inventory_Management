package com.ism.models;

public class Category {
    private long catId;
    private String nameCat;

    public Category(long catId, String nameCat) {
        this.catId = catId;
        this.nameCat = nameCat;
    }

    public long getCatId() { return catId; }
    public void setCatId(long catId) { this.catId = catId; }

    public String getNameCat() { return nameCat; }
    public void setNameCat(String nameCat) { this.nameCat = nameCat; }
}
