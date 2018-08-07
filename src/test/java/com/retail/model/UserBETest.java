package com.retail.model;

import com.retail.commontypes.UserType;
import com.retail.model.UserBE;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class UserBETest {
    private Date today;
    private UserBE user;

    @Before
    public void setUp() throws Exception {
        today = new Date();
        user = new UserBE(today, UserType.EMPLOYEE);
    }

    // can't change user's internal state by modifying the date object
    @Test
    public void testUserDateUserType() {
        assertEquals(today, user.getJoiningDate());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        today.setTime(calendar.getTime().getTime());

        assertFalse(today.equals(user.getJoiningDate()));

    }

    @Test
    public void testGetCustomerSince() {
        assertEquals(today, user.getJoiningDate());
        assertNotSame(today, user.getJoiningDate());
    }

    @Test
    public void testNullDate() {
        UserBE user = new UserBE(null, UserType.AFFILIATE);

        assertNull(user.getJoiningDate());
    }
}
