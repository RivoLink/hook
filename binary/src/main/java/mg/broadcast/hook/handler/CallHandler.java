package mg.broadcast.hook.handler;
import android.content.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class CallHandler extends Handler{

	public CallHandler(Context context){
		super(context);
	}
	
	public boolean save(String state,boolean date,boolean newline){
		if(dir.exists() || dir.mkdir()) try{

				File file=new File(dir,FILE_NAME);
				if(!file.exists())
					file.createNewFile();

				FileOutputStream out=new FileOutputStream(file,true);
				OutputStreamWriter writer=new OutputStreamWriter(out);
				writer.append(state);
				if(date) writer.append(new SimpleDateFormat(" HH:mm:ss").format(new Date()));
				if(newline) writer.append("\n");
				writer.close();
				out.close();

				return true;
			}
			catch(IOException e){
			}

		return false;
	}
	
	
}
