package i.farmer.picture.selector.utils;

import android.text.TextUtils;

import java.io.File;

/**
 * @author i-farmer
 * @created-time 2021/4/30 11:25 上午
 * @description 文件工具
 */
public class PictureFileUtil {
    public static boolean isFileExists(String path) {
        if (TextUtils.isEmpty(path) || !new File(path).exists()) {
            return false;
        }
        return true;
    }
}
