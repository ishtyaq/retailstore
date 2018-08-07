package com.retail.service;

import com.retail.commontypes.CategoryType;
import com.retail.commontypes.UserType;
import com.retail.dataaccess.DiscountData;
import com.retail.model.DiscountBE;
import com.retail.model.ItemBE;
import com.retail.model.UserBE;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;

import static org.junit.Assert.assertEquals;

public class DiscountServiceTest {
    private DiscountService service;

    private DiscountData discountData;

    private UserBE user;
    private ItemBE item;
    @Before
    public void setUp() throws Exception {

        discountData = new DiscountData();

        // 3 years ago
        Date date = DateUtils.addYears(new Date(), -3);


        user = new UserBE(date, UserType.EMPLOYEE);
        item = new ItemBE(user, new BigDecimal(450),CategoryType.GROCERIES);

        service = new DiscountService(user, new BigDecimal(450), CategoryType.GROCERIES);

        // load the discounts and set them on the bill
        service.setAlwaysApplicable(discountData.loadAlwayApplicableDiscounts());
        service.setMutuallyExclusive(discountData.loadMutuallyExclusiveDiscounts());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testBillInvalidNet() {
        new DiscountService(user, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBillInvalidUser() {
        new DiscountService(null, new BigDecimal(450), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyDiscountsInvalidNet() {

        item.setNet(null);
        service.applyDiscounts(item);
    }

    /**
     * test apply discounts when no discounts are added
     */
    @Test
    public void testApplyDiscountsNoDiscount() {

        service.setAlwaysApplicable(null);
        service.setMutuallyExclusive(null);

        // bill total is 99.99
        BigDecimal net = new BigDecimal(99.99);

        item.setNet(new BigDecimal(99.99));
        // no discounts should be applied
        BigDecimal netPayable = service.applyDiscounts(item);

        assertEquals(net, netPayable);

        service.setAlwaysApplicable(new ArrayList<DiscountBE>());
        service.setMutuallyExclusive(new ArrayList<DiscountBE>());

        netPayable = service.applyDiscounts(item);

        assertEquals(net, netPayable);


    }

    /**
     * Non of the discounts will be applicable
     */
    @Test
    public void testApplyDiscountsNonApplicable() {
        // customer for less that 2 years
        user.setUserType(UserType.CUSTOMER);
        user.setJoiningDate(DateUtils.addYears(new Date(), -1));

        // bill total is 99.99
        BigDecimal net = new BigDecimal(99.99);

        item.setCategory(CategoryType.CLOTHING);
        item.setNet(net);
        item.setUser(user);
        // no discounts should be applied
        BigDecimal netPayable = service.applyDiscounts(item);

        assertEquals(net, netPayable);

    }

    /**
     * Bill has no groceries, User is an employee, with 3 years as a customer and have a bill of $1450
     * should get 30% discount as an employee - discount = $435, discounted net = $1450 - $435 = $1015
     * should get $5 off for every $100 - discount (off $1015) is $50, so final payable is $965
     */
    @Test
    public void testApplyDiscountsEmployee() {
        user.setUserType(UserType.EMPLOYEE);


        item.setUser(user);
        item.setCategory(CategoryType.CLOTHING);
        item.setNet(new BigDecimal(1450.00));
        BigDecimal netPayable = service.applyDiscounts(item);
        assertEquals((new BigDecimal(965.00)).setScale(2), netPayable);
    }

    /**
     * Same scenario as testApplyDiscountsApplicable(), but bill is of groceries,
     * should get $5 off for every $100 - discount (off $1450) is $70, so final payable is $1380
     */
    @Test
    public void testApplyDiscountsEmployeeWithGroceries() {
        user.setUserType(UserType.EMPLOYEE);

        item.setNet(new BigDecimal(1450.00));
        item.setUser(user);
        BigDecimal netPayable = service.applyDiscounts(item);
        assertEquals((new BigDecimal(1380.00)).setScale(2), netPayable);
    }


    /**
     * Bill has no groceries, User is an affiliate, with 3 years as a customer and have a bill of $1450
     * should get 10% discount as an affiliate - discount = $145, discounted net = $1450 - $145 = $1305
     * should get $5 off for every $100 - discount (off $1305) is $65, so final payable is $1240
     */
    @Test
    public void testApplyDiscountsAffiliate() {
        user.setUserType(UserType.AFFILIATE);

        item.setCategory(CategoryType.CLOTHING);
        item.setNet(new BigDecimal(1450.00));
        item.setUser(user);
        BigDecimal netPayable = service.applyDiscounts(item);
        assertEquals((new BigDecimal(1240.00)).setScale(2), netPayable);
    }

    /**
     * Same scenario as testApplyDiscountsAffiliate(), but bill is of groceries,
     * should get $5 off for every $100 - discount (off $1450) is $70, so final payable is $1380
     */
    @Test
    public void testApplyDiscountsAffiliateWithGroceries() {
        user.setUserType(UserType.AFFILIATE);

        item.setNet(new BigDecimal(1450.00));
        item.setUser(user);
        BigDecimal netPayable = service.applyDiscounts(item);
        assertEquals((new BigDecimal(1380.00)).setScale(2), netPayable);
    }

    /**
     * Bill has no groceries, User is a customer, with 3 years as a customer and have a bill of $1450
     * should get 5% discount as an affiliate - discount = $72.5, discounted net = $1450 - $72.5 = $1377.5
     * should get $5 off for every $100 - discount (off $1377.5) is $65, so final payable is $1312.5
     */
    @Test
    public void testApplyDiscountsCustomer() {
        user.setUserType(UserType.CUSTOMER);


        item.setNet(new BigDecimal(1450.00));
        item.setCategory(CategoryType.CLOTHING);
        item.setUser(user);
        BigDecimal netPayable = service.applyDiscounts(item);
        assertEquals((new BigDecimal(1312.50)).setScale(2), netPayable);
    }

    /**
     * Same scenario as testApplyDiscountsAffiliate(), but bill is of groceries,
     * should get $5 off for every $100 - discount (off $1450) is $70, so final payable is $1380
     */
    @Test
    public void testApplyDiscountsCustomerWithGroceries() {
        user.setUserType(UserType.CUSTOMER);

        item.setNet(new BigDecimal(1450.00));
        item.setUser(user);
        BigDecimal netPayable = service.applyDiscounts(item);
        assertEquals((new BigDecimal(1380.00)).setScale(2), netPayable);
    }

    /**
     * New customer joined a year ago, only getting the $5 off for every $100
     */
    @Test
    public void testApplyDiscountsNewCustomer() {
        user.setJoiningDate(DateUtils.addYears(new Date(), -1));
        user.setUserType(UserType.CUSTOMER);


        item.setNet(new BigDecimal(1450.00));
        item.setCategory(CategoryType.CLOTHING);
        item.setUser(user);
        BigDecimal netPayable = service.applyDiscounts(item);
        assertEquals((new BigDecimal(1380.00)).setScale(2), netPayable);
    }

}
