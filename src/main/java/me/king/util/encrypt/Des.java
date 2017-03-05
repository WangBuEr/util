package me.king.util.encrypt;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;

/**
 * 
 * @Title:
 * @Description: des加解密
 * @Author:BuEr
 * @Since:2016年8月31日
 * @Version:1.1.0
 */
public class Des {
	/**
	 * 
	 * @param data 需要加密的数据
	 * @param key 加密键
	 * @return 加密之后的数据
	 * @throws Exception  
	 * @Description: 根据键值进行加密
	 */
	public static String encrypt(String data, String key){
		Preconditions.checkNotNull(data);
		Preconditions.checkNotNull(key);
		byte[] bt = encrypt(data.getBytes(), key.getBytes());
		BaseEncoding baseEncoding = BaseEncoding.base64Url();
		return baseEncoding.encode(bt);
	}
	/**
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws IOException
	 * @throws Exception 
	 * @Description:
	 */
	public static String decrypt(String data, String key){
		Preconditions.checkNotNull(data);
		Preconditions.checkNotNull(key);
		BaseEncoding baseEncoding = BaseEncoding.base64Url();
		byte[] buf = baseEncoding.decode(data);
		byte[] bt = decrypt(buf, key.getBytes());
		return new String(bt);
	}

	/**
	 * Description 根据键值进行加密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key){
		try {
			// 生成一个可信任的随机数源
			SecureRandom sr = new SecureRandom();
			// 从原始密钥数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);
			// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(dks);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密钥初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Description 根据键值进行解密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, byte[] key){
		try {
			// 生成一个可信任的随机数源
			SecureRandom sr = new SecureRandom();
			// 从原始密钥数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);
			// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(dks);
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密钥初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
