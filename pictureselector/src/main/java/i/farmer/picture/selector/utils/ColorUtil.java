package i.farmer.picture.selector.utils;

import android.graphics.Color;

/**
 * @author i-farmer
 * @created-time 2021/4/30 2:29 下午
 * @description 颜色工具
 */
public class ColorUtil {
    public static boolean isLightColor(int color) {
        double darkness = 1 - (0.299 * Color.red(color)
                + 0.587 * Color.green(color)
                + 0.114 * Color.blue(color)) / 255;
        if (darkness < 0.5) {
            return true; // It's a light color
        } else {
            return false; // It's a dark color
        }
    }
}
