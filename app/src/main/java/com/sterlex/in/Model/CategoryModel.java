package com.sterlex.in.Model;

public class CategoryModel {

    String category_id;
    String category_name;
    String image;
    String subcategorey;
    String color;
    String position;
    String mainCategory_id;
    String sub_category;

    public String getMainCategory_id() {
        return mainCategory_id;
    }

    public void setMainCategory_id(String mainCategory_id) {
        this.mainCategory_id = mainCategory_id;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSubcategorey() {
        return subcategorey;
    }

    public void setSubcategorey(String subcategorey) {
        this.subcategorey = subcategorey;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
