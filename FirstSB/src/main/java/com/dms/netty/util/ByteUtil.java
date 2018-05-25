package com.dms.netty.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.stream.FileImageInputStream;

public class ByteUtil {

	public static byte[] image2byte(String path){
	    byte[] data = null;
	    FileImageInputStream input = null;
	    try {
	      input = new FileImageInputStream(new File(path));
	      ByteArrayOutputStream output = new ByteArrayOutputStream();
	      byte[] buf = new byte[1024];
	      int numBytesRead = 0;
	      while ((numBytesRead = input.read(buf)) != -1) {
	      output.write(buf, 0, numBytesRead);
	      }
	      data = output.toByteArray();
	      output.close();
	      input.close();
	    }
	    catch (FileNotFoundException ex1) {
	      ex1.printStackTrace();
	    }
	    catch (IOException ex1) {
	      ex1.printStackTrace();
	    }
	    return data;
	  }
	/**
	 * //Original byte[]
        byte[] bytes = "hello world".getBytes();
         
        //Base64 Encoded
        String encoded = Base64.getEncoder().encodeToString(bytes);
         
        //Base64 Decoded
        byte[] decoded = Base64.getDecoder().decode(encoded);
         
        //Verify original content
        System.out.println( new String(decoded) );
	 */
	public static String Base64CgByteToString(byte[] bytes){
		return Base64.getEncoder().encodeToString(bytes);
	}
	
}
