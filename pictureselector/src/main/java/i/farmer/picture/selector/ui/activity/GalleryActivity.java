package i.farmer.picture.selector.ui.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import i.farmer.picture.selector.R;
import i.farmer.picture.selector.option.PictureConfigs;

/**
 * @author i-farmer
 * @created-time 2021/4/29 4:53 下午
 * @description 相册
 */
public class GalleryActivity extends UI {
    private TextView mPreviewButton = null;     // 预览按钮
    private CheckBox mOriginalButton = null;    // 是否原图
    private View mOkButton = null;              // 选中结果
    private TextView mOkCountView = null;
    private TextView mOkLabelView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTitle("全部相册");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PictureConfigs.getInstance().isOriginal) {
            // 是否勾选原图
            mOriginalButton.setChecked(PictureConfigs.getInstance().isOriginalChecked);
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.ps_activity_gallery;
    }

    @Override
    protected void setBottomBar(int bgColor, boolean isLightBgColor, ColorStateList mainColor) {
        super.setBottomBar(bgColor, isLightBgColor, mainColor);
        mPreviewButton = findViewById(R.id.mPreviewButton);
        mPreviewButton.setTextColor(mainColor);
        mPreviewButton.setClickable(true);
        mPreviewButton.setEnabled(false);
        mPreviewButton.setOnClickListener(v -> {
            // 预览
        });
        mOriginalButton = findViewById(R.id.mOriginalButton);
        mOriginalButton.setVisibility(PictureConfigs.getInstance().isOriginal ? View.VISIBLE : View.GONE);
        mOriginalButton.setTextColor(mainColor);
        mOriginalButton.setButtonDrawable(isLightBgColor ? R.drawable.ps_checkbox_dark : R.drawable.ps_checkbox_light);
        // 监听选中变化
        mOriginalButton.setOnCheckedChangeListener((button, isChecked) -> PictureConfigs.getInstance().isOriginal = isChecked);
        // 选择按钮 / 数量
        mOkButton = findViewById(R.id.mOkButton);
        mOkCountView = findViewById(R.id.mOkCountView);
        mOkCountView.setTextColor(ContextCompat.getColor(this, isWhiteColor() ? R.color.ps_darkColor : R.color.ps_lightColor));
        mOkLabelView = findViewById(R.id.mOkLabelView);
        findViewById(R.id.mOkButton).setOnClickListener(v -> {
            // 已经选择好
        });
        setSelectedCount(0);
    }

    private void setSelectedCount(int count) {
        boolean enabled = count > 0;
        mOkButton.setEnabled(enabled);
        mOkCountView.setText(enabled ? (count + "") : "");
        mOkCountView.setVisibility(enabled ? View.VISIBLE : View.GONE);
        mOkLabelView.setEnabled(enabled);
        mOkLabelView.setText(enabled ? "已完成" : "请选择");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PictureConfigs.getInstance().release();
    }

    private void readLocalMedia() {

    }
}
