package com.retail.businesslogic;

import com.retail.commontypes.CategoryType;
import com.retail.commontypes.DiscountType;
import com.retail.commontypes.UserType;
import com.retail.model.DiscountBE;
import com.retail.model.ItemBE;
import com.retail.model.UserBE;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 *
 * The core logic of discount is handled here for applicable users and general fix sale discount (over 100: 5%
 * Employee: 30%, Affiliate: 10 %, customer: 5% for 2 year long.
 * Grossary Items do not entitle for sale.
 *
 *
 *
 *
 */
public class DiscountBL implements iDiscountBL {
    private DiscountType type;
    private BigDecimal discount;
    private BigDecimal netMultiples;
    private Integer months;
    private UserBE user;




    public DiscountBL(UserBE user,  BigDecimal netMultiples, BigDecimal discount, Integer months) {
        super();



        if(discount == null) {
            throw new IllegalArgumentException("discount is required");
        }
        if(user.getUserType() == null) {
            throw new IllegalArgumentException("userType is required");
        }
        if((netMultiples == null) || BigDecimal.ZERO.equals(netMultiples)) {
            throw new IllegalArgumentException("netMultiples is missing or invalid");
        }
        if(months == null) {
            throw new IllegalArgumentException("months is required");
        }
        // default to percentage

        this.type = DiscountType.PERCENTAGE;

        this.user = user;
        this.netMultiples=new BigDecimal(100);
        this.discount = discount;

    }
    /**
     * Determines if the current discount is applicable for the provided
     * category
     * @param category to check in the excluded categories set
     * @return true if the category is not excluded, false otherwise
     */
    protected boolean isCategoryApplicable(CategoryType category,Set<CategoryType> exclude) {
        return (exclude == null) || (category == null) ||  !exclude.contains(category) ;
    }

    /**
     * Validate the discountable
     * @param discountable to validate
     */
    protected void validate(DiscountBE discountable, ItemBE item) {
        if((discountable == null) || (item.getNet() == null)) {
            throw new IllegalArgumentException("discountable is missing or invalid");
        }
    }

    @Override
    public boolean isApplicable(DiscountBE discountable, ItemBE item) {

        if((item == null) || (item.getUser() == null)
                || (item.getUser().getJoiningDate() == null)) {

            throw new IllegalArgumentException("discountable is missing or invalid");
        }
        else if(discountable.getUserType()== UserType.EMPLOYEE && item.getUser().getUserType() == null){
            throw new IllegalArgumentException("discountable is missing or invalid");
        }
        if(discountable.getUserType() == null) {
            throw new IllegalArgumentException("userType is required");
        }

        // check if the category is excluded
        boolean applicable = isCategoryApplicable(item.getCategory(),discountable.getExclude());

        if(applicable) {
            // if netMultiples then first rule
            if(discountable.getUserType()== UserType.ALL){
                int compare = item.getNet().compareTo(netMultiples);
                // only applicable if the netPayable is equal to or greater than the netMultiples
                applicable = (compare == 0) || (compare == 1);
            }
            else if(discountable.getUserType()== UserType.CUSTOMER){
                // check if the number of months on this instance is smaller than the time the user
                // has been a customer
                // get a calendar date x months ago
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -discountable.getMonths());
                // start of the day
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                Date customerSince = item.getUser().getJoiningDate();

                applicable = customerSince.before(calendar.getTime());
            }
            else {

                // check if the userType on this instance matches the one on the discountable
                applicable = item.getUser().getUserType().equals(discountable.getUserType());
            }
        }

        return applicable;
    }
    @Override
    public BigDecimal calculate(DiscountBE discountable, ItemBE item) {
        BigDecimal amount = null;

        validate(discountable,item);

        if(discount == null) {
            throw new IllegalArgumentException("discount is null");
        }

        if(this.isApplicable(discountable,item)) {
            BigDecimal net = item.getNet();

            if(DiscountType.PERCENTAGE.equals(type)) {
                // percentage based
                //if NetMultiples then first rule
                if(discountable.getUserType()==UserType.ALL){
                    amount = item.getNet().divide(netMultiples, 0, RoundingMode.FLOOR);
                    amount = amount.multiply(getDiscount()).setScale(2, RoundingMode.HALF_UP);
                }
                else {
                    //discount = discountable.getAmount();
                    amount = discount.divide(new BigDecimal(100.00), 2, RoundingMode.HALF_UP).multiply(item.getNet());
                }

            } else if(DiscountType.AMOUNT.equals(type)) {
                // amount based

                amount = discount;

            } else {
                throw new IllegalArgumentException("invalid discountType: " + type);
            }

            amount = amount.setScale(2, RoundingMode.HALF_UP);

        }

        return amount;
    }

    /**
     * @return the discount amount
     *
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * @param discount, if percentage then this should be provided as a whole number i.e. 50 for 50%
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }



    /**
     * @return the type of the discount
     */
    public DiscountType getType() {
        return type;
    }

    /**
     * @param type the type of the discount
     */
    public void setType(DiscountType type) {
        this.type = type;
    }

    public BigDecimal getNetMultiples() {
        return netMultiples;
    }

    public void setNetMultiples(BigDecimal netMultiples) {
        this.netMultiples = netMultiples;
    }
    public UserBE getUser() {
        return user;
    }

    public void setUser(UserBE user) {
        this.user = user;
    }
}
