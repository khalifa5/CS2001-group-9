
public class StockCount extends Wastage {	//This class inherits from the wastage class because StockCount uses all the variables that wastage uses plue one extra: unit

	private int unit;
	
	public StockCount(int s, int i, int a, String d,int u) {
		super(s, i, a, d);	//These are the variables inherited from the wastage class
		unit=u;				//This is the extra variable added for the Stock Control class
		// TODO Auto-generated constructor stub
	}
	
	public String print()
	{
		String output;
		output="("+super.ID+","+super.ItemID+","+super.amount+","+unit+",'"+super.date+"')";	//All the variables are output in the print class even the inherited variablies needed for this class
		return(output);
	}
}
