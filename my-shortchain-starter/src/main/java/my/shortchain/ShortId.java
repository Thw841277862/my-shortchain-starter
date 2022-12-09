package my.shortchain;

import cn.hutool.core.util.IdUtil;
import org.apache.commons.lang3.StringUtils;


/**
 * 诊所shortId生成器
 */
public class ShortId {
    //区分大小写的话可以输出： 62制  916 132 832 既 9亿+不同的组合
    //只做大写的话可以输出：  36制   60 466 176  既 6千万+不同的组合
    private final static char[] charArray = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    //    private static final int BASE_62_SCALE = 62;
    private static final int BASE_36_SCALE = 36;

    /**
     * 生成长度为5字节的shortId
     *
     * @return shortId 长度为5字节
     */
    private static String fromBase10ToBase36(long base10) {
        StringBuilder sb = new StringBuilder();
        long remainder = 0;
        do {
            remainder = base10 % BASE_36_SCALE;
            sb.append(charArray[(int) remainder]);
            base10 = base10 / BASE_36_SCALE;
        }
        while ((base10 > BASE_36_SCALE - 1) && sb.toString().length() < 5);
//        sb.append(charArray[(int) base10]);
        // 倒序
        return sb.reverse().toString();
    }

    public static synchronized String generateUnionDeviceId(long base10) {
        String base36 = fromBase10ToBase36(base10);
        // 往左补 0
        return StringUtils.leftPad(base36, 5, "0");
    }
}
