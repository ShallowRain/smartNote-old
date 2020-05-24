package cn.rainss.smartNote.diary;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.rainss.smartNote.R;

import com.readystatesoftware.systembartint.SystemBarTintManager;


public class BaseActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //判断当前android系统是否位于4.4之上，是的话，启动系统沉浸栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorPrimary);
        }
    }

    public Toolbar getToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        return toolbar;
    }
}
