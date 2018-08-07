package com.retail.dataacess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.retail.businesslogic.DiscountBL;
import com.retail.commontypes.CategoryType;
import com.retail.commontypes.DiscountType;
import com.retail.commontypes.UserType;
import com.retail.dataaccess.DiscountData;
import com.retail.model.DiscountBE;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class DiscountDataTest {
    private DiscountData discountData;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        discountData = new DiscountData();
    }

    /**
     * Test method for .
     */
    @Test
    public void testLoadAlwayApplicableDiscounts() {

        List<DiscountBE> discounts = discountData.loadAlwayApplicableDiscounts();

        assertNotNull(discounts);

        assertEquals(1, discounts.size());
        // NetMultiplesDiscount
        DiscountBE discount =  discounts.get(0);

        validateDiscount(discount, new BigDecimal(5), DiscountType.AMOUNT, true);

        //assertEquals(new BigDecimal(100), discount.getNetMultiples());
    }

    /**
     * Test method for
     */
    @Test
    public void testLoadMutuallyExclusiveDiscounts() {
        List<DiscountBE> discounts = discountData.loadMutuallyExclusiveDiscounts();

        assertNotNull(discounts);

        assertEquals(3, discounts.size());

        // check the order of the discounts
        // employee discount, UserTypeDiscount
        DiscountBE discount =  discounts.get(0);

        validateDiscount(discount, new BigDecimal(30), DiscountType.PERCENTAGE, false);

        assertEquals(UserType.EMPLOYEE, discount.getUserType());

        //Set<CategoryType> exclude = discount.getExclude();
        //validateExclude(exclude, CategoryType.GROCERIES);

        // affiliate discount, UserTypeDiscount
        discount =   discounts.get(1);

        validateDiscount(discount, new BigDecimal(10), DiscountType.PERCENTAGE, false);

        assertEquals(UserType.AFFILIATE, discount.getUserType());

     //   exclude = discount.getExclude();
     //   validateExclude(exclude, CategoryType.GROCERIES);


        // user period, CustomerPeriodDiscount
        DiscountBE periodDiscount = discounts.get(2);

        validateDiscount(periodDiscount, new BigDecimal(5), DiscountType.PERCENTAGE, false);

        assertEquals(new Integer(24), periodDiscount.getMonths());

    //    exclude = periodDiscount.getExclude();
    //    validateExclude(exclude, CategoryType.GROCERIES);
    }

    private void validateDiscount(DiscountBE discount, BigDecimal amount,
                                  DiscountType type, boolean emptyExclude) {

        assertNotNull(discount);

        assertEquals(amount, discount.getAmount());

    //    if(emptyExclude) {
    //        assertNull(discount.getExclude());
    //    } else {
    //        assertNotNull(discount.getExclude());
    //    }

        assertEquals(type, discount.getDiscountType());
    }

    private void validateExclude(Set<CategoryType> exclude, CategoryType... categories) {
        assertNotNull(exclude);
        assertEquals(categories.length, exclude.size());

        for(CategoryType category: categories) {
            assertTrue(exclude.contains(category));
        }
    }
}
