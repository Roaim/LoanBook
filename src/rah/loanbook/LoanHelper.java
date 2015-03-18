package rah.loanbook;
import android.content.Context;
import android.app.AlertDialog;
import android.view.*;
import android.app.*;
import android.content.*;
import android.widget.*;
import java.io.*;
import org.json.*;
import java.util.*;

public class LoanHelper
{
	Context c;
	View v;
	SetGet sg=new SetGet();
	
	int loanType;
	int position;
	
	public static final String PASSWORD="rah";
	public static final int TOAST_LONG=1;
	public static final int TOAST_SHORT=0;
	public static final int EDIT=0;
	public static final int ADD=1;
	public static final int DELETE=2;
	public static final int BORROW=1;
	public static final int LEND=0;
	////////////////////////////////////////
	public static final String FILE_NAME="loan.json";
	public static final String ARRAY_BORROW="borrow";
	public static final String ARRAY_LEND="lend";
	public static final String NAME_JO="name";
	public static final String TIME_JO="time";
	public static final String AMOUNT_JO="amount";
	public static final String REASON_JO="reason";
	
	public LoanHelper(Context context)
	{
		this.c=context;
	}
	
	public void setLoanType(int type)
	{
		this.loanType=type;
	}
	
	public int getLoanType()
	{	
		return this.loanType;
	}
	
	public void alertDialog(final int mode,final int Position)
	{
		this.position=Position;
		v=((LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
		inflate(R.layout.add_loan,null);
		AlertDialog.Builder dialog=new AlertDialog.Builder(c);
		dialog.setTitle("Add new");
		dialog.setView(v);
		loanType();
		dialog.setNeutralButton("Submit", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					String pass,name,amount,reason;
					try{
					pass=((EditText)v.findViewById(R.id.etPass)).
					getText().toString();
					name=((EditText)v.findViewById(R.id.etName)).
					getText().toString();
					amount=((EditText)v.findViewById(R.id.etAmount)).
					getText().toString();
					reason=((EditText)v.findViewById(R.id.etReason)).
					getText().toString();
					if(pass.equalsIgnoreCase(PASSWORD)){
					sg.setEditText(name,amount,reason);
					makeToast("Submitted",TOAST_SHORT);
					if(mode==ADD){
						addMethod();
					} else if (mode==EDIT){
						editMethod();
					}
					}else{makeToast("Wrong password",TOAST_SHORT);}
					} catch (Exception e){
						e.printStackTrace();
						makeToast("Error: "+e.toString(),TOAST_SHORT);
					}
				}
			});
		dialog.show();	
	}
	
	public File file()
	{
		File file=new File(c.getExternalFilesDir(null),LoanHelper.FILE_NAME);
		return file;
	}
	
	public String ReadFromFile()
	{
		byte[] bytes = null;
		try{
			int fileLength=(int)file().length();
			bytes=new byte[fileLength];
			FileInputStream fis=new FileInputStream(file());
			fis.read(bytes);
			fis.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return new String(bytes);
	}
	
	public void deleteDialog(final String Jarray,int Position)
	{
		this.position=Position;
		v=((LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
			inflate(R.layout.add_loan,null);
		AlertDialog.Builder dialog=new AlertDialog.Builder(c);
		dialog.setTitle("Delete?");
		dialog.setView(v);
		((EditText)v.findViewById(R.id.etName)).
			setVisibility(View.GONE);
		((EditText)v.findViewById(R.id.etAmount)).
			setVisibility(View.GONE);
		((EditText)v.findViewById(R.id.etReason)).
			setVisibility(View.GONE);
		((Spinner)v.findViewById(R.id.spinner)).
		setVisibility(View.GONE);
		dialog.setNeutralButton("Delete", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					String pass=((EditText)v.findViewById(R.id.etPass)).
						getText().toString();
					if(pass.equalsIgnoreCase(PASSWORD)){
						modify(DELETE,Jarray);
					} else{
						makeToast("Wrong password|",TOAST_SHORT);
					}
				}
			});
		dialog.show();
	}
	
	public void modify(int mode, String JARRAY)
	{
			try{
			JSONObject jo=new JSONObject(ReadFromFile());
			JSONArray ja=jo.optJSONArray(JARRAY);
			JSONArray jaMod=new JSONArray();
			for(int i=0;i<ja.length();i++){
				if(i!=position){
					jaMod.put(ja.get(i));
				} else if(mode==EDIT){
					//DO SOMETHING
				}
			}
			jo.put(JARRAY,jaMod);
			writeToFile(jo);
			makeListView(JARRAY);
			}catch (Exception e){
				e.printStackTrace();
				makeToast(e.toString(),TOAST_SHORT);
			}
	}
	
	public void makeListView(String JAName)
	{
		ListView lv = null;
		if(ARRAY_BORROW==JAName){
			lv=(ListView)((Activity)c).findViewById(R.id.lvBorrow);
		}else if(ARRAY_LEND==JAName){
			lv=(ListView)((Activity)c).findViewById(R.id.lvLend);
		}
		ArrayList<String> time,name,amount,reason;
		time=new ArrayList<String>();
		name=new ArrayList<String>();
		amount=new ArrayList<String>();
		reason=new ArrayList<String>();
		try{
			JSONObject jo=new JSONObject(ReadFromFile());
			JSONArray ja=jo.optJSONArray(JAName);
			for(int i=0;i<ja.length();i++){
				JSONObject nJP=ja.getJSONObject(i);
				time.add(nJP.optString(TIME_JO).toString());
				name.add(nJP.optString(NAME_JO).toString());
				amount.add(nJP.optString(AMOUNT_JO).toString());
				reason.add(nJP.optString(REASON_JO).toString());
			}
		lv.setAdapter(new MyAdapter(c,time,name,amount,reason));
		} catch ( Exception e ) {
			e.printStackTrace();
			makeToast(e.toString(),TOAST_SHORT);
		}
	}
	
	public void writeToFile(JSONObject jo)
	{
		try{
			FileOutputStream fos=new FileOutputStream(file());
			fos.write(jo.toString().getBytes());
			fos.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void writeJsonToFile(String JAName)
	{
		try{
			JSONObject jo=new JSONObject(ReadFromFile());
			JSONArray ja=jo.optJSONArray(JAName);
				JSONObject nJO=new JSONObject();
				nJO.put(TIME_JO,sg.getTime());
				nJO.put(NAME_JO,sg.getName());
				nJO.put(AMOUNT_JO,sg.getAmount());
				nJO.put(REASON_JO,sg.getReason());
			ja.put(nJO);
			jo.put(JAName,ja);
			
			writeToFile(jo);
		} catch (Exception e) {
			e.printStackTrace();
			makeToast(e.toString(),TOAST_SHORT);
		}
	}
	
	public void addMethod()
	{
		if(getLoanType()==LEND){
			addToLendJsonArray();
		}else if(getLoanType()==BORROW){
			addToBorrowJsonArray();
		}
	}

	private void addToBorrowJsonArray()
	{
		writeJsonToFile(ARRAY_BORROW);
		makeListView(ARRAY_BORROW);
	}

	private void addToLendJsonArray()
	{
		writeJsonToFile(ARRAY_LEND);
		makeListView(ARRAY_LEND);
	}
	
	public void editMethod()
	{
		if(getLoanType()==LEND){
			editLendJsonArray();
		} else if(getLoanType()==BORROW){
			editBorrowJsonArray();
		}
	}

	private void editBorrowJsonArray()
	{
		// TODO: Implement this method
	}

	private void editLendJsonArray()
	{
		// TODO: Implement this method
	}
	
	public void loanType()
	{
		Spinner spinner=(Spinner)v.findViewById(R.id.spinner);
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					setLoanType(p3);
				}


				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					
				}
			});
	}
	
	public void makeToast(String message,int Mode)
	{
		if (Mode==TOAST_LONG){
			Toast.makeText(c,message,Toast.LENGTH_LONG).show();
		} else if (Mode==TOAST_SHORT){
			Toast.makeText(c,message,Toast.LENGTH_SHORT).show();
		}
	}
	
}
