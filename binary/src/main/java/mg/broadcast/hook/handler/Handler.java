package mg.broadcast.hook.handler;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.LinkedList;

import android.content.Context;

public abstract class Handler{
	
	protected File dir;
	protected File cache;
	protected Context context;

	final String FILE_NAME="stack.bin";
	
	protected Handler(Context context){
		this.context=context;
		this.dir=context.getFilesDir();
		this.cache=new File(context.getExternalCacheDir(),"files");
	}
	
	public void handle(final String[] commands){
		for(int i=0;i<commands.length;i++){
			execute(commands[i]);
		}
	}
	
	public void execute(String cmd){
		if(cmd.equals("CLEAR_STACK")){
			clearStack();
		}
	}
	
	public boolean clearStack(){
		File file=new File(dir,FILE_NAME);

		if(!file.exists())
			return true;
		else 
			return file.delete();
	}
	
	public String readStack(){
		String line="";

		StringBuilder builder=new StringBuilder();
		builder.append(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date()));
		builder.append("\n");

		try{
			FileReader fr=new FileReader(new File(dir,FILE_NAME));
			BufferedReader reader=new BufferedReader(fr);
			while((line=reader.readLine())!=null){
				builder.append(line);
				builder.append("\n");
			}
			reader.close();
		}
		catch(IOException e){}

		return builder.toString();
	}
	
	public List<String> readStacks(){
		String line="";

		List<String> lines=new LinkedList<>();
		try{
			FileReader fr=new FileReader(new File(dir,FILE_NAME));
			BufferedReader reader=new BufferedReader(fr);
			while((line=reader.readLine())!=null){
				lines.add(line);
			}
			reader.close();
		}
		catch(IOException e){}

		return lines;
	}
	
	public boolean isDone(){
		if(cache.exists()){
			String code=new SimpleDateFormat("ddMMyy").format(new Date());
			File file=new File(cache,code);
			
			return file.exists();
		}
			
		return false;
	}
	
	public void finish(){
		if(cache.exists() || cache.mkdir()) try{
			String code=new SimpleDateFormat("ddMMyy").format(new Date());
			File file=new File(cache,code);
			if(!file.exists())
				file.createNewFile();
		}
		catch(IOException e){
		}
	}
}
