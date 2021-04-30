package i.farmer.demo.pictureselector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import i.farmer.picture.selector.PictureSelector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.mChooseButton).setOnClickListener(v ->
                PictureSelector.getInstance().openGallery(this, list -> {
                    // 选择结果
                })
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PictureSelector.getInstance().onResult(requestCode, data);
    }
}