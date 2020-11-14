package com.pjeffers.notonthehighstreet.checktout.model;
/**
 * PriceBasket Test Harness
 */


import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;


/**
 * @author peterjeffers
 *
 */
public class CheckoutTest {

	Checkout checkout;
	String list;
	String [] itemCodes;
	String subtotal;
	String result;
	
	@Before
	public void setUp(){ 

			try {
				checkout = new Checkout(App.DEFAULT_VALID_ITEM_DATA_FILE,
						       App.DEFAULT_VALID_PROMOTION_DATA_FILE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	
	// the following tests test the PriceBasket functionality as per the requirements
	
	@Test
	public void testSingleDiscountOnApplesNoDiscountOnBread() {
		//
		list = "Apples Milk Bread";
		System.out.println(list);		
		itemCodes = list.split(" ");
		try {
			checkout.totalBasket(itemCodes);
			subtotal= checkout.getSubtotalString();
			result = checkout.getTotalString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals("£3.00", result);
	}	
	
	
	@Test
	public void testSingleDiscountOnBread() {
		// test single discount on bread
		list = "Soup Soup Soup Bread Bread";
		itemCodes = list.split(" ");
		try {
			checkout.totalBasket(itemCodes);
			subtotal= checkout.getSubtotalString();
			result = checkout.getTotalString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals("£3.15", result);
	}
	
	@Test
	public void testSingleDiscountOnOneBread(){
		// test single discount on one bread (4 cans of soup and one loaf of bread should give one discount for bread)
	    list = "Soup Soup Soup Soup Bread";
		itemCodes = list.split(" ");
		try {
			checkout.totalBasket(itemCodes);
			subtotal= checkout.getSubtotalString();
			result = checkout.getTotalString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals("£3.00", result);
	}	


	@Test
	public void testMultipleDiscountOnBreadAndApples (){
		// test multiple discount on bread and apples (4 cans of soup and two loafs of bread and 2 apples 
		// should give two discounts for bread  and two discounts for apples
		list = "Soup Soup Soup Soup Bread Bread Milk Apples Apples";
		itemCodes = list.split(" ");
		try {
			checkout.totalBasket(itemCodes);
			subtotal= checkout.getSubtotalString();
			result = checkout.getTotalString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals("£6.50", result);
	}


	@Test
	public void testZreoItems() {
		list = " ";
		itemCodes = list.split(" ");
		try {
			checkout.totalBasket(itemCodes);
			subtotal= checkout.getSubtotalString();
			result = checkout.getTotalString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals("£0.00", result);
	}

	@Test
	public void testZeroDiscounts() {
		// test zero discounts
		String discountSummary = "";
		list = "Milk";
		itemCodes = list.split(" ");
		try {
			checkout.totalBasket(itemCodes);
			subtotal= checkout.getSubtotalString();
			discountSummary = checkout.getDiscountSummaryReport().get(0);
			result = checkout.getTotalString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals("£1.30", result);
		assertEquals("(no offers available)",discountSummary );
	}	
	
	@Test
	public void testZeroDiscounts2() {
		// test zero discounts
		String discountSummary ="";
		list = "Milk Soup";
		itemCodes = list.split(" ");
		try {
			checkout.totalBasket(itemCodes);
			subtotal= checkout.getSubtotalString();
			discountSummary = checkout.getDiscountSummaryReport().get(0);
			result = checkout.getTotalString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals("£1.95", result);
		assertEquals("(no offers available)",discountSummary );
	}	
				
	
	@Test
	public void testInvalidItemsInList() {

			// test invalid items in list
			list = "Apples Milk Bread Cornflakes Beans";
			itemCodes = list.split(" ");
			try {
				checkout.totalBasket(itemCodes);
				subtotal= checkout.getSubtotalString();
				result = checkout.getTotalString();
			} catch (Exception e) {
				assertEquals(e.getMessage(), "Cannot calculate total because the following are invalid Items: [Cornflakes, Beans]. Check the valid item data file: "+App.DEFAULT_VALID_ITEM_DATA_FILE);

			}
			
	}
	
	// the following tests test the validation in Item and Discount using invalid dataFiles to try and initialise them
	
	@Test
	public void testInvalidDiscountEntriesInTheDiscountDataFile(){

		try {
			checkout = new Checkout(App.DEFAULT_VALID_ITEM_DATA_FILE,
					"src/test/resources/invalid_discounts1.data");
		} catch (Exception e) {
			String message=e.getMessage();
			assertEquals("Fields in the discount data file cannot be empty for discount: BAD_DISCOUNT1. Check the discount data file.",message );
		}
		
	}
	
	@Test
	public void testInvalidDiscountEntriesInTheDiscountDataFile2 (){
		
		try {
			checkout = new Checkout(App.DEFAULT_VALID_ITEM_DATA_FILE,
											"src/test/resources//invalid_discounts2.data");
		} catch (Exception e) {
			String message=e.getMessage();
			assertEquals("Fields in the discount data file cannot be empty for discount: BAD_DISCOUNT2. Check the discount data file.",message );
		}
		
	}

	
	@Test
	public void testInvalidItemEntriesInTheItemDataFile1(){
		
		try {
			checkout = new Checkout("src/test/resources/invalid_items1.data",
					 					App.DEFAULT_VALID_PROMOTION_DATA_FILE);
		} catch (Exception e) {
			String message=e.getMessage();
			assertEquals("Fields in the item data file cannot be empty and there must be 3 entries for item: BAD_ITEM1. Check the valid item data file.",message );
		}
		
	}	
	
	@Test
	public void testInvalidItemEntriesInTheItemDataFile2(){
		
		try {
			checkout = new Checkout("src/test/resources/invalid_items2.data",
					 					App.DEFAULT_VALID_PROMOTION_DATA_FILE);
		} catch (Exception e) {
			String message=e.getMessage();
			assertEquals("Fields in the item data file cannot be empty and there must be 3 entries for item: BAD_ITEM2. Check the valid item data file.",message );
		}
		
	}		
	
	@Test
	public void testInvalidItemEntriesInTheItemDataFile3(){
		
		try {
			checkout = new Checkout("src/test/resources/invalid_items3.data",
					 					App.DEFAULT_VALID_PROMOTION_DATA_FILE);
		} catch (Exception e) {
			String message=e.getMessage();
			assertEquals("Fields in the item data file cannot be empty and there must be 3 entries for item: BAD_ITEM3. Check the valid item data file.",message );
		}
		
	}

	@Test
	public void testInvalidItemEntriesInTheItemDataFile4(){
		
		try {
			checkout = new Checkout("src/test/resources/invalid_items4.data",
					 					App.DEFAULT_VALID_PROMOTION_DATA_FILE);
		} catch (Exception e) {
			String message=e.getMessage();
			assertEquals("Fields in the item data file cannot be empty and there must be 3 entries. Check the valid item data file.",message );
		}	
	}	
	
	@Test
	public void testInvalidItemEntriesInTheItemDataFileNoCode(){
		
		try {
			checkout = new Checkout("src/test/resources/invalid_items5.data",
					 					App.DEFAULT_VALID_PROMOTION_DATA_FILE);
		} catch (Exception e) {
			String message=e.getMessage();
			assertEquals("Invalid number \"HHHH\" for price in Item : BAD_ITEM4. It must be a valid number greater than 0. Check the valid item data file.",message );
		}	
	}	
	
	@Test
	public void testInvalidItemEntriesInTheItemDataFileNoCode2(){
		
		try {
			checkout = new Checkout("src/test/resources/invalid_items6.data",
					 					App.DEFAULT_VALID_PROMOTION_DATA_FILE);
		} catch (Exception e) {
			String message=e.getMessage();
			assertEquals("Invalid number \"-6.50\" for price in Item : BAD_ITEM5. It must be a valid number greater than 0. Check the valid item data file.",message );
		}	
	}	
	
	@Test
	public void testDiscountValidationInvalidNumberForBuyNumber (){

		try {
			checkout = new Checkout(App.DEFAULT_VALID_ITEM_DATA_FILE,
										"src/test/resources/invalid_discounts5.data");
		} catch (Exception e) {
			String message=e.getMessage();
			assertEquals("Invalid number \"LLL\" for buy number for discount: BAD_DISCOUNT4. It must be a valid number greater than 0. Check the discount data file.",message );
		}	
	}	
	
	@Test
	public void testDiscountValidationNegativeNumberForBuyNumber(){
		try {
			checkout = new Checkout(App.DEFAULT_VALID_ITEM_DATA_FILE,
										"src/test/resources/invalid_discounts6.data");
		} catch (Exception e) {
			String message=e.getMessage();
			assertEquals("Invalid number \"-30\" for buy number for discount: BAD_DISCOUNT5. It must be a valid number greater than 0. Check the discount data file.",message );
		}	
	}

	@Test
	public void testDiscountValidationInvalidNumberForPercentage(){
		try {
			checkout = new Checkout(App.DEFAULT_VALID_ITEM_DATA_FILE,
										"src/test/resources/invalid_discounts7.data");
		} catch (Exception e) {
			String message=e.getMessage();
			assertEquals("Invalid number \"LLL\" for percentage discount for discount: BAD_DISCOUNT6. It must be a valid number greater than 0. Check the discount data file",message );
		}	
	}	
	
	@Test
	public void testDiscountValidationInvalidNumberForPercentage2(){
		
		try {
			checkout = new Checkout(App.DEFAULT_VALID_ITEM_DATA_FILE,
										"src/test/resources/invalid_discounts8.data");
		} catch (Exception e) {
			String message=e.getMessage();
			assertEquals("Invalid number \"-30\" for percentage discount for discount: BAD_DISCOUNT7. It must be a valid number greater than 0. Check the discount data file",message );
		}	
	}	


	@Test
	public void testWithNewAdditionalDiscounts1 (){
		
		// test  with new additional discounts and items in the Checkout configuration

		/*
		"*** Test with new additional discounts and items in configuration      ***"
		"*** check that 3 cans of soup produces 1 bread discount and therefore  ***"
		"*** allows the lower priority bread discount due to 3 cheeses          ***"
		"*** config files are: test/all_valid_items_plus_some_new_ones.data and ***"
		"*** test/all_valid_discounts_plus_some_new_ones.data                   ***"
		*/
		try {
			checkout = new Checkout("src/test/resources/all_valid_items_plus_some_new_ones.data",
										 "src/test/resources/all_valid_discounts_plus_some_new_ones.data");
		} catch (Exception e) {
			e.printStackTrace();
		}

		list = "Milk Soup Soup Soup Bread Cheese Cheese Cheese Bread Eggs Eggs Apples";
		itemCodes = list.split(" ");
		try {
			checkout.totalBasket(itemCodes);
			subtotal= checkout.getSubtotalString();
			result = checkout.getTotalString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals("£10.95", result);
	}	
	
	@Test
	public void testWithNewAdditionalDiscounts2(){
		
		/*
		"*** Test with new additional discounts and items in configuration       ***"
		"*** check that 4 items of soup produces 2 bread discounts and therefore ***"
		"*** inhibits the bread discount due to 3 cheeses                        ***"
		"*** config files are: test/all_valid_items_plus_some_new_ones.data and  ***"
		"*** test/all_valid_discounts_plus_some_new_ones.data                    ***"
		*/

		try {
			checkout = new Checkout("src/test/resources/all_valid_items_plus_some_new_ones.data",
										 "src/test/resources/all_valid_discounts_plus_some_new_ones.data");
		} catch (Exception e) {
			e.printStackTrace();
		}

		list = "Milk Soup Soup Soup Soup Bread Cheese Cheese Cheese Bread Eggs Apples";
		itemCodes = list.split(" ");
		try {
			checkout.totalBasket(itemCodes);
			subtotal= checkout.getSubtotalString();
			result = checkout.getTotalString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals("£12.00", result);
	}	
	
	@Test
	public void testDiscountDataFileNotFound(){

		try {
			checkout = new Checkout(App.DEFAULT_VALID_ITEM_DATA_FILE,
										"nonExistantDiscountDataFile.data");
		} catch (Exception e) {
			String message = e.getMessage();
			assertEquals("Could not find discount data file: nonExistantDiscountDataFile.data. Check that the file exists and is valid.",message );
		}
		
	}
	
	@Test
	public void testItemDataFileNotFound(){

		try {
			checkout = new Checkout("nonExistantItemDataFile.data",
					                      App.DEFAULT_VALID_PROMOTION_DATA_FILE);
		} catch (Exception e) {
			String message = e.getMessage();
			assertEquals("could not find item data file: nonExistantItemDataFile.data. Check that the file exists and is valid.",message );
		}
		
	}
}
