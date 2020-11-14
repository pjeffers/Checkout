package com.pjeffers.notonthehighstreet.checktout.model;

import java.util.Date;

public class BasePromotion {

    String code;				        // the discount code
    String description;			        // the discount description
    double percentageDiscount;	        // the percentage discount to be refunded
                                        //
    Date startDate = null;              // if these dates are set then they delimit the times between which a promotion is effective
    Date endDate = null;                //

    public BasePromotion(String[] parameters) {
        this.code				      = parameters[0].trim();
        this.description		      = parameters[1].trim();
    }
}
