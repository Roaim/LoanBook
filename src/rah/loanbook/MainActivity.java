package rah.loanbook;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import java.io.*;
import org.json.*;
import java.util.*;


public class MainActivity extends Activity
{
    Context c=MainActivity.this;
	ListView lvBorrow,lvLend;
	TextView tvTotalBorrow,tvTotalLend;
	LoanHelper lHelper=new LoanHelper(c);
	SetGet sg=new SetGet();

	public void initialize()
	{
		lvBorrow=(ListView) findViewById(R.id.lvBorrow);
		lvLend=(ListView) findViewById(R.id.lvLend);
		tvTotalBorrow=(TextView) findViewById(R.id.tvTotalBorrow);
		tvTotalLend=(TextView) findViewById(R.id.tvTotalLend);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		initialize();
		createNewJsonFile();
		lHelper.makeListView(LoanHelper.ARRAY_BORROW,lvBorrow);
		lHelper.makeListView(LoanHelper.ARRAY_LEND,lvLend);
    }

	private void createNewJsonFile()
	{
		try{
			if(lHelper.file().getTotalSpace()==0){
			JSONObject jo=new JSONObject();
			JSONArray ja=new JSONArray();
			jo.put(LoanHelper.ARRAY_BORROW,ja);
			jo.put(LoanHelper.ARRAY_LEND,ja);
			lHelper.writeToFile(jo);
			lHelper.makeToast("New Json file created: "+LoanHelper.FILE_NAME,LoanHelper.TOAST_SHORT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			lHelper.makeToast(e.toString(),LoanHelper.TOAST_SHORT);
		}
	}
	
	/************************************************************/

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add("Add").setIcon(R.drawable.ic_action_new).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

				@Override
				public boolean onMenuItemClick(MenuItem p1)
				{
					lHelper.alertDialog(LoanHelper.ADD,555);
					return false;
				}
			});
		return super.onCreateOptionsMenu(menu);
	}
	
	/************************************************************/
	
}
