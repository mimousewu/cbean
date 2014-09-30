package net.cbean.binary;

import java.util.Random;

/**
 * 数据转换工具类
 */
public class DataUtils {
    /**
	 * Convert a byte to hex string. Such as from '255' to 'FF'.
	 * 
	 * @param b byte value to be converted
	 * @return @return	hex string represented the byte value
	 */
	public static String byte2HexStr(byte b) {
	    String str = Integer.toHexString(b).toUpperCase();
	    if (str.length() < 2) {
	        str = "0" + str;
	    } else if (str.length() > 2) {
	        str = str.substring(str.length()-2, str.length());
	    }
	    return str;
	}

    /**
	 * 根据格式化的数据字符串的进制返回整型数
	 * @param numberStr
	 * @return
     * @see DataUtils#parseLong
	 *
	 */
    public static Integer parseInt(String numberStr){
        return parseLong(numberStr).intValue();
    }

    /**
     * 根据格式化的数据字符串的进制返回长整型数，例如：
     * <ul>
     * <li>0x11 --> 17 (表示16进制用0x作为前缀)</li>
     * <li>x11  --> 17 (表示16进制同时可以使用x作为前缀，目的是兼容错误的配置文件)</li>
     * <li>o11  --> 9  (表示8进制用o作为前缀)</li>
     * <li>b11  --> 3  (表示2进制用大写或者小写的b作为前缀)</li>
     * <li>15   --> 15 (表示10进制)<br />
     * </ul>
     * 一般的，我们建议使用小写的o表示2进制，以免与8进制混淆
     * @param numberStr 用于进行转换的字符串
     * @return 转换后的整数
     */
    public static Long parseLong(String numberStr){
        if(numberStr==null){
            throw new NumberFormatException("The value is null.");
        }
        if(numberStr.startsWith("0x")){
            return Long.parseLong(numberStr.substring(2,numberStr.length()),16);
        }else if(numberStr.startsWith("x")){
            return Long.parseLong(numberStr.substring(1,numberStr.length()),16);
        }else if(numberStr.matches("[Oo][1-9][0-9]*")){
            return Long.parseLong(numberStr.substring(1,numberStr.length()),8);
        }else if(numberStr.startsWith("b") || numberStr.startsWith("B")){
            return Long.parseLong(numberStr.substring(1,numberStr.length()),2);
        }else{
            return Long.parseLong(numberStr);
        }
    }

    /**
     * hexToByte()，用来把一个十六进制的ASCII码String类型表示的数转换成byte[]类型
     * 例如：String "7FE30A" -> byte[]类型的数{'0x7F','0xE3','0x0A'}
	 * @param s
	 * @return 
	 */
    public static byte[] hexToByte(String s) {

        int sLength = s.length ();
        if (sLength == 0)
            return null;

        byte[] bs = new byte[(sLength + 1) / 2];
        //Utility.Debugln("sLength:" + sLength + " bs.length:" + bs.length);
        int high, low;
        //Utility.Debugln("s:" + s);
        for (int i = bs.length - 1, j = sLength - 1; i >= 0; i--, j--) {
            //Utility.Debugln("j:" + j + " i:" + i);
            high = 0;
            low = Character.digit (s.charAt (j--), 16);
            if (j >= 0) // j had been minuted 1
                high = Character.digit (s.charAt (j), 16);
            int value = (high << 4) + low;
            //Utility.Debugln("high:" + high + " low:" + low);
            bs[i] = (byte) value;
        }

        return bs;
    }
	
	/**
	 * Convert a byte array to hex string.
	 * 
	 * @param buf byte array to be converted
	 * @param space space string between single byte value
	 * @return @return	hex string represented the byte array
	 */
	public static String byteArray2HexStr(byte[] buf, String space) {
        return byteArray2HexStr(buf, 0, buf.length, space);
	}
    
    /**
	 * @param buf
	 * @param inx
	 * @param len
	 * @param space
	 * @return 
	 */
    public static String byteArray2HexStr(byte[] buf, int inx, int len, String space) {
        String str = byte2HexStr(buf[inx]);
        for (int i=inx+1; i<buf.length && i<inx+len; i++) {
            str += space + byte2HexStr(buf[i]);
        }
        return str;
    }
    
    /**
	 * @param b
	 * @return 
	 */
    public static String byte2BinStr(byte b) {
        String str = Integer.toBinaryString(parseUnsignedByte(b));
        int len = str.length();
        for (int i=0; i < 8 - len; i++) {
            str = "0" + str;
        }
        return str;
    }
    
    /**
	 * @param buf
	 * @param inx
	 * @param len
	 * @param space
	 * @return 
	 */
    public static String byteArray2BinStr(byte[] buf, int inx, int len, String space) {
        String str = byte2BinStr(buf[inx]);
        for (int i=inx+1; i<buf.length && i<inx+len; i++) {
            str += space + byte2BinStr(buf[i]);
        }
        return str;
    }
    
    /**
	 * @param byteVal
	 * @return 
	 */
    public static int parseUnsignedByte(byte byteVal) {
        return ((int) byteVal & 0x000000FF);
    }

    /**
	 * @param buf
	 * @param inx
	 * @return 
	 */
    public static short parseShort(byte[] buf, int inx) {
        return (short) (((buf[inx] << 8) & 0x0000FF00) | (buf[inx + 1] & 0x000000FF));
    }
    
    /**
	 * @param buf
	 * @param inx
	 * @return 
	 */
    public static int parseUnsignedShort(byte[] buf, int inx) {
        return ((buf[inx] << 8) & 0x0000FF00) | ((int) buf[inx + 1] & 0x000000FF);
    }
    
    /**
     * 本方法解析byte数组，返回所表示的整型值，例如：
     * <p>
     * buf = new byte[]{};<br />
     * inx = 2;<br />
     * len = 2;</p>
     * 则返回结果是从第2个元素开始，长度为2的数组3,4所表示的整型值，即0x304，等于10进制的772
     *
	 * @param buf 等待解析的byte数组
	 * @param inx 在buffer中开始进行解析的起点
	 * @param len 被解析的buffer长度
	 * @return
	 */
    public static int parseInt(byte[] buf, int inx, int len) {
        if (len < 1) return 0;
        if (len == 1) {
            return ((int) buf[inx]) & 0x000000FF;
        } else if (len == 2) {
            return ((int) buf[inx] << 8) & 0x0000FF00
                    | ((int) buf[inx + 1]) & 0x000000FF
            ;
        } else if (len == 3) {
            return ((int) buf[inx] << 16) & 0x00FF0000
                    | ((int) buf[inx + 1] << 8) & 0x0000FF00
                    | ((int) buf[inx] + 2) & 0x000000FF
            ;
        } else if (len == 4) {
            return ((int) buf[inx] << 24) & 0xFF000000
                    | ((int) buf[inx + 1] << 16) & 0x00FF0000
                    | ((int) buf[inx + 2] << 8) & 0x0000FF00
                    | ((int) buf[inx + 3]) & 0x000000FF
            ;
        } else {
            return ((int) buf[inx] << 24) & 0xFF000000
                    | ((int) buf[inx + 1] << 16) & 0x00FF0000
                    | ((int) buf[inx + 2] << 8) & 0x0000FF00
                    | ((int) buf[inx + 3]) & 0x000000FF
            ;
        }
    }
    
    /**
	 * @param buf
	 * @param inx
	 * @return 
	 */
    public static int parseInt(byte[] buf, int inx) {
        return parseInt(buf, inx, 4);
    }
    
    /**
	 * @param buf
	 * @param inx
	 * @return 
	 */
    public static long parseUnsignedInt(byte[] buf, int inx) {
        int i = parseInt(buf, inx);
        if (i < 0) {
            long lng = Long.valueOf("FFFFFFFF", 16).longValue();
            return lng + i + 1;
        } else {
            return i;
        }
    }
    
    /**
	 * @param buf
	 * @param inx
	 * @param len
	 * @return 
	 */
    public static long parseUnsignedInt(byte[] buf, int inx, int len) {
        int i = parseInt(buf, inx, len);
        if (i < 0) {
            long lng = Long.valueOf("FFFFFFFF", 16).longValue();
            return lng + i + 1;
        } else {
            return (long) i;
        }
    }
    
    /**
	 * @param lng
	 * @param byteCount
	 * @return 
	 */
    public static byte[] encodeLong(long lng, int byteCount) {
        byte[] buf = new byte[byteCount];
        for (int i=0; i<byteCount; i++) {
            buf[i] = (byte) (lng >> ((byteCount-i-1)*8));
        }
        return buf;
    }
    
    /**
	 * @param ival
	 * @param byteCount
	 * @return 
	 */
    public static byte[] encodeInt(int ival, int byteCount) {
        byte[] buf = new byte[byteCount];
        for (int i=0; i<byteCount; i++) {
            buf[i] = (byte) (ival >> ((byteCount-i-1)*8));
        }
        return buf;
    }
    
    /**
	 * @return 
	 */
    public static int getRandomNum() {
//		Long seed = System.currentTimeMillis();
		Random random = new Random();	
		int randomInt = random.nextInt(4095);
		return randomInt;
    }
    
    /**
	 * @return 
	 */
    public static int getUlCredibleFlag() {
		int ulCredibleFlag = new Random().nextInt(100);
		if (ulCredibleFlag > 79) {
			ulCredibleFlag = 0;
		} else {
			ulCredibleFlag = 1;
		}
		return ulCredibleFlag;
    }
    
    /**
     * fill des byte array with start position of startPos
     * @param src
     * @param des
     * @param startPos
     */
    public static byte[] subByteArray(byte[] src,byte[] des,int startPos){
    	int length =
    		(src.length - des.length < startPos)?
    			src.length-startPos : des.length; 
    	System.arraycopy(src, startPos, des, 0, length);
    	return des;
    }
    
    public static byte[] subByteArray(byte[] src,int startPos){
    	int length = src.length - startPos;
    	byte[] dest = new byte[length];
    	System.arraycopy(src, startPos, dest, 0, length);
    	return dest;
    }

    /**
     * 将byte数组转换为一个可读的字符串
     * @param codes
     * @return
     */
    public static String convertToString(byte[] codes) {
        StringBuilder sb = new StringBuilder("byte[]{ ");
        for (int i = 0; i < codes.length; i++) {
            byte instanceId = codes[i];
            sb.append(" ").append((int)instanceId);
        }
        return sb.append(" }").toString();
    }
    
    
	public static int byteArrayToInt(byte[] b) {
        return byteArrayToInt(b, 0);
    }
	
	public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }
	
    public static byte[] intToByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }
}

