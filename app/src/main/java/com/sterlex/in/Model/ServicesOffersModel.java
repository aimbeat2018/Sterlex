package com.sterlex.in.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class ServicesOffersModel implements Serializable {


    ArrayList<String> imagesArrayList;
    String section_text,offer_id, section_name,image,  status, position, view, section_background, section_icon;

    public String getImage() {
        return image;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getImagesArrayList() {


        return imagesArrayList;
    }

    public void setImagesArrayList(ArrayList<String> imagesArrayList) {
        this.imagesArrayList = imagesArrayList;
    }

    public String getSection_text() {
        return section_text;
    }

    public void setSection_text(String section_text) {
        this.section_text = section_text;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getSection_background() {
        return section_background;
    }

    public void setSection_background(String section_background) {
        this.section_background = section_background;
    }

    public String getSection_icon() {
        return section_icon;
    }

    public void setSection_icon(String section_icon) {
        this.section_icon = section_icon;
    }
}
