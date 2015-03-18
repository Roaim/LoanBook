package rah.loanbook;
import java.util.*;
import java.text.*;
import android.app.*;

public class SetGet
{
	String time,name,amount,reason;
			
	
	public String getTime()
	{
		Calendar calendar=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MMM/yyyy - hh:mm a");
		time=sdf.format(calendar.getTime());
		return time;
	}
	
	public String getName()
	{	
		return this.name;
	}
	
	public String getAmount()
	{		
		return this.amount;
	}
	
	public String getReason()
	{
		
		return this.reason;
	}
	
	public void setEditText(String name,String amount,String reason)
	{
		this.name=name;
		this.amount=amount;
		this.reason=reason;
	}

}
