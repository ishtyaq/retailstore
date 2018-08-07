package com.retail.model;

import com.retail.commontypes.UserType;
import java.util.Date;

/**
 * map to user that can be employees, affiliates or customer
 *
 *
 *
 *
 */
public class UserBE {
    private Date joiningDate;

    private UserType userType;

    public UserBE(Date joiningDate, UserType type) {
        super();
        setJoiningDate(joiningDate);
        setUserType(type);
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
        if (joiningDate != null) this.joiningDate = new Date(joiningDate.getTime());
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
