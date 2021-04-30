package i.farmer.picture.selector;

/**
 * @author i-farmer
 * @created-time 2021/4/29 4:59 下午
 * @description 常量
 */
public interface PictureSelectorConfig {
    int REQUEST_CHOOSE = 188;
    int REQUEST_CAMERA = 189;

    int TYPE_ALL = 0;           // 相册-全部文件
    int TYPE_IMAGE = 1;         // 相册-全部图片
    int TYPE_VIDEO = 2;         // 相册-全部视频
}
