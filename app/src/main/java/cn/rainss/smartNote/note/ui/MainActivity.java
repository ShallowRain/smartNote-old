package cn.rainss.smartNote.note.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import cn.rainss.smartNote.R;
import cn.rainss.smartNote.note.adapter.MyNoteListAdapter;
import cn.rainss.smartNote.note.bean.Note;
import cn.rainss.smartNote.note.db.NoteDao;
import cn.rainss.smartNote.note.view.SpacesItemDecoration;

public class MainActivity extends BaseActivity {
    private MyNoteListAdapter mNoteListAdapter;
    private List<Note> noteList;
    private NoteDao noteDao;
    private long waitTime = 2000;
    private long touchTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_main);
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        noteDao = new NoteDao(this);

        RecyclerView rv_list_main = findViewById(R.id.rv_list_main);
        rv_list_main.addItemDecoration(new SpacesItemDecoration(0));//设置item间距
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//竖向列表
        rv_list_main.setLayoutManager(layoutManager);

        mNoteListAdapter = new MyNoteListAdapter();
        mNoteListAdapter.setmNotes(noteList);
        rv_list_main.setAdapter(mNoteListAdapter);

        mNoteListAdapter.setOnItemClickListener(new MyNoteListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Note note) {
                //showToast(note.getTitle());

                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("note", note);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
        mNoteListAdapter.setOnItemLongClickListener(new MyNoteListAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final Note note) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示");
                builder.setMessage("确定删除笔记？");
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int ret = noteDao.deleteNote(note.getId());
                        if (ret > 0) {
                            showToast("删除成功");
                            //TODO 删除笔记成功后，记得删除图片（分为本地图片和网络图片）
                            //获取笔记中图片的列表 StringUtils.getTextFromHtml(note.getContent(), true);
                            refreshNoteList();
                        }
                    }
                });
                builder.setNegativeButton("取消", null);
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
            }
        });
        //设置浮动按钮点击事件
        FloatingActionButton addButton =  findViewById(R.id.note_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                intent.putExtra("groupName", "默认笔记");
                intent.putExtra("flag", 0);
                startActivity(intent);
            }
        });
    }

    //刷新笔记列表
    private void refreshNoteList() {
        if (noteDao == null)
            noteDao = new NoteDao(this);
        noteList = noteDao.queryNotesAll(0);
        mNoteListAdapter.setmNotes(noteList);
        mNoteListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshNoteList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_note:
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                intent.putExtra("groupName", "默认笔记");
                intent.putExtra("flag", 0);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //按返回键时
    public void onBackPressed() {

        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= waitTime) {
            Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT).show();
            touchTime = currentTime;
        } else {
            finish();
        }
    }
}
