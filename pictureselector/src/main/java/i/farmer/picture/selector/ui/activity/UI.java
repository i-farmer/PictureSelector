package i.farmer.picture.selector.ui.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import i.farmer.picture.selector.R;
import i.farmer.picture.selector.option.PictureConfigs;
import i.farmer.picture.selector.utils.ColorUtil;
import i.farmer.picture.selector.utils.StatusBarUtil;

/**
 * @author i-farmer
 * @created-time 2021/4/29 5:51 下午
 * @description
 */
public abstract class UI extends AppCompatActivity {
    private TextView mTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        initViews();
    }

    protected abstract @LayoutRes
    int getLayoutRes();

    protected void initViews() {
        // 背景颜色
        int bgColor = PictureConfigs.getInstance().UI_backgroundColor;
        if (bgColor != 0) {
            findViewById(R.id.mRootLayout).setBackgroundResource(bgColor);
        }
        // 工具条
        setToolBar();
    }

    protected void setToolBar() {
        // 状态栏
        View mStatusBar = findViewById(R.id.mStatusBar);
        if (null != mStatusBar) {
            ViewGroup.LayoutParams lp = mStatusBar.getLayoutParams();
            lp.height = StatusBarUtil.getStatusBarHeight(this);
            mStatusBar.setLayoutParams(lp);
        }
        // 工具条
        int colorRes = PictureConfigs.getInstance().UI_toolbarColor;
        int bgColor;
        if (colorRes == 0) {
            bgColor = Color.parseColor("#333333");
        } else {
            bgColor = ContextCompat.getColor(this, colorRes);
        }
        // 背景颜色
        View mToolbar = findViewById(R.id.mToolbar);
        mToolbar.setBackgroundColor(bgColor);
        // 状态栏文字颜色
        boolean isLightBgColor = ColorUtil.isLightColor(bgColor);
        StatusBarUtil.setStatusMode(this, isLightBgColor);
        ColorStateList mainColor = ContextCompat.getColorStateList(this, isLightBgColor
                ? R.color.ps_color_button_dark : R.color.ps_color_button_light);
        // 返回按钮
        ImageButton mBackButton = findViewById(R.id.mBackButton);
        // 颜色
        mBackButton.setImageTintList(mainColor);
        int backIcon = PictureConfigs.getInstance().UI_toolbarBackIcon;
        if (backIcon != 0) {
            mBackButton.setImageResource(backIcon);
        }
        ViewGroup.LayoutParams lp = mBackButton.getLayoutParams();
        int height = PictureConfigs.getInstance().UI_toolbarHeight;
        if (height != 0) {
            lp.height = getResources().getDimensionPixelOffset(PictureConfigs.getInstance().UI_toolbarHeight);
        }
        // 点击事件
        mBackButton.setLayoutParams(lp);
        mBackButton.setOnClickListener(v -> onBackPressed());
        // 标题颜色
        mTitleView = findViewById(R.id.mTitleView);
        mTitleView.setTextColor(mainColor);
        // 箭头颜色
        View mArrowIcon = findViewById(R.id.mArrowIcon);
        mArrowIcon.setBackgroundTintList(mainColor);

        // 底部工具条
        setBottomBar(bgColor, isLightBgColor, mainColor);
    }

    protected void setBottomBar(int bgColor, boolean isLightBgColor, ColorStateList mainColor) {
        View mBottomBar = findViewById(R.id.mBottomBar);
        if (null == mBottomBar) {
            return;
        }
        getWindow().setNavigationBarColor(bgColor); // 底部导航栏颜色
        mBottomBar.setBackgroundColor(bgColor);
        ViewGroup.LayoutParams lp = mBottomBar.getLayoutParams();
        int height = PictureConfigs.getInstance().UI_toolbarHeight;
        if (height != 0) {
            lp.height = getResources().getDimensionPixelOffset(PictureConfigs.getInstance().UI_toolbarHeight);
        }
        mBottomBar.setLayoutParams(lp);
    }

    @Override
    public void setTitle(int titleId) {
        if (null == mTitleView) {
            return;
        }
        mTitleView.setText(titleId);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (null == mTitleView) {
            return;
        }
        mTitleView.setText(title);
    }

    protected int getColorPrimary() {
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.colorPrimary, tv, true)) {
            return tv.data;
        }
        return 0;
//        return ContextCompat.getColor(this, R.color.ps_colorPrimary);
    }

    protected boolean isColorPrimaryLight() {
        int color = getColorPrimary();
        return ColorUtil.isLightColor(color);
    }

    protected boolean isWhiteColor() {
        return getColorPrimary() == -1; // 是否是白色
    }
}
