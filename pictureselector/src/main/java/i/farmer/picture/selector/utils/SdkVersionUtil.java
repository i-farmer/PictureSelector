package i.farmer.picture.selector.utils;

import android.os.Build;

/**
 * @author i-farmer
 * @created-time 2021/4/29 5:37 下午
 * @description
 */
public class SdkVersionUtil {
    /**
     * 判断是否是Android Q版本
     *
     * @return
     */
    public static boolean checkedAndroid_Q() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    /**
     * 判断是否是Android R版本
     *
     * @return
     */
    public static boolean checkedAndroid_R() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }
}
