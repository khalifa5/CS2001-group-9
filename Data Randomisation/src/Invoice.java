
public class Invoice {

	private int InvoiceID;	//The variables correspond to fields of the tables in the invoice table
	private int ItemID;
	private int amount;
	private int unit;
	private String date;

	public Invoice(int i,int it,int a,int u,String d)	//This method is used to input variables when the object is created
	{
		InvoiceID=i;
		ItemID=it;
		amount=a;
		unit=u;
		date=d;
	}
	
	public String print()		//The print function outputs variables in the object in a format needed for the mysql input
	{
		String output;
		output="("+InvoiceID+","+ItemID+","+amount+","+unit+",'"+date+"')";
		return(output);
	}
}
