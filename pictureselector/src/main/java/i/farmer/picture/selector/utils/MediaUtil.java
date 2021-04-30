package i.farmer.picture.selector.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

/**
 * @author i-farmer
 * @created-time 2021/4/29 5:40 下午
 * @description
 */
public class MediaUtil {

    /**
     * R  createQueryArgsBundle
     *
     * @param selection
     * @param selectionArgs
     * @param limitCount
     * @param offset
     * @return
     */
    @TargetApi(Build.VERSION_CODES.R)
    public static Bundle createQueryArgsBundle(String selection, String[] selectionArgs, int limitCount, int offset) {
        Bundle queryArgs = new Bundle();
        queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection);
        queryArgs.putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs);
        queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER, MediaStore.Files.FileColumns._ID + " DESC");
        if (offset > 0) {
            queryArgs.putString(ContentResolver.QUERY_ARG_SQL_LIMIT, limitCount + " offset " + offset);
        }
        return queryArgs;
    }
}
