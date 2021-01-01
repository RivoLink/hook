package mg.broadcast.hook;

import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.telephony.SmsMessage;

import mg.broadcast.hook.handler.SmsHandler;

public class SmsReceiver extends BroadcastReceiver{
	
	final String ACTION_RECEIVE_SMS="android.provider.Telephony.SMS_RECEIVED";    
	
	@Override
	public void onReceive(Context context,Intent intent){
		
		if(intent.getAction().equals(ACTION_RECEIVE_SMS)){
			Bundle bundle=intent.getExtras();
			
			if(bundle!=null){      
				Object[] pdus=(Object[])bundle.get("pdus"); 
				
				if(pdus==null)
					return;
				
				final SmsMessage[] messages=new SmsMessage[pdus.length]; 

				for(int i=0;i<pdus.length;i++){   
					messages[i]=SmsMessage.createFromPdu((byte[])pdus[i]);   
				}
				
				if(messages.length>0){ 
					final String messageBody=messages[0].getMessageBody(); 
					final String phoneNumber=messages[0].getDisplayOriginatingAddress(); 
					
					new SmsHandler(context).handle(phoneNumber,messageBody);
				
				}
			} 
		}
	}
	
}
