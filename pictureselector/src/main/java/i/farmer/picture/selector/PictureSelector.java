package i.farmer.picture.selector;

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
    public void openGallery(OnSelectCallback callback) {
        this.callback = callback;
    }

    /**
     * 打开相机
     */
    public void openCamera(OnSelectCallback callback) {
        this.callback = callback;
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
