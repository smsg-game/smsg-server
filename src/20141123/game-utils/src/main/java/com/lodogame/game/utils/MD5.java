package com.lodogame.game.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    private final static String[] hexDigits={"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb=new StringBuffer();
        for(int i=0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));  
        }
        return resultSb.toString();
    }
    
   private static String byteToHexString(byte b) {
        int n=b;
        if(n < 0) {
            n=256 + n;
        }
        int d1=n / 16;
        int d2=n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin) {
        String resultString=null;
        try {
            resultString=new String(origin);
            MessageDigest md=MessageDigest.getInstance("MD5");
            resultString=byteArrayToHexString(md.digest(resultString.getBytes("ISO-8859-1")));
        } catch(Exception ex) {
        }
        return resultString;
    }
    
    public static String MD5Encode(String origin,String code) {
        String resultString=null;
        try {
            resultString=new String(origin);
            MessageDigest md=MessageDigest.getInstance("MD5");
            resultString=byteArrayToHexString(md.digest(resultString.getBytes(code)));
        } catch(Exception ex) {
        }
        return resultString;
    }

    /**
     * 生成文件的 MD5 签名
     * @param path 文件路径
     * @return 生成的 MD5 签名
     * @throws NoSuchAlgorithmException 
     * @throws IOException 
     */
	public static String MD5Checksum(String path) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		FileInputStream fis = new FileInputStream(path);

		byte[] dataBytes = new byte[1024];

		int nread = 0;
		while ((nread = fis.read(dataBytes)) != -1) {
			md.update(dataBytes, 0, nread);
		}

		byte[] mdbytes = md.digest();

		// convert the byte to hex format method 2
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < mdbytes.length; i++) {
			String hex = Integer.toHexString(0xff & mdbytes[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();

	}
    
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        String data="mid=237&gid=2&sid=1&uif=2.249163&cardno=3pvG8fU3/TUwqQMo5Vqmcg==&cardpwd=3pvG8fU3/TXra8UXGkYiR/6CQ6d6/raA&utp=0&uip=220.231.155.194&eif=Y5060162242&amount=10&timestamp=20110110180711&verstring=Vx&@113*";
        System.out.println(MD5Encode(data));
//    		System.out.println(MD5Checksum("/Users/chengevo/1.txt"));
    	
    }
}
