package examples;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.vision.util.ValidationUtil;

public class DownloadFile {

	public static void main(String[] args) {
		try {
			ByteArrayInputStream in = null;
			FileOutputStream fos = null;
			String filePath = System.getProperty("java.io.tmpdir");
			if (!ValidationUtil.isValid(filePath)) {
				filePath = System.getenv("TMP");
			}
			if (ValidationUtil.isValid(filePath)) {
				filePath = filePath + File.separator;
			}
			System.out.println(filePath);
			String fileName = "customers.xlsx";
			File lFile = null;
			lFile = new File(filePath + fileName);
			if(lFile.exists()){
				lFile.delete();
			}
			fos = new FileOutputStream(lFile);
			int bit = 4096;
			while ((bit) >= 0) {
				bit = in.read();
				fos.write(bit);
			}
			in.close();
			fos.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
