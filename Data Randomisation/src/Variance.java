
public class Variance {
	
	private int ItemID;
	private String date;
	private int theoretical;
	private int variance;
	private int varper;
	
	public Variance(int id,String d,int t,int v,int vp)
	{
		ItemID=id;
		date=d;
		theoretical=t;
		variance=v;
		varper=vp;
	}
	
	public String print() {
		String output;
		output="("+ItemID+",'"+date+"',"+theoretical+","+variance+","+varper+")";
		return(output);
	}
}

