package com.retail.service;


import com.retail.model.ItemBE;

import java.math.BigDecimal;

public interface iDiscountService {

    BigDecimal applyDiscounts(ItemBE itemBE);
}
