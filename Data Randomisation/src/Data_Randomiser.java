import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class Data_Randomiser {

	private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");		//This is the format of the date in the sql database
	private static final int iter=200;												//This will be the amount of weeks the data is randomised for
	private static ArrayList<StockCount> StockCounts = new ArrayList<StockCount>();
	private static ArrayList<Wastage> Wastages = new ArrayList<Wastage>();
	private static ArrayList<Variance> Variances = new ArrayList<Variance>();
	private static ArrayList<Invoice> Invoices = new ArrayList<Invoice>();
	private static int startID=0;													//This is in case you want to randomise data but there is already data in the database, when this happens just set the startID to the last ID in the table
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		RandStockandVariance();
		RandomiseInvoices();
		RandomiseWastage();
		OutputData();
//		PrintInvoices();
//		PrintStockCounts();
//		PrintWastages();
//		PrintVariances();
	}

	public static int randnoinc(int lower,int upper)			//Input the maximum and minimum numbers in the range to randomise
	{
		if(upper<lower)return(-1);								//If the upper range is smaller than the lower then output -1	
		int n;													//Create the variable that will hold the returned number
		Random rand = new Random();								//Create the Random object that will be used to create the random number
		rand.setSeed(System.nanoTime());						//Set the random seed to make it truly random and use nanotime in case milliseconds isn't fast enough
		int range=upper-(lower-1);								//Find the range but make it 1 more that the actual range so that the randomiser is inclusive
		n=rand.nextInt(range)+lower;							//Create the random number from 0 to the range then add on the lower bound to get the correct numbers
		return(n);												//Return the randomised number
	}
	
	public static void RandStockandVariance()
	{
		Calendar start=new GregorianCalendar(2014,5,02);;		//This is the start date
		int stockID=1,amount,theo,var;
		double varper;											
		int unit;
		for(int j=0;j<iter;j++)									//Ran for as many weeks that are needed
		{
			for(int i=1;i<33;i++)								//For loop started on 1 and for the first StockID until 32 for all the items
			{
				unit=randnoinc(0,1);							//Randomise the unit
				if(unit==1&&i!=19)								//If the unit is 1 meaning the big unit smaller numbers have to be randomised for realness
					{
						amount=randnoinc(1,5);					//Numbers are only randomised between 1 and 5
						StockCounts.add(new StockCount(stockID+startID,i,amount,formatter.format(start.getTime()),unit));	//Randomised data is added to the ArrayList
						theo=randnoinc(0,200);					//Theoretical value is randomised from 0 to 200 because a big unit is being used and variance is only displayed in the small units
						amount=randnoinc(0,200);				//A new amount is randomised so it can be used to calculate the variance with more of a realistic value
					}
				else 
					{
						amount=randnoinc(0,20);					//If the unit was the small unit then the number randomised is only up to 20
						StockCounts.add(new StockCount(stockID+startID,i,amount,formatter.format(start.getTime()),unit));	//Data is added
						theo=randnoinc(0,20);					//The theoretical value is also randomised up to 20 to give a more realistic variance
					}
				
				var=amount-theo;								//The variance is calculated by minusing the actual amount by the theoretical amount
				if(amount==0) varper=var;						//The amount value is made sure it wasn't zero because it would have provided a maths error. The variance percentage is therefore just the same as the variance
				else varper=((double)var/(double)amount)*100;	//Otherwise the variance percentage is calculated
				varper=Math.round(varper);						//The variance percentage is then rounded
				Variances.add(new Variance(i,formatter.format(start.getTime()),theo,var,(int)varper)); 	//All data is then added to the ArrayList
//				System.out.println("Theo: "+theo+" Amount: "+amount+" Var: "+var+" VarPer: "+varper);	
			}
			start.add(Calendar.DAY_OF_YEAR, 7);					//Increment the date by a week ready for the next loop
			stockID++;											//Increment the stockId for the next loop
		}
		System.out.println("Stock and Variance randmoised");	//This is to allow you to know when the data has been randomised to help you keep track of what the program is doing
	}
		
	public static void RandomiseInvoices()
	{	
		Calendar start=new GregorianCalendar(2014,5,02);
		start.add(Calendar.DAY_OF_YEAR, 6);						//Invoices are never sent on the same day as the stock count is taken so I am putting it 6 days afterwards
		ArrayList<Integer> ItGone = new ArrayList<Integer>();	//This ArrayList will keep track of all the previous items gone so there are no repeat items in the invoice
		int invoiceID=1,unit=1,itemID;							//The auto number in the table starts at 1	
		int items = randnoinc(5,32);							//A number is randomised in-between 5 and 32
		for (int i=0;i<iter;i++)
		{
			for(int j=0;j<items;j++)
			{
				itemID=randnoinc(1,32);							//Random ItemID has been created
				if (ItGone.contains(itemID)==false)				//If the number has not already been added to the invoice add it
				{
					Invoices.add(new Invoice(invoiceID+(startID),itemID,randnoinc(1,5),unit,formatter.format(start.getTime())));	//Add item to the invoices ArrayList
					ItGone.add(itemID);							//Add the number to the ArrayList of items that have already been added so there are not repeats
				}
				else j--;										//If the item has already been added then decrement the count as to not waste a loop and run the loop again
			}
			ItGone.clear();										//ItGone needs to be cleared otherwise it will just fill of all the item IDs and the j for loop will loop forever
			start.add(Calendar.DAY_OF_YEAR, 7);					//Increment the date by a week ready for the next loop
			invoiceID++;										//Increment the stockId for the next loop
		}
		System.out.println("Invoices randomised");				//Lets you know that the invoice has been randomised
	}
	
	public static void RandomiseWastage()
	{
		int WastageID=1,waste,datecount=0,nextday=0,itemsno;
		Calendar start = new GregorianCalendar(2014,5,02);									
		Calendar WasteDay = new GregorianCalendar(2014,5,02);
		for (int i=0;i<iter;i++)								//Run for globally defined number of weeks
		{
			datecount=0;
			itemsno=randnoinc(0,24);
			WasteDay.setTime(start.getTime());
			for(int j=0;j<itemsno;j++)							//This random number defines the amount of wasted items there were during that week
			{
				waste=randnoinc(1,20);							//A random number that is used for the case statement below
				switch (waste)									//This is a case statement that gives differing probability to different sizes of waste, I changed it to a case statement from if statements because I thought it would be easier to read but I was wrong
				{
					case 1 : waste=200;							//If the random number is 1 the maximum wastage is 200 items
					break;
				
					case 2:case 3:case 4:
					case 5: waste=50;							//If the random number is 3, 4 or 5 then the maximum number to be wasted is 50
					break;
					
					case 6:case 7:case 8:case 9:case 10:case 11:
					case 12:case 13:case 14:case 15:case 16: case 17:
					case 18: case 19: 
					case 20: waste=15;							//For all other numbers the maximum number wasted is 15
					break;
					
					default: waste=0;
				}
				nextday=randnoinc(0,10);						//The same is done here with nextday that was done with wastage, a random number is randmoised between 0 and 10
				if(nextday>5) nextday=0;						//All number above 5 and nextday is made to be 0
				else if(nextday<5&&nextday>2) nextday=1;		//For number 5, 4 and 3 nextday is 1
				else if(nextday<3) nextday=2;					//And for 2 and 1 nextday is 2
				datecount+=nextday;								//If the total number of days increased by all together as part of this wastage week does not pass a week then the date is put forward, otherwise the date would go into the next week and the data would be incorrect
				if (datecount<7) WasteDay.add(Calendar.DAY_OF_YEAR, nextday);	//The date is then put forward by the amount of days that nextday is equal to
				Wastages.add(new Wastage(WastageID+startID,randnoinc(1,32),randnoinc(1,waste),formatter.format(WasteDay.getTime())));				
				WastageID++;									//Increment the invoice number by one to move onto the next wastage	
			}
			start.add(Calendar.DAY_OF_YEAR, 7);					//Increment the date by a week ready for the next loop
		}
		System.out.println("Wastage randomised");				//To tell you when the wastage has been randomised
	}	

	//These print methods are now obsolete but i have kept them in anyway	
	public static void PrintInvoices()	//The next set of methods simply apply the print method in the objects that hold all the randomised data in the ArrayLists
	{
		Invoice current;										//This variable will be used to manipulate the current object being printed
		System.out.println("INSERT INTO Invoice(InvoiceID,ItemID,amount,unit,date) VALUES");	//This line is to make is possible for me to simply copy and paste the output of the console straight to the msql console because this program is designed to make randomising and adding data easier
		for(int i=0;i<Invoices.size();i++)						//The for loop is run for the entire length of the ArrayList
		{
			current=Invoices.get(i);							//The current object is made to point to the current object in the ArrayList
			current.print();									//The print method is applied
			if (i!=Invoices.size()-1) System.out.println(",");	//This line of code outputs a comma at the end of each line except the end one
		}
		System.out.println(";");								//At the end a ; is output to end the command and input of data to the mysql console
	}															//All the rest are the same but with their respected objects and ArrayLists
	
	public static void PrintStockCounts()
	{
		StockCount current;
		System.out.println("INSERT INTO StockCount(StockID,ItemID,amount,unit,date) VALUES");
		for(int i=0;i<StockCounts.size();i++)
		{
			current=StockCounts.get(i);
			current.print();
			if (i!=StockCounts.size()-1) System.out.println(",");
		}
		System.out.println(";");
	}
	
	public static void PrintWastages()
	{
		Wastage current;
		System.out.println("INSERT INTO Wastage(WastageID,ItemID,amount,date) VALUES");
		for(int i=0;i<Wastages.size();i++)
		{
			current=Wastages.get(i);
			current.print();
			if (i!=Wastages.size()-1) System.out.println(",");
		}
		System.out.println(";");
	}

	public static void PrintVariances()
	{
		Variance current;
		System.out.println("INSERT INTO Variance (ItemID,date,theoretical,variance,VarPer) VALUES");
		for(int i=0;i<Variances.size();i++)
		{
			current=Variances.get(i);
			current.print();
			if (i!=Variances.size()-1) System.out.println(",");
		}
		System.out.println(";");
	}
	
	public static void OutputData() throws IOException			//This method outputs all the data into a text file
	{
		Variance varcur;										//This is used to access the current Variance object
		StockCount stocur;										//Accesses the current StockCount object
		Invoice incur;											//Accesses the current Invoices object
		Wastage wascur;											//Accesses the current Wastage object
		FileWriter fw = new FileWriter("C:\\Users\\edpep\\Documents\\Year 2 Computing\\Algorithms\\Year 2 Labs\\StockControlData.txt",false);	//This is where the file will be stored and what it is called, in this line the FileWriter is also created and the false means do not append
		@SuppressWarnings("resource")
		PrintWriter pw = new PrintWriter(fw,true);				//Here the PrintWriter is created and the true means autoflush which adds the lines automatically
		pw.println("INSERT INTO StockCount(StockID,ItemID,amount,unit,date) VALUES");	//This is the insert command for mysql to input data and makes it easier to input as all I have to do is copy the lines of data and paste them straight into the ssh console
		for(int i=0;i<StockCounts.size();i++)					//For every item in the StockCount ArrayList
		{
			stocur=StockCounts.get(i);							//Look at the current object in the ArrayList
			pw.print(stocur.print());							//Write the returned string from the method print in the class which outputs all the variables of the object in the correct format for the data
			if (i!=StockCounts.size()-1) pw.println(",");		//For every line except the end write a comma afterwards
		}
		pw.println(";");										//For the last line write a ; to finish the insert data command
		
		pw.println("INSERT INTO Variance (ItemID,date,theoretical,variance,VarPer) VALUES");	//All the other loops are idendical but for their respected objects and ArrayLists
		for(int i=0;i<Variances.size();i++)
		{
			varcur=Variances.get(i);
			pw.print(varcur.print());
			if (i!=Variances.size()-1) pw.println(",");
		}
		pw.println(";");
		
		pw.println("INSERT INTO Invoices(InvoiceID,ItemID,amount,unit,date) VALUES");
		for(int i=0;i<Invoices.size();i++)
		{
			incur=Invoices.get(i);
			pw.print(incur.print());
			if (i!=Invoices.size()-1) pw.println(",");
		}
		pw.println(";");
		
		pw.println("INSERT INTO Wastage(WastageID,ItemID,amount,date) VALUES");
		for(int i=0;i<Wastages.size();i++)
		{
			wascur=Wastages.get(i);
			pw.print(wascur.print());
			if (i!=Wastages.size()-1) pw.println(",");
		}
		pw.println(";");
		
		System.out.println("Data Ouput");						//Tells me when it has finished and all the data is output to the file
	}

}
