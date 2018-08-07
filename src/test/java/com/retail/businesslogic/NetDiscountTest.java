package com.retail.businesslogic;

import com.retail.commontypes.CategoryType;
import com.retail.commontypes.DiscountType;
import com.retail.commontypes.UserType;
import com.retail.model.DiscountBE;
import com.retail.model.ItemBE;
import com.retail.model.UserBE;
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

public class NetDiscountTest {
    private DiscountBL discountBL;

    private UserBE user;
    private ItemBE item;
    private DiscountBE discount;

    @Before
    public void setUp() throws Exception {

        // 3 years ago
        Date date = DateUtils.addYears(new Date(), -3);

        user = new UserBE(date, UserType.EMPLOYEE);
        item = new ItemBE(user, BigDecimal.ZERO,CategoryType.GROCERIES);

        Set<CategoryType> exclude = new HashSet<>();
        exclude.add(CategoryType.GROCERIES);
     //   bill = new Bill(user, new BigDecimal(450), CategoryType.GROCERIES);
        discountBL = new DiscountBL(user, new BigDecimal(100),new BigDecimal(5),24);



        discount =
                new DiscountBE(DiscountType.AMOUNT,new BigDecimal(5), UserType.ALL, null, exclude);
        discount.setCategory(CategoryType.GROCERIES);

    }

    @Test
    public void testNetMultiplesDiscountValid() {
        DiscountBE discount =
                new DiscountBE(DiscountType.AMOUNT,new BigDecimal(5), null, null, null);

    //    assertEquals(new BigDecimal(100), discount.getNetMultiples());
        assertEquals(DiscountType.AMOUNT, discount.getDiscountType());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testNetMultiplesDiscountInvalidDiscount() {
        new DiscountBL(user, new BigDecimal(100),null, 24);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testNetMultiplesDiscountNullNetMultiples() {
        new DiscountBL(user, null,new BigDecimal(5),24);
    //    new NetMultiplesDiscount(new BigDecimal(5), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNetMultiplesDiscountZeroNetMultiples() {
        new DiscountBL(user, BigDecimal.ZERO, null,24);
        //    new NetMultiplesDiscount(new BigDecimal(5), null, BigDecimal.ZERO);
    }


    @Test
    public void testIsApplicableInvalidDiscountable() {
        try {
            discountBL.isApplicable(null,null);
            fail("expected exception not thrown");
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }



        try {
            discountBL.isApplicable(discount,null);
            fail("expected exception not thrown");
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsApplicableInvalidDiscount() {


        item.setNet(new BigDecimal(99));
        item.setUser(null);
    //    discount.setNetMultiples(null);
        discountBL.isApplicable(discount,item);
    }

    @Test
    public void testIsApplicable() {

        item.setUser(user);
        item.setCategory(CategoryType.CLOTHING);
        item.setNet(new BigDecimal(99));
        assertFalse(discountBL.isApplicable(discount,item));


        item.setNet(new BigDecimal(100));
        assertTrue(discountBL.isApplicable(discount,item));


        item.setNet(new BigDecimal(200));
    //    discount.setNetMultiples(new BigDecimal(200));
        assertTrue(discountBL.isApplicable(discount,item));


        item.setCategory(null);
        assertTrue(discountBL.isApplicable(discount,item));

     //   discount.setExclude(null);
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

        discountBL.setDiscount(null);

        item.setNet(new BigDecimal(120));
        try {
            discountBL.calculate(discount,item);
            fail("expected exception not thrown");
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCalculateNotApplicable() {

        item.setNet(new BigDecimal(220));
        item.setUser(user);
        // category excluded
        assertNull(discountBL.calculate(discount,item));

        // setting the customer period to 3 years
    //   discount.setNetMultiples(new BigDecimal(300));
        assertNull(discountBL.calculate(discount,item));
    }

    @Test
    public void testCalculateApplicable() {
        //bill.setNetPayable(bill.getNet());

        item.setUser(user);
        item.setCategory(CategoryType.ELECTRONICS);
        // $5 off for every $100 from a net of $450
        item.setNet(new BigDecimal(450));
        BigDecimal amount = discountBL.calculate(discount,item);
        assertNotNull(amount);

        assertEquals(new BigDecimal(20.00).setScale(2), amount);

        // $10 off for every $200 from a net of $990

        item.setNet(new BigDecimal(990));
        discountBL.setNetMultiples(new BigDecimal(200));
        discountBL.setDiscount(new BigDecimal(10));
        amount = discountBL.calculate(discount,item);
        assertNotNull(amount);

        assertEquals(new BigDecimal(40.00).setScale(2, RoundingMode.HALF_UP), amount);

    }
}
