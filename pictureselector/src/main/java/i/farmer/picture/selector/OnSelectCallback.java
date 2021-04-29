package i.farmer.picture.selector;

import java.util.List;

import i.farmer.picture.selector.model.LocalMedia;

/**
 * @author i-farmer
 * @created-time 2021/4/29 4:19 下午
 * @description 结果回掉
 */
public interface OnSelectCallback {
    void onSelectResult(List<LocalMedia> list);
}
