package org.bee;

public class Base64 {
    static String charSet =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    public static byte[] decode(String s) {
        int end = 0;
        if (s.endsWith("=")) {
            end++;
        }
        if (s.endsWith("==")) {
            end++;
        }
        int len = (s.length() + 3) / 4 * 3 - end;
        byte[] result = new byte[len];
        int dst = 0;
        for (int src = 0; src < s.length(); src++) {
            int code = charSet.indexOf(s.charAt(src));
            if (code == -1) {
                break;
            }
            switch (src % 4) {
            case 0:
                result[dst] = (byte) (code << 2);
                break;
            case 1:
                result[dst++] |= (byte) ((code >> 4) & 0x3);
                result[dst] = (byte) (code << 4);
                break;
            case 2:
                result[dst++] |= (byte) ((code >> 2) & 0xf);
                result[dst] = (byte) (code << 6);
                break;
            case 3:
                result[dst++] |= (byte) (code & 0x3f);
                break;
            }
        }
        return result;
    }
}