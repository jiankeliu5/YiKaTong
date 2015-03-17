package com.seedsoft.ykt.util;

import java.security.MessageDigest;

public class MD5Util {

	public final static String MD5(String pwd) {

		// 鐢ㄤ簬鍔犲瘑鐨勫瓧锟�

		char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',

		'A', 'B', 'C', 'D', 'E', 'F' };

		try {

			// 浣跨敤骞冲彴鐨勯粯璁ゅ瓧绗﹂泦灏嗘 String 缂栫爜锟�byte搴忓垪锛屽苟灏嗙粨鏋滃瓨鍌ㄥ埌锟�锟斤拷鏂扮殑
			// byte鏁扮粍锟�

			byte[] btInput = pwd.getBytes("UTF-8");

			// 鑾峰緱鎸囧畾鎽樿绠楁硶锟�MessageDigest瀵硅薄锛屾澶勪负MD5

			// MessageDigest绫讳负搴旂敤绋嬪簭鎻愪緵淇℃伅鎽樿绠楁硶鐨勫姛鑳斤紝锟�MD5 锟�SHA 绠楁硶锟�

			// 淇℃伅鎽樿鏄畨鍏ㄧ殑鍗曞悜鍝堝笇鍑芥暟锛屽畠鎺ユ敹浠绘剰澶у皬鐨勬暟鎹紝骞惰緭鍑哄浐瀹氶暱搴︾殑鍝堝笇鍊硷拷?

			MessageDigest mdInst = MessageDigest.getInstance("MD5");

			// MD5 Message Digest from SUN, <initialized>

			// MessageDigest瀵硅薄閫氳繃浣跨敤 update鏂规硶澶勭悊鏁版嵁锟�浣跨敤鎸囧畾鐨刡yte鏁扮粍鏇存柊鎽樿

			mdInst.update(btInput);

			// MD5 Message Digest from SUN, <in progress>

			// 鎽樿鏇存柊涔嬪悗锛岋拷?杩囪皟鐢╠igest锛堬級鎵ц鍝堝笇璁＄畻锛岃幏寰楀瘑锟�

			byte[] md = mdInst.digest();

			// System.out.println(md);

			// 鎶婂瘑鏂囪浆鎹㈡垚鍗佸叚杩涘埗鐨勫瓧绗︿覆褰㈠紡

			int j = md.length;

			// System.out.println(j);

			char str[] = new char[j * 2];

			int k = 0;

			for (int i = 0; i < j; i++) { // i = 0

				byte byte0 = md[i]; // 95

				str[k++] = md5String[byte0 >>> 4 & 0xf]; // 5

				str[k++] = md5String[byte0 & 0xf]; // F

			}

			// 杩斿洖缁忚繃鍔犲瘑鍚庣殑瀛楃锟�

			return new String(str);

		} catch (Exception e) {

			e.printStackTrace();

			return null;
		}

	}

}
