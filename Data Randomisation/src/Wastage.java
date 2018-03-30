
public class Wastage {
	
	protected int ID;		//All these variables are protected, this is so the inherited classes: StockCount can use the variables but with the variables effectively private
	protected int ItemID;
	protected int amount;
	protected String date;
	
	public Wastage(int s,int i,int a,String d)
	{
		ID=s;
		ItemID=i;
		amount=a;
		date=d;
	}
	
	public String print()
	{
		String output;
		output="("+ID+","+ItemID+","+amount+",'"+date+"')";
		return(output);
	}
}
