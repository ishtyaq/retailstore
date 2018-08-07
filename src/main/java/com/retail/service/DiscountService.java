package com.retail.service;

import com.retail.businesslogic.DiscountBL;
import com.retail.commontypes.CategoryType;
import com.retail.model.DiscountBE;
import com.retail.model.UserBE;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Discount Service to be callable by external programs.
 * It can act as single point of contact rendering all applciable discount services.
 *
 *
 *
 */
public class DiscountService implements iDiscountService {


    private UserBE user;

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    // the bill's net before discounts are applied
    private BigDecimal net;

    // the bill's net after the discounts are applied
    private BigDecimal netPayable;

    public CategoryType getCategory() {
        return category;
    }

    public void setCategory(CategoryType category) {
        this.category = category;
    }

    private CategoryType category;

    // discounts that will always be applied
    private List<DiscountBE> alwaysApplicableDiscounts;

    // discounts that are mutually exclusive, if one is applied,
    // the rest won't
    private List<DiscountBE> mutuallyExclusiveDiscounts;


    /**
     * Create a new bill for a user with net and category
     * @param user that the bill belongs to
     * @param net the net total of the bill
     * @param category the category of the bill
     */
    public DiscountService(UserBE user, BigDecimal net, CategoryType category) {
        super();

        if(net == null) {
            throw new IllegalArgumentException("net is required");
        }

        if(user == null) {
            throw new IllegalArgumentException("user is require");
        }

        this.user = user;
        this.net = net;
        this.category = category;
    }

    /**
     * Apply the discounts attached to this instance and return
     * the net after the discounts have been applied
     * @return the net after the discounts are applied
     */
    @Override
    public BigDecimal applyDiscounts() {

        if(this.net == null) {
            throw new IllegalArgumentException("net is required");
        }

        // start of with netPayable equals to net
        netPayable = net;

        BigDecimal discountAmount = null;


        DiscountBL discountBL = new DiscountBL(user, user.getUserType(),null,new BigDecimal(100), new BigDecimal(5),24);
        discountBL.setUserType(user.getUserType());
        // apply the mutually exclusive discounts first, they are applied in the order they
        // are inserted into the ArrayList. We could extend this approach to explicitly set the order
        // in the discount based on an instance variable
        if((mutuallyExclusiveDiscounts != null) && !mutuallyExclusiveDiscounts.isEmpty()) {

            for(DiscountBE discount: mutuallyExclusiveDiscounts) {
                discount.setNetPayable(this.net);
                discount.setUser(this.user);
                discountBL.setDiscount(discount.getAmount());
                discount.setCategory(this.category);
                discountAmount = discountBL.calculate(discount);

                if(discountAmount != null) {
                    // one discount was applied now exit
                    break;
                }
            }

            // if any discount was applied then take it off the netPayable
            if(discountAmount != null) {
                netPayable = netPayable.subtract(discountAmount);
            }
        }

        // apply the always applicable
        if((alwaysApplicableDiscounts != null) && !alwaysApplicableDiscounts.isEmpty()) {

            for(DiscountBE discount: alwaysApplicableDiscounts) {
                discount.setNetPayable(netPayable);
                discount.setUser(this.user);
                discountBL.setDiscount(discount.getAmount());

                discountAmount = discountBL.calculate(discount);

                if(discountAmount != null) {
                    // apply it
                    netPayable = netPayable.subtract(discountAmount);
                }
            }
        }

        return netPayable;
    }

    /**
     * @return the alwaysApplicable
     */
    public List<DiscountBE> getAlwaysApplicable() {
        return alwaysApplicableDiscounts;
    }

    /**
     * @param alwaysApplicable the alwaysApplicable to set
     */
    public void setAlwaysApplicable(List<DiscountBE> alwaysApplicable) {
        this.alwaysApplicableDiscounts = alwaysApplicable;
    }

    /**
     * @return the mutuallyExclusive
     */
    public List<DiscountBE> getMutuallyExclusive() {
        return mutuallyExclusiveDiscounts;
    }

    /**
     * @param mutuallyExclusive the mutuallyExclusive to set
     */
    public void setMutuallyExclusive(List<DiscountBE> mutuallyExclusive) {
        this.mutuallyExclusiveDiscounts = mutuallyExclusive;
    }

    public UserBE getUser() {
        return user;
    }

    public void setUser(UserBE user) {
        this.user = user;
    }
}
