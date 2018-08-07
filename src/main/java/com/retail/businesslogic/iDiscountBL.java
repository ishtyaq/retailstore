package com.retail.businesslogic;

import com.retail.model.DiscountBE;

import java.math.BigDecimal;

public interface iDiscountBL {
    /**
     * Apply the discount to the provided bill
     * @param discount - the discountable instance for which the discount should be applied
     * @return the discounted amount if applicable, null otherwise
     */
    BigDecimal calculate(DiscountBE discount);

    /**
     * Determines if the discount is applicable to the provided discountable item
     * @param discount the item that can be discounted
     * @return true if the discount is applicable
     */
    boolean isApplicable(DiscountBE discount);
}

