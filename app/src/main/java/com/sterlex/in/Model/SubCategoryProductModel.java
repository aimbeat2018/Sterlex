package com.sterlex.in.Model;

import java.util.ArrayList;

public class SubCategoryProductModel {
    String category_name,category_id,sub_cat_banner;
    ArrayList<ProductModel> productModelArrayList;
    ArrayList<CategoryModel> categoryModelArrayList;

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSub_cat_banner() {
        return sub_cat_banner;
    }

    public void setSub_cat_banner(String sub_cat_banner) {
        this.sub_cat_banner = sub_cat_banner;
    }

    public ArrayList<ProductModel> getProductModelArrayList() {
        return productModelArrayList;
    }

    public void setProductModelArrayList(ArrayList<ProductModel> productModelArrayList) {
        this.productModelArrayList = productModelArrayList;
    }

    public ArrayList<CategoryModel> getCategoryModelArrayList() {
        return categoryModelArrayList;
    }

    public void setCategoryModelArrayList(ArrayList<CategoryModel> categoryModelArrayList) {
        this.categoryModelArrayList = categoryModelArrayList;
    }
}
