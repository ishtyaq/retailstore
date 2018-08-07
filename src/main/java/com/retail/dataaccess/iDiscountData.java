package com.retail.dataaccess;

import com.retail.model.DiscountBE;

import java.util.List;

public interface iDiscountData {
    /**
     * Load the discounts that are always applicable
     * @return always applicable discounts
     */
    List<DiscountBE> loadAlwayApplicableDiscounts();

    /**
     * Load the discounts that are mutually exclusive, i.e. only one
     * should be applied
     * @return mutually exclusive discounts
     */
    List<DiscountBE> loadMutuallyExclusiveDiscounts();
}
