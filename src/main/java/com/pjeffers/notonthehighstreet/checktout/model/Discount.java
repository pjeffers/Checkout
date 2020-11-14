package com.pjeffers.notonthehighstreet.checktout.model;

import java.util.HashMap;

public interface Discount {


    public double calculateDiscount(HashMap<String, Item> allValidProducts,
                                    HashMap<String, Integer> itemCountSummary );

}
