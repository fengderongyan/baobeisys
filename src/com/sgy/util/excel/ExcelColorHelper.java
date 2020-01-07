package com.sgy.util.excel;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;

public class ExcelColorHelper {

	public ExcelColorHelper() {
	}

	public static String getHex(String strHex) {
		if (strHex.length() > 0) {
			String[] a = strHex.split(":");
			strHex = "";
			for (int n = 0; n < a.length; n++) {
				if (a[n].length() > 0) {
					if (a[n].length() < 2) {
						strHex += "0" + a[n];
					} else {
						strHex += a[n].substring(0, 2);
					}
				}
			}
		}
		return strHex.length() > 0 ? strHex : "";

	}

	public static String getHex(HSSFColor c) {
		return getHex(c == null ? "" : c.getHexString());
	}

	public static String getHex(int nColor) {
		String strHex = "";
		List list = new ArrayList();
		
		if (nColor == HSSFColor.LIGHT_CORNFLOWER_BLUE.index) {
			strHex = HSSFColor.LIGHT_CORNFLOWER_BLUE.hexString;
		}
		if (nColor == HSSFColor.ROYAL_BLUE.index) {
			strHex = HSSFColor.ROYAL_BLUE.hexString;
		}
		if (nColor == HSSFColor.CORAL.index) {
			strHex = HSSFColor.CORAL.hexString;
		}
		if (nColor == HSSFColor.ORCHID.index) {
			strHex = HSSFColor.ORCHID.hexString;
		}
		if (nColor == HSSFColor.MAROON.index) {
			strHex = HSSFColor.MAROON.hexString;
		}
		if (nColor == HSSFColor.LEMON_CHIFFON.index) {
			strHex = HSSFColor.LEMON_CHIFFON.hexString;
		}
		if (nColor == HSSFColor.CORNFLOWER_BLUE.index) {
			strHex = HSSFColor.CORNFLOWER_BLUE.hexString;
		}
		if (nColor == HSSFColor.WHITE.index) {
			strHex = HSSFColor.WHITE.hexString;
		}
		if (nColor == HSSFColor.LAVENDER.index) {
			strHex = HSSFColor.LAVENDER.hexString;
		}
		if (nColor == HSSFColor.PALE_BLUE.index) {
			strHex = HSSFColor.PALE_BLUE.hexString;
		}
		if (nColor == HSSFColor.LIGHT_TURQUOISE.index) {
			strHex = HSSFColor.LIGHT_TURQUOISE.hexString;
		}
		if (nColor == HSSFColor.LIGHT_GREEN.index) {
			strHex = HSSFColor.LIGHT_GREEN.hexString;
		}
		if (nColor == HSSFColor.LIGHT_YELLOW.index) {
			strHex = HSSFColor.LIGHT_YELLOW.hexString;
		}
		if (nColor == HSSFColor.TAN.index) {
			strHex = HSSFColor.TAN.hexString;
		}
		if (nColor == HSSFColor.ROSE.index) {
			strHex = HSSFColor.ROSE.hexString;
		}
		if (nColor == HSSFColor.GREY_25_PERCENT.index) {
			strHex = HSSFColor.GREY_25_PERCENT.hexString;
		}
		if (nColor == HSSFColor.PLUM.index) {
			strHex = HSSFColor.PLUM.hexString;
		}
		if (nColor == HSSFColor.SKY_BLUE.index) {
			strHex = HSSFColor.SKY_BLUE.hexString;
		}
		if (nColor == HSSFColor.TURQUOISE.index) {
			strHex = HSSFColor.TURQUOISE.hexString;
		}
		if (nColor == HSSFColor.BRIGHT_GREEN.index) {
			strHex = HSSFColor.BRIGHT_GREEN.hexString;
		}
		if (nColor == HSSFColor.YELLOW.index) {
			strHex = HSSFColor.YELLOW.hexString;
		}
		if (nColor == HSSFColor.GOLD.index) {
			strHex = HSSFColor.GOLD.hexString;
		}
		if (nColor == HSSFColor.PINK.index) {
			strHex = HSSFColor.PINK.hexString;
		}
		if (nColor == HSSFColor.GREY_40_PERCENT.index) {
			strHex = HSSFColor.GREY_40_PERCENT.hexString;
		}
		if (nColor == HSSFColor.VIOLET.index) {
			strHex = HSSFColor.VIOLET.hexString;
		}
		if (nColor == HSSFColor.LIGHT_BLUE.index) {
			strHex = HSSFColor.LIGHT_BLUE.hexString;
		}
		if (nColor == HSSFColor.AQUA.index) {
			strHex = HSSFColor.AQUA.hexString;
		}
		if (nColor == HSSFColor.SEA_GREEN.index) {
			strHex = HSSFColor.SEA_GREEN.hexString;
		}
		if (nColor == HSSFColor.LIME.index) {
			strHex = HSSFColor.LIME.hexString;
		}
		if (nColor == HSSFColor.LIGHT_ORANGE.index) {
			strHex = HSSFColor.LIGHT_ORANGE.hexString;
		}
		if (nColor == HSSFColor.BLACK.index) {
			strHex = HSSFColor.BLACK.hexString;
		}
		if (nColor == HSSFColor.BLUE_GREY.index) {
			strHex = HSSFColor.BLUE_GREY.hexString;
		}
		if (nColor == HSSFColor.BLUE.index) {
			strHex = HSSFColor.BLUE.hexString;
		}
		if (nColor == HSSFColor.BROWN.index) {
			strHex = HSSFColor.BROWN.hexString;
		}
		if (nColor == HSSFColor.DARK_BLUE.index) {
			strHex = HSSFColor.DARK_BLUE.hexString;
		}
		if (nColor == HSSFColor.DARK_GREEN.index) {
			strHex = HSSFColor.DARK_GREEN.hexString;
		}
		if (nColor == HSSFColor.DARK_RED.index) {
			strHex = HSSFColor.DARK_RED.hexString;
		}
		if (nColor == HSSFColor.DARK_RED.index) {
			strHex = HSSFColor.DARK_RED.hexString;
		}
		if (nColor == HSSFColor.DARK_TEAL.index) {
			strHex = HSSFColor.DARK_TEAL.hexString;
		}
		if (nColor == HSSFColor.DARK_YELLOW.index) {
			strHex = HSSFColor.DARK_YELLOW.hexString;
		}
		if (nColor == HSSFColor.GREEN.index) {
			strHex = HSSFColor.GREEN.hexString;
		}
		if (nColor == HSSFColor.GREY_25_PERCENT.index) {
			strHex = HSSFColor.GREY_25_PERCENT.hexString;
		}
		if (nColor == HSSFColor.GREY_40_PERCENT.index) {
			strHex = HSSFColor.GREY_40_PERCENT.hexString;
		}
		if (nColor == HSSFColor.GREY_50_PERCENT.index) {
			strHex = HSSFColor.GREY_50_PERCENT.hexString;
		}
		if (nColor == HSSFColor.GREY_80_PERCENT.index) {
			strHex = HSSFColor.GREY_80_PERCENT.hexString;
		}
		if (nColor == HSSFColor.INDIGO.index) {
			strHex = HSSFColor.INDIGO.hexString;
		}
		if (nColor == HSSFColor.OLIVE_GREEN.index) {
			strHex = HSSFColor.OLIVE_GREEN.hexString;
		}
		if (nColor == HSSFColor.RED.index) {
			strHex = HSSFColor.RED.hexString;
		}
		if (nColor == HSSFColor.TEAL.index) {
			strHex = HSSFColor.TEAL.hexString;
		}
		if (nColor == HSSFColor.TEAL.index) {
			strHex = HSSFColor.TEAL.hexString;
		}
		return getHex(strHex);

	}
	public static void main(String[] args)
    {
		System.out.println(HSSFColor.BRIGHT_GREEN.index);
		System.out.println(getHex(HSSFColor.BRIGHT_GREEN.hexString));
    }
	
}