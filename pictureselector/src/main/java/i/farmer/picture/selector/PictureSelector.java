package i.farmer.picture.selector;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import i.farmer.picture.selector.ui.activity.GalleryActivity;

/**
 * @author i-farmer
 * @created-time 2021/4/29 3:59 下午
 * @description 图片选择器入口文件
 */
public class PictureSelector {
    private OnSelectCallback callback;

    /**
     * 打开相册
     */
    public void openGallery(Activity activity, OnSelectCallback callback) {
        this.callback = callback;
        Intent intent = new Intent(activity, GalleryActivity.class);
        activity.startActivityForResult(intent, PictureSelectorConfig.REQUEST_CHOOSE);
    }

    /**
     * 打开相机
     */
    public void openGallery(Fragment fragment, OnSelectCallback callback) {
        this.callback = callback;
    }

    /**
     * 打开相机
     */
    public void openCamera(Activity activity, OnSelectCallback callback) {
        this.callback = callback;
    }

    /**
     * 打开相机
     */
    public void openCamera(Fragment fragment, OnSelectCallback callback) {
        this.callback = callback;
    }

    /**
     * 请求结果
     *
     * @param requestCode
     * @param data
     */
    public void onResult(int requestCode, Intent data) {
        if (null == callback) {
            return;
        }
        if (null == data) {
            callback.onSelectResult(null);
            return;
        }
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 单例定义 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private static class SingletonHolder {
        /**
         * 单例对象实例
         */
        static PictureSelector INSTANCE = new PictureSelector();
    }

    public static PictureSelector getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * private的构造函数用于避免外界直接使用new来实例化对象
     */
    private PictureSelector() {
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
