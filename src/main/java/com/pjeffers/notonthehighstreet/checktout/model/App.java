package com.pjeffers.notonthehighstreet.checktout.model;

import com.pjeffers.notonthehighstreet.checktout.exception.ApplicationException;

/**
 * Checkout Application which totals the cost of items whilst applying Promotions
 *
 * This application Assumes that
 *  promotions effect discounts which can be based on a
 *
 *      the minimum number of times a particular product is bought,
 *      the actual times (Tn) a number of a particular product has been purchased
 *      the total checkout price for a basket after item specific promotions have been applied
 *
 *  the discount associated with a promotion can only be applied once to ony item or basket.
 *  the items which trigger the discount can only be used in one discount calculation.
 *  there cannot be different promotions on on item at the same time
 *  the conditions for a discount may be met multiple times during a single checkout
 *
 */
public class App {

    static final String DEFAULT_VALID_ITEM_DATA_FILE 		= "src/main/resources/all_valid_items.data";
    static final String DEFAULT_VALID_PROMOTION_DATA_FILE 	= "src/main/resources/all_valid_promotions.data";
    private static Checkout checkout;

    public  static void main(String[] args) {
        try{

            checkout = new Checkout(DEFAULT_VALID_ITEM_DATA_FILE,
                                    DEFAULT_VALID_PROMOTION_DATA_FILE);

            //checkout.totalBasket(args);
            checkout.scan("001");
            checkout.scan("002");
            checkout.scan("003");
            checkout.total();
            checkout.clear();

            checkout.scan("001");
            checkout.scan("003");
            checkout.scan("001");
            checkout.total();
            checkout.clear();

            checkout.scan("001");
            checkout.scan("002");
            checkout.scan("001");
            checkout.scan("003");
            checkout.total();
            checkout.clear();


        }catch(ApplicationException e){

            // application exception has already been handled so just exit with error code
           return;

        }catch(Exception e){

            //exception has not been handled so print the stack trace and exit with error code
            e.printStackTrace();
            System.exit(1);
        }
    }


}
