package com.retail.model;

import com.retail.commontypes.CategoryType;

import java.math.BigDecimal;

public class ItemBE {

    private UserBE user;

    // the bill's net before discounts are applied
    private BigDecimal net;

    private CategoryType category;

    public ItemBE(UserBE user, BigDecimal net,CategoryType category){
        this.user=user;
        this.net = net;
        this.category=category;
    }

    public UserBE getUser() {
        return user;
    }

    public void setUser(UserBE user) {
        this.user = user;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public CategoryType getCategory() {
        return category;
    }

    public void setCategory(CategoryType category) {
        this.category = category;
    }


}
