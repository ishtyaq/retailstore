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

public class UserTypeTest {
    private DiscountBL discountBL;

    private UserBE user;
    private ItemBE item;
    private DiscountBE discount;

    @Before
    public void setUp() throws Exception {

        user = new UserBE(new Date(), UserType.EMPLOYEE);

        user.setJoiningDate( DateUtils.addYears(new Date(), -3));

        Set<CategoryType> exclude = new HashSet<>();
        exclude.add(CategoryType.GROCERIES);
        item = new ItemBE(user, new BigDecimal(450),CategoryType.GROCERIES);

        discountBL = new DiscountBL(user, new BigDecimal(450),new BigDecimal(30),24);

        discount =
                new DiscountBE(DiscountType.PERCENTAGE, new BigDecimal(30),  UserType.EMPLOYEE,null,exclude);
        //discount.setUser(user);
        discount.setCategory(CategoryType.GROCERIES);

    }

    @Test
    public void testUserTypeDiscountValid() {
        // check, UserType.AFFILIATE,
        user.setUserType(UserType.AFFILIATE);
        DiscountBL discount =
                new DiscountBL(user,new BigDecimal(100),new BigDecimal(10),24);
        discount.setType(DiscountType.AMOUNT);
        assertEquals(UserType.AFFILIATE, discount.getUser().getUserType());
        assertEquals(DiscountType.AMOUNT, discount.getType());
    }

    @Test
    public void testUserTypeDiscountDefaultType() {
        //check UserType.CUSTOMER,
        DiscountBL discount =
                new DiscountBL(user,new BigDecimal(100),new BigDecimal(10),24 );

     //   assertEquals(UserType.CUSTOMER, discount.getUserType());
        assertEquals(DiscountType.PERCENTAGE, discount.getType());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testUserTypeDiscountInvalidDiscount() {
        //UserType.AFFILIATE,
        new DiscountBL(user, null,null,24);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testUserTypeDiscountInvalidUserType() {
        new DiscountBL(user,new BigDecimal(100),null,24 );
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

        user.setUserType(null);

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
        discount.setUserType(null);
        discountBL.isApplicable(discount,item);
    }

    @Test
    public void testIsApplicable() {


        item.setUser(user);
        item.setCategory(CategoryType.GROCERIES);
        assertFalse(discountBL.isApplicable(discount,item));


        item.setCategory(CategoryType.ELECTRONICS);
        assertTrue(discountBL.isApplicable(discount,item));


        item.setCategory(null);
        assertTrue(discountBL.isApplicable(discount,item));


        item.setCategory(CategoryType.CLOTHING);
    //    discount.setExclude(null);
        assertTrue(discountBL.isApplicable(discount,item));

        discount.setUserType(UserType.AFFILIATE);
        assertFalse(discountBL.isApplicable(discount,item));

    }

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

        // category excluded

        item.setUser(user);
        item.setNet(new BigDecimal(220));
        assertNull(discountBL.calculate(discount,item));

        // non matching userType
        discount.setUserType(UserType.AFFILIATE);

        assertNull(discountBL.calculate(discount,item));
    }

    @Test
    public void testCalculateApplicable() {
     //   discount.setNetPayable(bill.getNet());

        item.setUser(user);
        item.setCategory(CategoryType.ELECTRONICS);
        // 30% off $450
        item.setNet(new BigDecimal(450));
        BigDecimal amount = discountBL.calculate(discount,item);
        assertNotNull(amount);

        assertEquals(new BigDecimal(135).setScale(2), amount);

        // 10% off $125.50

        item.setNet(new BigDecimal(125.50));
        discountBL.setDiscount(new BigDecimal(10));
        amount = discountBL.calculate(discount,item);
        assertNotNull(amount);

        assertEquals(new BigDecimal(12.55).setScale(2, RoundingMode.HALF_UP), amount);


        // discount with amount simply returns the amount
        discountBL.setType(DiscountType.AMOUNT);
        amount = discountBL.calculate(discount,item);
    //    amount = discount.discountBL(discount);

        assertEquals(new BigDecimal(10).setScale(2), amount);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateInvalidDiscountType() {
    //    discount.setNetPayable(discount.getNet());

        item.setCategory(CategoryType.ELECTRONICS);
     //   discount.setType(null);
        item.setUser(null);
        item.setNet(new BigDecimal(450));
        discountBL.calculate(discount,item);
    }
}

