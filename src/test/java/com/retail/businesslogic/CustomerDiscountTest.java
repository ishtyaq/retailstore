package com.retail.businesslogic;

import com.retail.commontypes.CategoryType;
import com.retail.commontypes.DiscountType;
import com.retail.commontypes.UserType;
import com.retail.model.DiscountBE;
import com.retail.model.ItemBE;
import com.retail.model.UserBE;
import com.retail.service.DiscountService;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CustomerDiscountTest {
    private DiscountBL discountBL;

    private UserBE user;

    private DiscountBE discount;
    private ItemBE item;

    @Before
    public void setUp() throws Exception {

        // 3 years ago
        Date date = DateUtils.addYears(new Date(), -3);

        user = new UserBE(date, UserType.EMPLOYEE);

        Set<CategoryType> exclude = new HashSet<>();
        exclude.add(CategoryType.GROCERIES);
        item = new ItemBE(user, new BigDecimal(450),CategoryType.GROCERIES);

        //service = new DiscountService(user, new BigDecimal(450), CategoryType.GROCERIES);
        discountBL = new DiscountBL(user, new BigDecimal(100), new BigDecimal(5),24);



        //CustomerPeriodDiscount
        discount =
                new DiscountBE(DiscountType.PERCENTAGE, new BigDecimal(5), UserType.CUSTOMER, 24,exclude);
        //discount.setUser(user);

        discount.setCategory(CategoryType.GROCERIES);

    }

    @Test
    public void testCustomerPeriodDiscountValid() {
        DiscountBE discount =
                new DiscountBE(DiscountType.AMOUNT, new BigDecimal(5), null, 24,null);

        assertEquals(new Integer(24), discount.getMonths());
        assertEquals(DiscountType.AMOUNT, discount.getDiscountType());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testCustomerPeriodDiscountInvalidDiscount() {
        //UserType.CUSTOMER,
        new DiscountBL(user,   null, null,24);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testCustomerPeriodDiscountInvalidMonths() {
        new DiscountBL(user, new BigDecimal(100), null, 24);
    }

    @Test
    public void testIsApplicableInvalidDiscountable() {
        try {
            discountBL.isApplicable(null,null);
            fail("expected exception not thrown");
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }


        item.setUser(null);
        try {
            discountBL.isApplicable(discount,item);
            fail("expected exception not thrown");
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }

        user.setJoiningDate(null);
        discountBL.setUser(user);
        item.setUser(user);
        try {
            discountBL.isApplicable(discount,item);
            fail("expected exception not thrown");
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsApplicableInvalidDiscount() {
        discount.setMonths(null);
        item.setUser(null);
        discountBL.isApplicable(discount,item);
    }

    @Test
    public void testIsApplicable() {
        //discount.setUserType(UserType.EMPLOYEE);
        user.setJoiningDate( DateUtils.addYears(new Date(), -3));

        item.setUser(user);
        item.setCategory(CategoryType.GROCERIES);
        assertFalse(discountBL.isApplicable(discount,item));


        item.setCategory(CategoryType.ELECTRONICS);
        assertTrue(discountBL.isApplicable(discount,item));


        item.setCategory(null);
        assertTrue(discountBL.isApplicable(discount,item));


        item.setCategory(CategoryType.CLOTHING);
       // discount.setExclude(null);
        assertTrue(discountBL.isApplicable(discount,item));

        discount.setMonths(36);
        assertFalse(discountBL.isApplicable(discount,item));

        discount.setMonths(35);
        assertTrue(discountBL.isApplicable(discount,item));

    }

    // ---- Calculate tests

    @Test
    public void testCalculateInvalid() {
        try {
            discountBL.calculate(null,null);
            fail("expected exception not thrown");
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }


        // netPayable is null
        try {
            item.setUser(null);
            discountBL.calculate(discount,item);
            fail("expected exception not thrown");
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }

    //    discount.setDiscount(null);
    //    discount.setNetPayable(new BigDecimal(120));

        try {
            discountBL.calculate(discount,item);
            fail("expected exception not thrown");
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCalculateNotApplicable() {

        item.setUser(user);
        item.setNet(new BigDecimal(220));
        // category excluded
        assertNull(discountBL.calculate(discount,item));

        // setting the customer period to 3 years
        discount.setMonths(36);
        assertNull(discountBL.calculate(discount,item));
    }

    @Test
    public void testCalculateApplicable() {
    //    discount.setNetPayable(bill.getNet());


        item.setUser(user);
        item.setCategory(CategoryType.ELECTRONICS);
        // 5% off $450
        BigDecimal amount = discountBL.calculate(discount,item);
        assertNotNull(amount);

        assertEquals(new BigDecimal(22.50).setScale(2), amount);

        // 10% off $125.50

        item.setNet(new BigDecimal(125.50));
        discountBL.setDiscount(new BigDecimal(10));
        amount = discountBL.calculate(discount,item);
        assertNotNull(amount);

        assertEquals(new BigDecimal(12.55).setScale(2, RoundingMode.HALF_UP), amount);


        // discount with amount simply returns the amount
        discountBL.setType(DiscountType.AMOUNT);

        amount = discountBL.calculate(discount,item);

        assertEquals(new BigDecimal(10).setScale(2), amount);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateInvalidDiscountType() {
    //   bill.setNetPayable(bill.getNet());

        item.setCategory(CategoryType.ELECTRONICS);
        discountBL.setType(null);

        discountBL.calculate(discount,item);
    }
}
