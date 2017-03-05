package me.king.util.encrypt;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5 {
	public static String encrypt(String data){
		return DigestUtils.md5Hex(data);
	}
}
