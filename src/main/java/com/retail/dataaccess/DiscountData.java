package com.retail.dataaccess;

import com.retail.commontypes.CategoryType;
import com.retail.commontypes.DiscountType;
import com.retail.commontypes.UserType;
import com.retail.model.DiscountBE;

import java.math.BigDecimal;
import java.util.*;

/**
 * This class act as master data for all discount rules.
 * It can later be linked with database or common service for loading  rules.
 *
 *
 *
 */
public class DiscountData implements iDiscountData {

    // discounts that are always applied
    private List<DiscountBE> alwaysApplicable = new ArrayList<>();

    // discounts that only one is applied
    private List<DiscountBE> mutuallyExclusive = new ArrayList<>();

    @Override
    public List<DiscountBE> loadAlwayApplicableDiscounts() {
        return Collections.unmodifiableList(alwaysApplicable);
    }

    @Override
    public List<DiscountBE> loadMutuallyExclusiveDiscounts() {
        return Collections.unmodifiableList(mutuallyExclusive);
    }

    /**
     * Constructs a new service
     */
    public DiscountData() {
        super();

        // discounts that are always applicable
        alwaysApplicable = new ArrayList<>();
        //For every $100 on the bill, there would be a $5 discount
        DiscountBE discount = new DiscountBE(DiscountType.AMOUNT, new BigDecimal(5), UserType.ALL,24);
        discount.setCategory(null);
        alwaysApplicable.add(discount);

        // mutually exclusive discounts
        // A user can get only one of the percentage based discounts on a bill
        // The percentage based discounts do not apply on groceries.
        Set<CategoryType> exclude = new HashSet<>();
        exclude.add(CategoryType.GROCERIES);


        // The discounts below are applied in the order they are inserted
        // If the user is an employee of the store, he gets a 30% discount
        discount = new DiscountBE(DiscountType.PERCENTAGE,
                new BigDecimal(30), UserType.EMPLOYEE,24);
        discount.setCategory(CategoryType.GROCERIES);
        mutuallyExclusive.add(discount);

        // If the user is an affiliate of the store, he gets a 10% discount
        discount = new DiscountBE(DiscountType.PERCENTAGE,
                new BigDecimal(10), UserType.AFFILIATE,24);
        discount.setCategory(CategoryType.GROCERIES);
        mutuallyExclusive.add(discount);

        // If the user has been a customer for over 2 years (24 months), he gets a 5% discount
        discount = new DiscountBE(DiscountType.PERCENTAGE,
                new BigDecimal(5),UserType.CUSTOMER,24);
        discount.setCategory(CategoryType.GROCERIES);
        mutuallyExclusive.add(discount);

    }
}
