package com.pjeffers.notonthehighstreet.checktout.model;

import com.pjeffers.notonthehighstreet.checktout.exception.ApplicationException;
import com.pjeffers.notonthehighstreet.checktout.utils.Utils;

import java.util.HashMap;

public class TotalSpendPromotion extends BasePromotion  {

    double spendThreshold=0.0;
    String spendThresholdString="0.0";
    double percentageDiscount =0.0;
    String percentageDiscountString="";

    public TotalSpendPromotion(String[] parameters) {
        super(parameters);

        try{
            this.spendThresholdString     = parameters[2].trim();
            this.percentageDiscountString = parameters[3].trim();

            // check that none of the string fields are empty
            if     ((code.equals("")			      ||
                    (description.equals(""))		  ||
                    (spendThresholdString.equals("")) ||
                    (percentageDiscountString.equals("")))){

                // throw an exception this will get caught and rethrown
                throw new ApplicationException("Fields cannot be empty");
            }

        }catch(Exception e){

            if (Utils.isNullEmptyOrWhiteSpace(code)){
                throw new ApplicationException("Discount fields in the promotion data file cannot be empty and there must be six entries. Check the discount data file.",e);
            }else{
                throw new ApplicationException("Fields in the promotion data file cannot be empty for discount: "+ code+". Check the discount data file.",e);
            }
        }

    // check that we have a valid trigger threshold (value of spend which triggers discount).
		try{
            this.spendThreshold = Double.parseDouble(spendThresholdString);
            if(this.spendThreshold<=0){
                throw new ApplicationException("Spend threshold is invalid");
            }
        } catch(Exception e){
        throw new ApplicationException("Invalid spend threshold: \""+spendThresholdString+"\" for discount: "
                +this.code+". It must be a valid decimal number greater than 0. Check the discount data file.", e);
        }

		try{
            this.percentageDiscount	= Double.parseDouble(percentageDiscountString);
            if(this.percentageDiscount<=0){
                throw new ApplicationException("percentage discount cannot be negative or zero");
            }
		} catch(Exception e){
            throw new ApplicationException("Invalid number \""+percentageDiscountString+"\" for percentage discount for discount: "
                +this.code+". It must be a valid number greater than 0. Check the discount data file", e);
		}

    }

    public double calculateDiscount(HashMap<String, Item> allValidProducts, HashMap<String, Integer> itemCountSummary, double totalWithItemDiscounts) {
        double discount = 0.0;
        if ((totalWithItemDiscounts>this.spendThreshold)&&(totalWithItemDiscounts>0)) {
            discount = totalWithItemDiscounts * percentageDiscount/ 100;
        }
        return discount;
    }


}
