package com.pjeffers.notonthehighstreet.checktout.model;

import com.pjeffers.notonthehighstreet.checktout.exception.ApplicationException;
import com.pjeffers.notonthehighstreet.checktout.utils.Utils;

class Product{
    String code;
    String description;
    String priceString;
    double price;


    public Product(String [] parameter) throws Exception {
        try{
            this.code 			= parameter[0];
            this.description 	= parameter[1];
            this.priceString    = parameter[2];

            // check that none of the string fields are empty
            if ((code.equals("")			||
                    (description.equals(""))		||
                    (priceString.equals("")))){

                // throw an exception this will get caught and rethrown
                throw new ApplicationException("Mandatory fields are empty");
            }

        }catch(Exception e){

            if (Utils.isNullEmptyOrWhiteSpace(code)){

                throw new ApplicationException("Fields in the item data file cannot be empty and there must be 3 entries. Check the valid item data file.",e);
            }else{
                throw new ApplicationException("Fields in the item data file cannot be empty and there must be 3 entries for item: "+ code+". Check the valid item data file.",e);
            }
        }

        // now check we've been given a valid decimal for the price
        try{
            this.price 	= Double.parseDouble(priceString);

            //the price must be greater than zero
            if(this.price<0){
                throw new Exception("the item price must be greater than zero");
            }
        }
        catch(Exception e){
            throw new ApplicationException("Invalid number \""+parameter[2]+"\" for price in Item : "
                    +this.code+". It must be a valid number greater than 0. Check the valid item data file.", e);
        }

    }


    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }


}

