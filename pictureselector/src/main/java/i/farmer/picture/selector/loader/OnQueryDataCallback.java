package i.farmer.picture.selector.loader;

import java.util.List;

/**
 * @author i-farmer
 * @created-time 2021/4/29 5:16 下午
 * @description 查询文件回调
 */
public interface OnQueryDataCallback<T> {
    void onComplete(List<T> data, int currentPage);
}
