package com.sterlex.in.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductModel implements Serializable {
    String product_id;
    String product_name;
    String image;
    String flag;
    String unit;
    String price;
    String discount;
    String qty;
    String spi;
    String subscribe_status;
    String type;
    String total_amt;
    String price_id,is_veg;
    String is_in_wishlist;
    String save_id;

    public String getSave_id() {
        return save_id;
    }

    public void setSave_id(String save_id) {
        this.save_id = save_id;
    }

    public String getIs_in_wishlist() {
        return is_in_wishlist;
    }

    public void setIs_in_wishlist(String is_in_wishlist) {
        this.is_in_wishlist = is_in_wishlist;
    }

    public String getIs_veg() {
        return is_veg;
    }

    public void setIs_veg(String is_veg) {
        this.is_veg = is_veg;
    }

    ArrayList<UnitModel> product_unitArraylist;

    public ArrayList<UnitModel> getProduct_unitArraylist() {
        return product_unitArraylist;
    }

    public void setProduct_unitArraylist(ArrayList<UnitModel> product_unitArraylist) {
        this.product_unitArraylist = product_unitArraylist;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    String gst;

    public String getPrice_id() {
        return price_id;
    }

    public void setPrice_id(String price_id) {
        this.price_id = price_id;
    }

    public String getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(String total_amt) {
        this.total_amt = total_amt;
    }

    public String getSubscribe_status() {
        return subscribe_status;
    }

    public void setSubscribe_status(String subscribe_status) {
        this.subscribe_status = subscribe_status;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getSpi() {
        return spi;
    }

    public void setSpi(String spi) {
        this.spi = spi;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
