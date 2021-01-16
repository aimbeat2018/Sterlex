package com.sterlex.in.Model;

import java.util.ArrayList;

public class SlotModel {
    String slot_id;
    String day;
    String slot_timing;
    String disabled;
    boolean isSelected;

    ArrayList<SlotModel> slotArraylist;

    public ArrayList<SlotModel> getSlotArraylist() {
        return slotArraylist;
    }

    public void setSlotArraylist(ArrayList<SlotModel> slotArraylist) {
        this.slotArraylist = slotArraylist;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public String getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(String slot_id) {
        this.slot_id = slot_id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSlot_timing() {
        return slot_timing;
    }

    public void setSlot_timing(String slot_timing) {
        this.slot_timing = slot_timing;
    }
}
