package com.retail.model;

import com.retail.commontypes.CategoryType;
import com.retail.commontypes.DiscountType;
import com.retail.commontypes.UserType;

import java.math.BigDecimal;

public class DiscountBE {


    private UserBE user;



    // the due amount before discounts are applied
    private BigDecimal amount;

    // the bill's net after the discounts are applied
    private BigDecimal netPayable;

    public void setNetPayable(BigDecimal netPayable) {
        this.netPayable = netPayable;
    }



    private CategoryType category;

    private DiscountType discountType;

    private UserType userType;



    private Integer months;

    /**
     * Create a new order for a user with amount & category
     * @param discountType that the bill belongs to
     * @param amount the net total of the bill
     * @param userType the userType of the bill
     * @param months default month for customer type
     */
    public DiscountBE(DiscountType discountType, BigDecimal amount, UserType userType, Integer months) {
        super();

        if(amount == null) {
            throw new IllegalArgumentException("net is required");
        }

        if(discountType == null) {
            throw new IllegalArgumentException("discountType is required");
        }

        this.discountType = discountType;
        this.amount = amount;
        this.userType = userType;
        this.months = months;

    }

    public BigDecimal getNetPayable() {
        return netPayable;
    }

    public CategoryType getCategory() {
        return category;
    }

    public void setCategory(CategoryType category) {
        this.category = category;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Integer getMonths() {
        return months;
    }

    public void setMonths(Integer months) {
        this.months = months;
    }
    public UserBE getUser() {
        return user;
    }

    public void setUser(UserBE user) {
        this.user = user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
