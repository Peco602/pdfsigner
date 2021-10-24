import java.nio.channels.*;
import java.io.*;

public class FileCopy {
	File sfile;
	File dfile;
	
	FileCopy(String s, String d) throws Exception {
		sfile = new File(s);
		dfile = new File(d);
		copyFile();
	}
	
	@SuppressWarnings("resource")
	public void copyFile() throws Exception
	{
		FileChannel source = new FileInputStream(sfile).getChannel();
		FileChannel dest = new FileOutputStream(dfile).getChannel();
		source.transferTo(0, source.size(), dest);
		source.close();
		dest.close();
	}

}
