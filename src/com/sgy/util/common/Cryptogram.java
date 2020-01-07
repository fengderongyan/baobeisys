package com.sgy.util.common;

public class Cryptogram {

	public static String decrypt(String input) {
		int str_len, p1;
		String str1, str2, Str;
		StringBuffer Result = new StringBuffer("");
		Str = "bssp";
		if (input.length() != 32) {
			return Result.toString();
		}
		str2 = input.substring(30, 32);
		str_len = Integer.parseInt(String.valueOf(str2.charAt(0)));
		if (str2.charAt(1) == 'A' || str2.charAt(1) == 'a') {
			str_len = str_len * 16 + 10;
		} else if (str2.charAt(1) == 'B' || str2.charAt(1) == 'b') {
			str_len = str_len * 16 + 11;
		} else if (str2.charAt(1) == 'C' || str2.charAt(1) == 'c') {
			str_len = str_len * 16 + 12;
		} else if (str2.charAt(1) == 'D' || str2.charAt(1) == 'd') {
			str_len = str_len * 16 + 13;
		} else if (str2.charAt(1) == 'E' || str2.charAt(1) == 'e') {
			str_len = str_len * 16 + 14;
		} else if (str2.charAt(1) == 'F' || str2.charAt(1) == 'f') {
			str_len = str_len * 16 + 15;
		} else {
			str_len = str_len * 16
					+ Integer.parseInt(String.valueOf(str2.charAt(1)));
		}
		str_len = str_len - 6;
		for (int i = 1; i <= str_len; i++) {
			str2 = input.substring((i - 1) * 2, (i - 1) * 2 + 2);
			p1 = 0;
			if (str2.charAt(0) == 'A' || str2.charAt(0) == 'a') {
				p1 = p1 + 10 * 16;
			} else if (str2.charAt(0) == 'B' || str2.charAt(0) == 'b') {
				p1 = p1 + 11 * 16;
			} else if (str2.charAt(0) == 'C' || str2.charAt(0) == 'c') {
				p1 = p1 + 12 * 16;
			} else if (str2.charAt(0) == 'D' || str2.charAt(0) == 'd') {
				p1 = p1 + 13 * 16;
			} else if (str2.charAt(0) == 'E' || str2.charAt(0) == 'e') {
				p1 = p1 + 14 * 16;
			} else if (str2.charAt(0) == 'F' || str2.charAt(0) == 'f') {
				p1 = p1 + 15 * 16;
			} else {
				p1 = p1 + Integer.parseInt(String.valueOf(str2.charAt(0))) * 16;
			}

			if (str2.charAt(1) == 'A' || str2.charAt(1) == 'a') {
				p1 = p1 + 10;
			} else if (str2.charAt(1) == 'B' || str2.charAt(1) == 'b') {
				p1 = p1 + 11;
			} else if (str2.charAt(1) == 'C' || str2.charAt(1) == 'c') {
				p1 = p1 + 12;
			} else if (str2.charAt(1) == 'D' || str2.charAt(1) == 'd') {
				p1 = p1 + 13;
			} else if (str2.charAt(1) == 'E' || str2.charAt(1) == 'e') {
				p1 = p1 + 14;
			} else if (str2.charAt(1) == 'F' || str2.charAt(1) == 'f') {
				p1 = p1 + 15;
			} else {
				p1 = p1 + Integer.parseInt(String.valueOf(str2.charAt(1)));
				
			}

			p1 = p1 - (int) Str.charAt(((i - 1) % 4));

			str1 = String.valueOf((char) p1);
			Result = Result.append(str1);

		}
		return Result.toString();
	}

	public static String encrypt(String input) {
		int str_len, p1;
		char str1;
		String Str = "bssp", str2;
		String Passwd = "";
		String Result = "";
		str_len = input.length();
		for (int i = 1; i <= str_len; i++) {
			str1 = input.charAt(i - 1);
			p1 = (int) (str1) + (int) (Str.charAt((i - 1) % 4));
			if (Integer.toHexString(p1).length() < 2) {
				str2 = "0" + Integer.toHexString(p1);
			} else {
				str2 = Integer.toHexString(p1);
			}
			if (str2.length() != 2) {
				str2 = String.valueOf(i) + str2;
			}

			Passwd = Passwd + str2;
		}
		for (int i = 1; i <= 15 - str_len; i++) {
			String temp = Integer.toHexString((int) (Str.charAt((i - 1) % 4))
					+ str_len);
			if (temp.length() < 2) {
				Passwd = Passwd + "0" + temp;
			} else {
				Passwd = Passwd + temp;
			}
		}

		if (Integer.toHexString(str_len + 6).length() < 2) {
			Result = Passwd + "0" + Integer.toHexString(str_len + 6);
		} else {
			Result = Passwd + Integer.toHexString(str_len + 6);
		}

		return Result;
	}

	public static void main(String[] args) {
	 	//String s = Passwd_UnBind("93a5a6a497a96879797668797976680c");
		//String s ="zz123456";
		//int n=(int)(s.charAt(1))+(int)(s.charAt(0));
		String s = encrypt("199999");
		System.out.println(s);
	}

}
