package com.pjeffers.notonthehighstreet.checktout.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import com.pjeffers.notonthehighstreet.checktout.exception.ApplicationException;

/**
 * @author Peter Jeffers
 *
 */
public class Checkout implements NotOnTheHighStreetCheckOut {


	static final String DELIMITER_CHAR 	=",";				// data file delimiter
	static final String COMMENT 		="#";				// data file comment character. Lines starting with this are ignored
	static final String TOTAL_SPEND_DISCOUNT_CODE_PREFIX = "TOT_SPEND";

	// file names
	private String validItemDataFile;
	private String validDiscountDataFile;

	private HashMap <String, Item > allValidProducts;	    // valid products keyed by product code derived from the item data file
	private ArrayList<ItemPromotion> allvalidItemPromotions;// valid promotions derived from the discount data file
	
    private ArrayList <Item> basket; 	  					// list of items to be purchased
	private HashSet <String> invalidItems;					// items which are input as arguments but not found in the item data file
	private HashMap <String,Integer> itemCountSummary;		// maintains the count for each item type
	
	// report parameters
    private double subtotal;								 // the total before discounts are applied  	
	private ArrayList <String> discountSummaryReport;        // array of lines summarising applied discounts per Promotion
    private double totalItemDiscount;						 // the total discount (from all discounts) to be applied
	private double totalSpendDiscount;						 // discount obtained on the total spend (after item discounts)
    private double total;									 // subtotal - total discount
    
	private String subtotalString = "";				         // currency formatted subtotal
	private String totalItemDiscountString = "";			 // currency formatted total discount
	private String totalSpendDiscountString = "";		     // currency formatted total discount
	private String totalDiscountString 	= "";		 		 // currency formatted total discount
	private String totalString 			= "";				 // currency formatted total
	private NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
	TotalSpendPromotion totalSpendPromotion = null;
	/*
	 *  Constructor reads the product and promotion data files and converts them into product and promotions
	 */
	public Checkout(String validItemDataFile, String validPromotionDataFile) throws Exception{

		clear();

		try{	     
				 //assign filenames for later use in reporting
			     this.validItemDataFile 	= validItemDataFile;
			     this.validDiscountDataFile = validPromotionDataFile;
			     
			     // create item and discount objects from the data files
				 allValidProducts =	this.retrieveValidItems(validItemDataFile);
				 allvalidItemPromotions =	this.retrieveValidPromotions(validPromotionDataFile);
		
		}catch(ApplicationException e){

			System.out.println(e.getMessage());
			throw e;
		}
	}

	public void clear() {
		basket 				= new ArrayList<Item>();
		invalidItems		= new HashSet<String>();
		itemCountSummary 	= new HashMap<String, Integer>();
		subtotal 			= 0.0;
		total 				= 0.0;
		subtotalString 	 	= "";
		totalItemDiscount 	= 0.0;
		totalString 		="";
		totalSpendDiscount  =0.0;
		totalDiscountString ="";

		System.out.println("\n");
	}

	public double total() {
		calculateDicountedTotal();
		return total;
	};


	/*
	 * calculate the total for the items in the list: (total = subtotal - total discount)
	 */
	public String  totalBasket(String[] itemList) throws ApplicationException, Exception {
	
		totalItemDiscount = 0.0;
		subtotal		= 0.0;
		total			= 0.0;
	
	    totalString ="";
	    subtotalString  ="";
	    
		try{
				// add the items for each string in the  the list of arguments to the basket
				// subtotal them and keep a record of how many of each distinct item type we have
				addItemsFromListSubtotalAndCount(itemList);

 				calculateDicountedTotal();

		}catch(ApplicationException e){

				System.out.println(e.getMessage());
				throw e;
		}
		return totalString;
	}

	private void calculateDicountedTotal() {
		subtotalString = numberFormat.format(subtotal);
		//System.out.println("Subtotal: "+ subtotalString);

		// apply all discounts and calculate the total discount
		calculateAllDiscountsAndGenerateDiscountSummary(allvalidItemPromotions, allValidProducts, (HashMap<String, Integer>) itemCountSummary.clone());

		// print the discount summary report
        // printSummaryReport();

		total = (subtotal - totalItemDiscount);

		if (totalSpendPromotion !=null) {
			totalSpendDiscount = totalSpendPromotion.calculateDiscount(allValidProducts, itemCountSummary, total);
			total = total - totalSpendDiscount;
		}
		totalString = numberFormat.format(total);
		totalDiscountString = numberFormat.format(totalItemDiscount);


         System.out.print("Basket: ");
		for (Item item:basket){
			System.out.print(item.getCode() +", ");
		}

		System.out.println("\nTotal price expected:"+totalString);
	}

	private void printSummaryReport() {
		for (String line:discountSummaryReport){
			System.out.println(line);
		}
	}

	/*
	* add the items to the basket maintain the subtotal and maintain a count for each item type
	*/
	private void addItemsFromListSubtotalAndCount(String[] itemList) throws ApplicationException{
		
		basket 				= new ArrayList<Item>();
		invalidItems		= new HashSet<String>();
		itemCountSummary 	= new HashMap<String, Integer>();
		subtotal 			= 0.0;
		subtotalString 	 	= "";


		// for each item  code in the list retrieve the item object from the list of valid items
		for(String itemName:itemList){

			scan(itemName);
		}
		
		subtotalString = numberFormat.format(subtotal);
		
		if (!invalidItems.isEmpty()){
			//throw new InvalidItemsException(invalidItemsList);
			throw new ApplicationException( "Cannot calculate total because the following are invalid Items: "+invalidItems.toString() + ". Check the valid item data file: " + validItemDataFile);
			
		}
	}

	public void scan(String itemName) {
		// if the item code is in the list of valid products
		if(allValidProducts.containsKey(itemName)){

			// the item is valid so add it to the basket
			Item item = allValidProducts.get(itemName);
			basket.add(item);

			// maintain the subtotal
			subtotal += item.getPrice();

			// count the items of this type for later use in calculating discounts and add it to a table (HashMap)
			if(!itemCountSummary.containsKey(itemName)){

				//this is the first item of this type we've seen so add it to the item counter HashMap with count 1
				itemCountSummary.put(itemName, 1);

			}else{

				// extract the current count for this item increment it and put it back in
				Integer itemCount=itemCountSummary.get(itemName);
				itemCount++;
				itemCountSummary.put(itemName, itemCount);
			}

		}else{
			// the item is not valid (i.e not in the item data file) so add it to the invalid items list for reporting
			invalidItems.add(itemName);
		}
	}

	/*
	 *   loop through each discount calculating the refunds for each and add them to the total discount 
	 */
	private void calculateAllDiscountsAndGenerateDiscountSummary(ArrayList<ItemPromotion> allValidItemPromotions,
																HashMap<String, Item> allValidItems,
																HashMap<String, Integer> itemCountSummary)

	{
		discountSummaryReport = new ArrayList <String> ();
		double totalRefundForThisDiscount;
		String discountSummaryLine ="";
		
		for(ItemPromotion itemPromotion : allValidItemPromotions){

			// calculate discount
			totalRefundForThisDiscount = itemPromotion.calculateDiscount( allValidItems,
					                                                      itemCountSummary);

			// if there was a refund then add it to the total discount and log the discount summary list and console
			if (totalRefundForThisDiscount !=0.0){
				discountSummaryLine = itemPromotion.getDescription()+" : -"+numberFormat.format(totalRefundForThisDiscount);
				
				// update the summary report with the discount
				discountSummaryReport.add(discountSummaryLine);
				
				totalItemDiscount +=totalRefundForThisDiscount;
			}
		}

		if (totalItemDiscount ==0.0){
			
			discountSummaryReport.add("(no offers available)");

		}
		
		
	}


	/*
	 * Retrieve and  create valid items from the valid products file and put them into a HashMap keyed by item code
	 */
	private HashMap<String, Item> retrieveValidItems(String validItemDataFile) throws  ApplicationException, Exception {
		HashMap<String, Item> validItems = new HashMap<String, Item>();
		BufferedReader br = null;
		String line;
		String parameters[] = null;

		try {
			 br =  new BufferedReader(new FileReader((validItemDataFile)));
			 
			// read a line from the discount data file and discard
			 br.readLine();
			 while ((line=br.readLine())!=null){
				 
				 line = line.trim();
				 // skip blank or commented lines
				 if(line.startsWith(COMMENT)||line.equals("")) continue;

				// split line into an array of parameters (String values)
				 parameters=line.split(DELIMITER_CHAR);
				 
				 //
				 Item item = new Item(parameters);
				 validItems.put(item.getCode() ,item);

			 };
		
		 }catch(FileNotFoundException e){

		   throw new ApplicationException("could not find item data file: "+ validItemDataFile +
				                           ". Check that the file exists and is valid.", e);
		
		 }finally{
	    	 
			 if (br!=null){ 
				 br.close();	   
			 }
	     }
		return validItems;

	}

	/*
	 * Retrieve valid Promotion parameters from the valid discount file and put them into a HashMap keyed by item code
	 */
	private ArrayList<ItemPromotion> retrieveValidPromotions(String validDiscountDataFile) throws ApplicationException, Exception {
		ArrayList<ItemPromotion> validItemPromotions = new ArrayList<ItemPromotion>();
		BufferedReader br = null;
		String line;
		String parameters[];
		try {
			
			 br =  new BufferedReader(new FileReader(validDiscountDataFile));
			 
			 // read a line from the discount data file and discard
			 br.readLine();
			 
			 while ((line = br.readLine())!=null){
				 line = line.trim();
				 
				 // skip blank or commented lines
				 if(line.startsWith(COMMENT)||line.equals("")) continue; 
				 
				 // split line into an array of parameters (String values)
				 parameters=line.split(DELIMITER_CHAR);

				 // if the code indicates that this is a total spend promotion then create a single
				 // total spend promotion otherwise create an Product promotion and add it to the promotion list.
				 if (parameters[0].startsWith(TOTAL_SPEND_DISCOUNT_CODE_PREFIX)) {

				 	if (totalSpendPromotion !=null){
				 		throw new ApplicationException("There can only be one total spend promotion starting with prefix "+TOTAL_SPEND_DISCOUNT_CODE_PREFIX);
					}
					 totalSpendPromotion = new TotalSpendPromotion(parameters);

				 } else {

					 // convert the array to a discount
					 ItemPromotion itemPromotion = new ItemPromotion(parameters);
					 validItemPromotions.add(itemPromotion);
				 }
				 	 
			 };

		 }

		catch(FileNotFoundException e ){
			  
			   throw new ApplicationException("Could not find discount data file: "+ validDiscountDataFile +
					   							". Check that the file exists and is valid.", e);
		
	     } finally {
			 if (br!=null){ 
				 br.close();	   
			 }
	     }
		return validItemPromotions;

	}	
	public ArrayList<Item> getBasket() {
		return basket;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public double getTotal() {
		return total;
	}
	public HashMap<String, Integer> getItemCountSummary() {
		return itemCountSummary;
	}
	public ArrayList<String> getDiscountSummaryReport() {
		return discountSummaryReport;
	}
	public String getSubtotalString() {
		return subtotalString;
	}
	public String getTotalString() {
		return totalString;
	}
	public String getTotalDiscountString() {
		return totalDiscountString;
	}

	
}

