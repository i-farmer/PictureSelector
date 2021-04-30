package i.farmer.picture.selector.option;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;

import i.farmer.picture.selector.R;

/**
 * @author i-farmer
 * @created-time 2021/4/29 5:49 下午
 * @description 配置
 */
public class PictureConfigs {
    public int chooseMode;                      // 选择模式
    public int videoMaxSecond = 0;              // 视频最大时长 0=不做限制
    public int videoMinSecond = 0;              // 视频最小时长 0=不做限制
    public String specifiedFormat = "";         // 直接查询固定格式的文件
    public boolean isOriginal = false;          // 是否可以选择原文件
    public boolean isOriginalChecked = false;   // 是否选中了原文件按钮
    public boolean isGif = false;               // 是否包含gif图片，默认不包含
    public boolean isWebp = true;               // 是否包含Webp图片
    public boolean isBmp = true;                // 是否包含bmg图片
    public float filterFileSize = -1.f;         // 大于0的时候过滤文件
    public long fileSizeUnit = 1024 * 1024L;    // 文件大小的单位，默认M

    public @ColorRes
    int UI_backgroundColor = 0;                 // 页面背景颜色
    public @ColorRes
    int UI_toolbarColor = 0;                    // 工具条-背景颜色
    public @DimenRes
    int UI_toolbarHeight = 0;                   // 工具条-高度
    public @DrawableRes
    int UI_toolbarBackIcon = 0;                 // 工具条-返回按钮

    public void release() {

    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 单例定义 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private static class SingletonHolder {
        /**
         * 单例对象实例
         */
        static PictureConfigs INSTANCE = new PictureConfigs();
    }

    public static PictureConfigs getInstance() {
        return PictureConfigs.SingletonHolder.INSTANCE;
    }

    /**
     * private的构造函数用于避免外界直接使用new来实例化对象
     */
    private PictureConfigs() {
    }

    /**
     * readResolve方法应对单例对象被序列化时候
     *
     * @return
     */
    private Object readResolve() {
        return getInstance();
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 单例定义 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}
