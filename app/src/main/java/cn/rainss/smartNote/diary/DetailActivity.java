package cn.rainss.smartNote.diary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import cn.rainss.smartNote.R;

import androidx.appcompat.widget.Toolbar;

import cn.rainss.smartNote.diary.dao.DBManager;
import cn.rainss.smartNote.diary.model.Diary;

public class DetailActivity extends BaseActivity {
    private DBManager mgr;
    private Diary diary;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);

        Toolbar toolbar = getToolbar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mgr = DBManager.getMgr(this);

        TextView diary_content = (TextView) findViewById(R.id.activity_detail_content);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        diary = mgr.query(id);
        toolbar.setTitle(diary.getLabel());
        diary_content.setText(diary.getContent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id1 = item.getItemId();
        switch (id1) {
            case R.id.diary_save:
                mgr.recoverDiary(String.valueOf(id1));
                Toast.makeText(DetailActivity.this, "日记恢复成功!", Toast.LENGTH_LONG).show();
                finish();
                break;
            case R.id.diary_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("要删除吗？").setMessage("将要删除此篇日记！")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    mgr.movetotrash(id);
                                    Toast.makeText(DetailActivity.this, "已移至垃圾桶！", Toast.LENGTH_SHORT).show();
                                    finish();
                                } catch (Exception x) {
                                    Toast.makeText(DetailActivity.this, "移至垃圾桶失败！", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("取消", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                Window dialogWindow = dialog.getWindow();
                WindowManager m = getWindowManager();
                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高
                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                Point point = new Point();
                d.getSize(point);
                // 设置宽度
                p.width = (int) (point.x * 0.95); // 宽度设置为屏幕的0.95
                p.gravity = Gravity.CENTER;//设置位置
                dialogWindow.setAttributes(p);
                break;
            case R.id.diary_update:
                Bundle data = new Bundle();
                data.putSerializable("diary", diary);
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class);
                intent.putExtras(data);
                startActivity(intent);
                finish();
                break;
            case R.id.diary_list:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
// TODO 垃圾桶内的日记可以单独删除.
