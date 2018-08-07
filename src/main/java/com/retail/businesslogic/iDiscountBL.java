package com.retail.businesslogic;

import com.retail.model.DiscountBE;
import com.retail.model.ItemBE;

import java.math.BigDecimal;

public interface iDiscountBL {
    /**
     * Apply the discount to the provided bill
     * @param discount - rules to apply the discount from user type to discount amount 10%, 30%, 5% or fixed.
     * @param item - the discountable instance for which the discount should be applied
     * @return the discounted amount if applicable, null otherwise
     */
    BigDecimal calculate(DiscountBE discount, ItemBE item);

    /**
     * Determines if the discount is applicable to the provided discountable item
     * @param discount - rules to apply the discount from user type to discount amount 10%, 30%, 5% or fixed.
     * @param item - the discountable instance for which the discount should be applied
     * @return true if the discount is applicable
     */
    boolean isApplicable(DiscountBE discount, ItemBE item);
}

