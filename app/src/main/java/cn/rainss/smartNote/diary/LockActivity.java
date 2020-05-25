package cn.rainss.smartNote.diary;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.rainss.smartNote.R;

import androidx.appcompat.widget.Toolbar;

/**
 * Description: 日记本加锁
 */
public class LockActivity extends BaseActivity {
    SharedPreferences preferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_lock);

        Toolbar toolbar = getToolbar();
        setSupportActionBar(toolbar);

        preferences = getSharedPreferences("diary", MODE_PRIVATE);

        if (!preferences.getBoolean("lockable", false)) {
            Intent intent = new Intent(LockActivity.this, ListActivity.class);
            startActivity(intent);
            finish();
        } else {
            Button button = (Button) findViewById(R.id.button);
            button.setOnClickListener(  new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = (EditText) findViewById(R.id.editText);
                    String password = editText.getText().toString();
                    if (password.equals(preferences.getString("key", null))) {
                        Intent intent = new Intent(LockActivity.this, ListActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //这里shake是用传统动画框架Animation实现的
//                        Animation shake = AnimationUtils.loadAnimation(LockActivity.this, R.anim.shake);
//                        findViewById(R.id.editText).startAnimation(shake);

                        //这里shake是用属性动画框架Animator实现的
                        ObjectAnimator animator =
                                ObjectAnimator.ofFloat(findViewById(R.id.editText),"translationX",0,10F);
                        animator.setDuration(1000);
                        animator.setInterpolator(new CycleInterpolator(7));
                        animator.start();
                        Toast.makeText(LockActivity.this, "密码不正确!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
