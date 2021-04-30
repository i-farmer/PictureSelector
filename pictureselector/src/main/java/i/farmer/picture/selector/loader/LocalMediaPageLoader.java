package i.farmer.picture.selector.loader;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import i.farmer.picture.selector.PictureSelectorConfig;
import i.farmer.picture.selector.model.LocalFolder;
import i.farmer.picture.selector.model.LocalMedia;
import i.farmer.picture.selector.option.PictureConfigs;
import i.farmer.picture.selector.thread.PictureThreadUtils;
import i.farmer.picture.selector.utils.MediaUtil;
import i.farmer.picture.selector.utils.PictureFileUtil;
import i.farmer.picture.selector.utils.PictureMimeType;
import i.farmer.picture.selector.utils.SdkVersionUtil;

/**
 * @author i-farmer
 * @created-time 2021/4/29 5:14 下午
 * @description 分页加载本地文件
 */
public final class LocalMediaPageLoader {
    private static final String NOT_GIF_UNKNOWN = "!='image/*'";
    private static final String NOT_GIF = "!='image/gif' AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF_UNKNOWN;
    private static final String COLUMN_BUCKET_ID = "bucket_id";
    // 要查询的字段
    private static final String[] PROJECTION_PAGE = {
            MediaStore.Files.FileColumns._ID,               // id
            MediaStore.MediaColumns.DATA,                   // 路径
            MediaStore.MediaColumns.MIME_TYPE,              // 类型
            MediaStore.MediaColumns.WIDTH,                  // 宽
            MediaStore.MediaColumns.HEIGHT,                 // 高
            MediaStore.MediaColumns.DURATION,               // 时长
            MediaStore.MediaColumns.SIZE,                   // 大小
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,    // 所在目录名
            MediaStore.MediaColumns.DISPLAY_NAME,           // 名字
            COLUMN_BUCKET_ID};                              // 目录id

    private final WeakReference<Activity> mActivity;

    private final Uri QUERY_URI = MediaStore.Files.getContentUri("external");

    private PictureConfigs configs;

    public LocalMediaPageLoader(Activity activity) {
        this.mActivity = new WeakReference<>(activity);
    }

    /**
     * 分页加载文件
     *
     * @param bucketId 目录id
     * @param page     第几页 小于0 全部
     * @param limit    1页显示多少条
     * @param callback
     */
    public void loadMedia(long bucketId, int page, int limit, OnQueryDataCallback<LocalMedia> callback) {
        if (null == mActivity.get()) {
            return;
        }
        PictureThreadUtils.executeByIo(new PictureThreadUtils.SimpleTask<List<LocalMedia>>() {
            @Override
            public List<LocalMedia> doInBackground() {
                Activity activity = mActivity.get();
                if (null == activity) {
                    return null;
                }
                Cursor data = null;
                try {
                    if (SdkVersionUtil.checkedAndroid_R()) {
                        Bundle queryArgs = MediaUtil.createQueryArgsBundle(getPageSelection(bucketId),
                                getPageSelectionArgs(bucketId),
                                limit,
                                (page - 1) * limit);
                        data = activity.getContentResolver().query(QUERY_URI, PROJECTION_PAGE, queryArgs, null);
                    } else {
                        String orderBy = page > 0 ?
                                MediaStore.Files.FileColumns._ID + " DESC limit " + limit + " offset " + (page - 1) * limit
                                :
                                MediaStore.Files.FileColumns._ID + " DESC";
                        data = activity.getContentResolver().query(QUERY_URI, PROJECTION_PAGE,
                                getPageSelection(bucketId), getPageSelectionArgs(bucketId), orderBy);
                    }
                    if (data != null) {
                        List<LocalMedia> result = null;    // 文件列表
                        if (data.getCount() > 0) {
                            // 有数据
                            int idColumn = data.getColumnIndexOrThrow(PROJECTION_PAGE[0]);          // id
                            int dataColumn = data.getColumnIndexOrThrow(PROJECTION_PAGE[1]);        // 路径
                            int mimeTypeColumn = data.getColumnIndexOrThrow(PROJECTION_PAGE[2]);    // 类型
                            int widthColumn = data.getColumnIndexOrThrow(PROJECTION_PAGE[3]);       // 宽
                            int heightColumn = data.getColumnIndexOrThrow(PROJECTION_PAGE[4]);      // 高
                            int durationColumn = data.getColumnIndexOrThrow(PROJECTION_PAGE[5]);    // 时长
                            int sizeColumn = data.getColumnIndexOrThrow(PROJECTION_PAGE[6]);        // 大小
                            int folderNameColumn = data.getColumnIndexOrThrow(PROJECTION_PAGE[7]);  // 目录名字
                            int fileNameColumn = data.getColumnIndexOrThrow(PROJECTION_PAGE[8]);    // 文件名字
                            int bucketIdColumn = data.getColumnIndexOrThrow(PROJECTION_PAGE[9]);    // 目录id
                            data.moveToFirst();
                            do {
                                long id = data.getLong(idColumn);
                                String absolutePath = data.getString(dataColumn);
                                String url = SdkVersionUtil.checkedAndroid_Q() ? getRealPathAndroid_Q(id) : absolutePath;
                                if (!PictureFileUtil.isFileExists(absolutePath)) {
                                    // 过滤无效文件
                                    continue;
                                }
                                String mimeType = data.getString(mimeTypeColumn);
                                mimeType = TextUtils.isEmpty(mimeType) ? PictureMimeType.ofJPEG() : mimeType;
                                // Here, it is solved that some models obtain mimeType and return the format of image / *,
                                // which makes it impossible to distinguish the specific type, such as mi 8,9,10 and other models
                                if (mimeType.endsWith("image/*")) {
                                    if (PictureMimeType.isContent(url)) {
                                        mimeType = PictureMimeType.getImageMimeType(absolutePath);
                                    } else {
                                        mimeType = PictureMimeType.getImageMimeType(url);
                                    }
                                    if (!configs.isGif) {
                                        if (PictureMimeType.isGif(mimeType)) {
                                            continue;
                                        }
                                    }
                                }

                                if (!configs.isWebp) {
                                    if (mimeType.startsWith(PictureMimeType.ofWEBP())) {
                                        continue;
                                    }
                                }

                                if (!configs.isBmp) {
                                    if (mimeType.startsWith(PictureMimeType.ofBMP())) {
                                        continue;
                                    }
                                }
                                int width = data.getInt(widthColumn);
                                int height = data.getInt(heightColumn);
                                long duration = data.getLong(durationColumn);
                                long size = data.getLong(sizeColumn);
                                String folderName = data.getString(folderNameColumn);
                                String fileName = data.getString(fileNameColumn);
                                long bucket_id = data.getLong(bucketIdColumn);
                                if (configs.filterFileSize > 0) {
                                    // 过滤文件大小
                                    if (size > configs.filterFileSize * configs.fileSizeUnit) {
                                        continue;
                                    }
                                }
                                if (PictureMimeType.isHasVideo(mimeType)) {
                                    if (duration == 0) {
                                        //If the length is 0, the corrupted video is processed and filtered out
                                        continue;
                                    }
                                    if (size <= 0) {
                                        // The video size is 0 to filter out
                                        continue;
                                    }
                                    if (configs.videoMinSecond > 0 && duration < configs.videoMinSecond) {
                                        // If you set the minimum number of seconds of video to display
                                        continue;
                                    }
                                    if (configs.videoMaxSecond > 0 && duration > configs.videoMaxSecond) {
                                        // If you set the maximum number of seconds of video to display
                                        continue;
                                    }
                                }

                                LocalMedia image = new LocalMedia
                                        (id, url, fileName, folderName, duration, mimeType, width, height, size, bucket_id);
                                if (null == result) {
                                    result = new ArrayList<>();
                                }
                                result.add(image);

                            } while (data.moveToNext());
                        }
                        return result;
                    }
                } catch (Exception ex) {
                    return null;
                } finally {
                    if (null != data && !data.isClosed()) {
                        data.close();
                        data = null;
                    }
                }
                return null;
            }

            @Override
            public void onSuccess(List<LocalMedia> result) {
                if (null != callback) {
                    callback.onComplete(result, page);
                }
            }
        });
    }

    /**
     * 获取条件sql
     *
     * @param bucketId
     * @return
     */
    private String getPageSelection(long bucketId) {
        // 视频时长条件
        String durationCondition = getDurationCondition(0, 0);
        // 具体某一个类型
        boolean isQueryFormat = !TextUtils.isEmpty(configs.specifiedFormat);
        String format = !isQueryFormat ? "" : (" AND " + MediaStore.MediaColumns.MIME_TYPE + "='" + configs.specifiedFormat + "'");
        // 具体某一个目录
        String bucket = (bucketId == -1) ? "" : (" AND " + COLUMN_BUCKET_ID + "=?");
        // 是否包含gif
        String gif = (isQueryFormat || configs.isGif) ? "" : (" AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF);
        switch (configs.chooseMode) {
            case PictureSelectorConfig.TYPE_IMAGE: {
                // 图片
                return "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                        + gif + format
                        + ") AND " + MediaStore.MediaColumns.SIZE + ">0" + bucket;
            }
            case PictureSelectorConfig.TYPE_VIDEO: {
                // 视频
                return "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                        + " AND " + durationCondition + format
                        + ") AND " + MediaStore.MediaColumns.SIZE + ">0" + bucket;
            }
            default: {
                // 所有图片、视频
                return "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                        + gif
                        + ") OR (" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                        + " AND " + durationCondition
                        + ") AND " + MediaStore.MediaColumns.SIZE + ">0"
                        + bucket;
            }
        }
    }


    /**
     * 获取条件值
     *
     * @param bucketId
     * @return
     */
    private String[] getPageSelectionArgs(long bucketId) {
        String[] args;
        boolean hasBucket = bucketId != -1;
        if (configs.chooseMode == PictureSelectorConfig.TYPE_IMAGE) {
            // 图片
            args = new String[hasBucket ? 2 : 1];
            args[0] = String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
        } else if (configs.chooseMode == PictureSelectorConfig.TYPE_VIDEO) {
            // 视频
            args = new String[hasBucket ? 2 : 1];
            args[0] = String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
        } else {
            // 图片 + 视频
            args = new String[hasBucket ? 3 : 2];
            args[0] = String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
            args[1] = String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
        }
        if (hasBucket) {
            args[args.length - 1] = String.valueOf(bucketId);
        }
        return args;
    }

    /**
     * 视频时长过滤额条件
     *
     * @param exMaxLimit 最大限制
     * @param exMinLimit 最小限制
     * @return
     */
    private String getDurationCondition(long exMaxLimit, long exMinLimit) {
        long maxS = configs.videoMaxSecond == 0 ? Long.MAX_VALUE : configs.videoMaxSecond;
        if (exMaxLimit != 0) {
            maxS = Math.min(maxS, exMaxLimit);
        }
        return String.format(Locale.CHINA, "%d <%s " + MediaStore.MediaColumns.DURATION + " and " + MediaStore.MediaColumns.DURATION + " <= %d",
                Math.max(exMinLimit, configs.videoMinSecond),
                Math.max(exMinLimit, configs.videoMinSecond) == 0 ? "" : "=",
                maxS);
    }

    private String getRealPathAndroid_Q(long id) {
        return QUERY_URI.buildUpon().appendPath(String.valueOf(id)).build().toString();
    }
}
