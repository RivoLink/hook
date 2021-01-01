package mg.broadcast.hook;

import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

import mg.broadcast.hook.handler.NetHandler;

public class NetReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context,Intent intent){
		NetHandler handler=new NetHandler(context);
		
		if(handler.isOnline())
			handler.postStack();
			
	}
	
}
