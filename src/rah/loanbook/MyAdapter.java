package rah.loanbook;
import android.widget.*;
import java.util.*;
import android.content.*;
import android.view.*;

public class MyAdapter extends BaseAdapter
{
	ArrayList<String> ltime,lname,lamount,lreason;
	Context context;
	public MyAdapter(Context mContext,ArrayList<String> time,ArrayList<String> name,ArrayList<String> amount,ArrayList<String> reason)
	{
		this.context=mContext;
		this.lamount=amount;	this.lname=name;
		this.lreason=reason;	this.ltime=time;
	}

	@Override
	public int getCount()
	{
		// TODO: Implement this method
		return lname.size();
	}

	@Override
	public Object getItem(int p1)
	{
		// TODO: Implement this method
		return lname.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		// TODO: Implement this method
		return p1;
	}

	@Override
	public View getView(int p1, View p2, ViewGroup p3)
	{
		p2=((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
			inflate(R.layout.list,null);
		// TODO: Implement this method
		((TextView)p2.findViewById(R.id.tvAmount)).
			setText(lamount.get(p1));
		((TextView)p2.findViewById(R.id.tvName)).
			setText(lname.get(p1));
		((TextView)p2.findViewById(R.id.tvReason)).
			setText(lreason.get(p1));
		((TextView)p2.findViewById(R.id.tvTime)).
			setText(ltime.get(p1));
		return p2;
	}
}
