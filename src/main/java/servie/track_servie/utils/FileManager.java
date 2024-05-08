package servie.track_servie.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import org.springframework.stereotype.Component;

@Component
public class FileManager
{
	public void downloadFile(String fileUrl, String savePath) throws IOException
	{
		try
		{
			URL url = new URI(fileUrl).toURL();
			BufferedInputStream in = new BufferedInputStream(url.openStream());
			FileOutputStream fileOutputStream = new FileOutputStream(savePath);
			byte[] dataBuffer = new byte[1024];
			int bytesRead;
			while((bytesRead = in.read(dataBuffer, 0, 1024))!=-1)
				fileOutputStream.write(dataBuffer, 0, bytesRead);
			fileOutputStream.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void extractGzFile(String gzFilePath, String outputFilePath)
	{
		try
		{
			FileInputStream fileInputStream = new FileInputStream(gzFilePath);
			GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
			FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);
			byte[] buffer = new byte[1024];
			int bytesRead;
			while((bytesRead = gzipInputStream.read(buffer))>0)
				fileOutputStream.write(buffer, 0, bytesRead);
			fileOutputStream.close();
			gzipInputStream.close();
			System.out.println("File extraction completed successfully.");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
