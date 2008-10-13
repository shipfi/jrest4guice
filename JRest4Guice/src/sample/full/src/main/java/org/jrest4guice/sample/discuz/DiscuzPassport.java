package org.jrest4guice.sample.discuz;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import sun.misc.BASE64Decoder;

@SuppressWarnings("unchecked")
public class DiscuzPassport {

	public static String encrypt(String src, String key) {
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());
		String rand = "" + random.nextInt() % 32000;
		String encKey = DiscuzPassport.md5(rand);

		int ctr = 0;
		String tmp = "";

		for (int i = 0; i < src.length(); i++) {
			ctr = (ctr == encKey.length() ? 0 : ctr);
			tmp += encKey.charAt(ctr);
			char c = (char) (src.charAt(i) ^ encKey.charAt(ctr));
			tmp += c;
			ctr++;
		}
		String passportKey = passportKey(tmp, key);
		return new sun.misc.BASE64Encoder().encode(passportKey.getBytes());
	}

	public static String decrypt(String src, String key) {
		byte[] bytes = null;
		try {
			bytes = new BASE64Decoder().decodeBuffer(src);
			src = new String(bytes);
		} catch (Exception e) {
			return null;
		}
		src = passportKey(src, key);

		String tmp = "";
		for (int i = 0; i < src.length(); ++i) {
			char c = (char) (src.charAt(i) ^ src.charAt(++i));
			tmp += c;
		}
		return tmp;
	}

	public static String passportKey(String src, String key) {
		String encKey = DiscuzPassport.md5(key);

		int ctr = 0;
		String tmp = "";
		for (int i = 0; i < src.length(); ++i) {
			ctr = (ctr == encKey.length() ? 0 : ctr);
			char c = (char) (src.charAt(i) ^ encKey.charAt(ctr));
			tmp += c;
			ctr++;
		}
		return tmp;
	}

	public static String passportEncode(Map data) {
		Set keys = data.keySet();
		String key = "";
		String ret = "";
		Iterator iterator = keys.iterator();
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			try {
				ret += key + "=" + (String) data.get(key) + "&";
			} catch (Exception e) {
				return "";
			}
		}
		if (ret.length() > 0)
			return ret.substring(0, ret.length() - 1);
		return "";
	}

	/**
	 * Md5加密
	 * 
	 * @param x
	 * @return
	 * @throws Exception
	 */
	public static String md5(String x) {

		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
			m.update(x.getBytes("UTF8")); // 更新被文搞描述的位元组
		} catch (NoSuchAlgorithmException e) {
			// 创建一个MD5消息文搞 的时候出错
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// 更新被文搞描述的位元组 的时候出错
			e.printStackTrace();
		}
		byte s[] = m.digest(); // 最后更新使用位元组的被叙述的排列,然后完成文摘计算
		// System.out.println(s); // 输出加密后的位元组
		String result = "";
		for (int i = 0; i < s.length; i++) {
			result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00)
					.substring(6);
			// 进行十六进制转换
		}

		return result;

	}

}