package com.retail.model;

import com.retail.commontypes.CategoryType;
import com.retail.commontypes.DiscountType;
import com.retail.commontypes.UserType;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Discount entity file that holds key information relevant for mapping state and for calculating discount.
 *
 *
 *
 *
 */
public class DiscountBE {


    private UserBE user;



    // the due amount before discounts are applied
    private BigDecimal amount;


    private CategoryType category;

    private DiscountType discountType;

    private UserType userType;

    private Integer months;


    private Set<CategoryType> exclude;

    /**
     * Create a new order for a user with amount & category
     * @param discountType : PERCENTAGE,AMOUNT
     * @param amount the net total of the bill
     * @param userType :EMPLOYEE,AFFILIATE,CUSTOMER, ALL: denote common rules applicable to all 3 types.
     *
     * @param months default month for customer type: 24 minimum to be eligible for discount for "CUSTOMER" user type
     */
    public DiscountBE(DiscountType discountType, BigDecimal amount, UserType userType, Integer months, Set<CategoryType> exclude) {
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
        this.exclude = exclude;
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

    public Set<CategoryType> getExclude() {
        return exclude;
    }

    public void setExclude(Set<CategoryType> exclude) {
        this.exclude = exclude;
    }
}
