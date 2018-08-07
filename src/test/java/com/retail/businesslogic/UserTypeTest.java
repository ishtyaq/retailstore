package com.retail.businesslogic;

import com.retail.commontypes.CategoryType;
import com.retail.commontypes.DiscountType;
import com.retail.commontypes.UserType;
import com.retail.model.DiscountBE;
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

public class UserTypeTest {
    private DiscountBL discountBL;

    private UserBE user;

    private DiscountBE discount;

    @Before
    public void setUp() throws Exception {

        user = new UserBE(new Date(), UserType.EMPLOYEE);

        user.setJoiningDate( DateUtils.addYears(new Date(), -3));

        Set<CategoryType> exclude = new HashSet<>();
        exclude.add(CategoryType.GROCERIES);

        discountBL = new DiscountBL(user, user.getUserType(), exclude, new BigDecimal(450),new BigDecimal(30),24);

        discount =
                new DiscountBE(DiscountType.PERCENTAGE, new BigDecimal(30),  UserType.EMPLOYEE,null);
        //discount.setUser(user);
        discount.setCategory(CategoryType.GROCERIES);
        discount.setNetPayable(new BigDecimal(450));
    }

    @Test
    public void testUserTypeDiscountValid() {
        // check
        DiscountBL discount =
                new DiscountBL(user,UserType.AFFILIATE, null,new BigDecimal(100),new BigDecimal(10),24);
        discount.setType(DiscountType.AMOUNT);
        assertEquals(UserType.AFFILIATE, discount.getUserType());
        assertEquals(DiscountType.AMOUNT, discount.getType());
    }

    @Test
    public void testUserTypeDiscountDefaultType() {
        //check
        DiscountBL discount =
                new DiscountBL(user,UserType.CUSTOMER, null,new BigDecimal(100),new BigDecimal(10),24 );

     //   assertEquals(UserType.CUSTOMER, discount.getUserType());
        assertEquals(DiscountType.PERCENTAGE, discount.getType());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testUserTypeDiscountInvalidDiscount() {
        new DiscountBL(user, UserType.AFFILIATE, null, null,null,24);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testUserTypeDiscountInvalidUserType() {
        new DiscountBL(user,null,null, new BigDecimal(100),new BigDecimal(10),24 );
    }

    @Test
    public void testIsApplicableInvalidDiscountable() {
        try {
            discountBL.isApplicable(null);
            fail("expected exception not thrown");
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }

        discount.setUser(null);
        try {
            discountBL.isApplicable(discount);
            fail("expected exception not thrown");
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }

        user.setUserType(null);
        discount.setUser(user);

        try {
            discountBL.isApplicable(discount);
            fail("expected exception not thrown");
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsApplicableInvalidDiscount() {
        discount.setUserType(null);
        discountBL.isApplicable(discount);
    }

    @Test
    public void testIsApplicable() {

        discount.setUser(user);
        discount.setCategory(CategoryType.GROCERIES);
        assertFalse(discountBL.isApplicable(discount));

        discount.setCategory(CategoryType.ELECTRONICS);
        assertTrue(discountBL.isApplicable(discount));

        discount.setCategory(null);
        assertTrue(discountBL.isApplicable(discount));

        discount.setCategory(CategoryType.CLOTHING);
    //    discount.setExclude(null);
        assertTrue(discountBL.isApplicable(discount));

        discount.setUserType(UserType.AFFILIATE);
        assertFalse(discountBL.isApplicable(discount));

    }

    @Test
    public void testCalculateInvalid() {
        try {
            discountBL.calculate(null);
            fail("expected exception not thrown");
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }


        // netPayable is null
        try {
            discountBL.calculate(discount);
            fail("expected exception not thrown");
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }

    //    discount.setDiscount(null);
        discount.setNetPayable(new BigDecimal(120));

        try {
            discountBL.calculate(discount);
            fail("expected exception not thrown");
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCalculateNotApplicable() {
        discount.setNetPayable(new BigDecimal(220));
        // category excluded
        discount.setUser(user);
        assertNull(discountBL.calculate(discount));

        // non matching userType
        discount.setUserType(UserType.AFFILIATE);
        assertNull(discountBL.calculate(discount));
    }

    @Test
    public void testCalculateApplicable() {
     //   discount.setNetPayable(bill.getNet());
        discount.setUser(user);
        discount.setCategory(CategoryType.ELECTRONICS);

        // 30% off $450
        BigDecimal amount = discountBL.calculate(discount);
        assertNotNull(amount);

        assertEquals(new BigDecimal(135).setScale(2), amount);

        // 10% off $125.50
        discount.setNetPayable(new BigDecimal(125.50));
        discountBL.setDiscount(new BigDecimal(10));
        amount = discountBL.calculate(discount);
        assertNotNull(amount);

        assertEquals(new BigDecimal(12.55).setScale(2, RoundingMode.HALF_UP), amount);


        // discount with amount simply returns the amount
        discountBL.setType(DiscountType.AMOUNT);
        amount = discountBL.calculate(discount);
    //    amount = discount.discountBL(discount);

        assertEquals(new BigDecimal(10).setScale(2), amount);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateInvalidDiscountType() {
    //    discount.setNetPayable(discount.getNet());
        discount.setCategory(CategoryType.ELECTRONICS);
     //   discount.setType(null);

        discountBL.calculate(discount);
    }
}

