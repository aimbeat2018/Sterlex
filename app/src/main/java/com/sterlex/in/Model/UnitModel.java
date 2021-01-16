package com.sterlex.in.Model;

public class UnitModel {
    String id, unit,
            uint_price,
            unit_discount,
            unit_purchase_price,
            unit_gst,
            unit_qty,
            save_id;

    public String getUnit_qty() {
        return unit_qty;
    }

    public void setUnit_qty(String unit_qty) {
        this.unit_qty = unit_qty;
    }

    public String getSave_id() {
        return save_id;
    }

    public void setSave_id(String save_id) {
        this.save_id = save_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUint_price() {
        return uint_price;
    }

    public void setUint_price(String uint_price) {
        this.uint_price = uint_price;
    }

    public String getUnit_discount() {
        return unit_discount;
    }

    public void setUnit_discount(String unit_discount) {
        this.unit_discount = unit_discount;
    }

    public String getUnit_purchase_price() {
        return unit_purchase_price;
    }

    public void setUnit_purchase_price(String unit_purchase_price) {
        this.unit_purchase_price = unit_purchase_price;
    }

    public String getUnit_gst() {
        return unit_gst;
    }

    public void setUnit_gst(String unit_gst) {
        this.unit_gst = unit_gst;
    }

    /*  "unit": "1Kg",
                                "uint_price": "25",
                                "unit_discount": "3",
                                "unit_purchase_price": "20",
                                "unit_gst": "0"*/
}
