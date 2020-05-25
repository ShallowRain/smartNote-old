package cn.rainss.smartNote.diary;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cn.rainss.smartNote.diary.dao.DBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.rainss.smartNote.R;

public class ListActivity extends BaseActivity {
    private DBManager mgr;
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private DrawerLayout mDrawerLayout;
    private ListView lvLeftMenu;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);

        checkPermission();
    }

    /**
     * 检查权限，申请软件权限
     */
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请READ_PHONE_STATE权限
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0001);
        }else {
            mgr = DBManager.getMgr(this);

            initView();

            inflater();
        }
    }

    private void initView() {
        Toolbar toolbar = getToolbar();
        FloatingActionButton imageButton = (FloatingActionButton) findViewById(R.id.fab);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        listView = (ListView) findViewById(R.id.activity_list_listview);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);

        //Toolbar的设置，用来取代Actionbar，并接管Actionbar中的功能
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //界面中浮动按钮的设置，绑定新建日记activity
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListActivity.this, InsertActivity.class));
            }
        });

        //侧滑菜单的设置
        //ActionBarDrawerToggle是一个开关，用于打开/关闭DrawerLayout抽屉
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();//该方法会自动和actionBar关联, 将开关的图片显示在了action上，
        // 如果不设置，也可以有抽屉的效果，不过是默认的图标
        mDrawerLayout.setDrawerListener(mDrawerToggle);//设置drawer的开关监听

        //填充侧滑菜单列表
        simpleAdapter = new SimpleAdapter(this, getData(),
                R.layout.drawerlayout_left_item,
                new String[]{"title", "img"},
                new int[]{R.id.dl_left_item_label, R.id.dl_left_item_imageView});
        lvLeftMenu.setAdapter(simpleAdapter);

        // 为侧滑菜单中的选项绑定将要启动的activity
        lvLeftMenu.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(ListActivity.this, InsertActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(ListActivity.this, TrashActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(ListActivity.this, BackupActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(ListActivity.this, SettingActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(ListActivity.this, InfoActivity.class));
                        break;
                    case 5:
                        finish();
                        break;
                }
            }
        });

    }

    //设置侧滑菜单List，并返回
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String[] strings = new String[]{"新建", "回收站", "备份",
                "加密",  "退出"};
        int[] icons = new int[]{R.drawable.ic_1content_new,
                R.drawable.ic_1content_discard, R.drawable.ic_1device_sd_storage,
                R.drawable.ic_1action_settings,
                R.drawable.ic_1navigation_cancel};
        Map<String, Object> map;
        int i;
        for (i = 0; i < strings.length; i++) {
            map = new HashMap<String, Object>();
            map.put("title", strings[i]);
            map.put("img", icons[i]);
            list.add(map);
        }

        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list, menu);

        //搜索栏的设置
        MenuItem search = menu.findItem(R.id.list_search);
        SearchView sv = (SearchView) search.getActionView();
        sv.setSubmitButtonEnabled(true);
        sv.setQueryHint("内容,标题或时间");
        sv.setIconifiedByDefault(true);
        sv.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setVisibility(View.INVISIBLE);
                inflater();
                return false;
            }
        });
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final Cursor c1 = mgr.find(query);
                TextView textView = (TextView) findViewById(R.id.textView);
                //确保查询结果中有"_id"列
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(ListActivity.this, R.layout.list_item, c1,
                        new String[]{"diary_label", "diary_content", "diary_date", "diary_mood", "diary_weather"},
                        new int[]{R.id.list_item_diary_label, R.id.list_item_diary_content, R.id.list_item_diary_date
                                , R.id.img_mood, R.id.img_weather});
                if (adapter.isEmpty()) {
                    listView.setAdapter(null);
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.INVISIBLE);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                            String id1 = c1.getString(0);
                            intent.putExtra("id", id1);
                            startActivity(intent);
                        }
                    });
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    //为listView填充日记表列的方法
    private void inflater() {

        final Cursor c = mgr.queryTheCursor();
        startManagingCursor(c);    //托付给activity根据自己的生命周期去管理Cursor的生命周期
        final CursorWrapper cursorWrapper = new CursorWrapper(c);
        //使用SimpleCursorAdapter，必须确保查询结果中有"_id"列
        adapter = new SimpleCursorAdapter(this, R.layout.list_item,
                cursorWrapper, new String[]{"diary_label", "diary_content", "diary_date", "diary_mood", "diary_weather"},
                new int[]{R.id.list_item_diary_label, R.id.list_item_diary_content,
                        R.id.list_item_diary_date, R.id.img_mood, R.id.img_weather});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                String id1 = cursorWrapper.getString(0);
                intent.putExtra("id", id1);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                //初始化DBManager对象，为数据提供增删改查的功能
                if (mgr == null) {
                    mgr = DBManager.getMgr(this);
                }
                //将日记列表填充至listView
                inflater();
            } else {
                // Permission Denied
                AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("请求权限")
                        .setMessage("记录日记，需要请求读写磁盘权限，以便日记正常存储，请允许")
                        .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkPermission();
                            }
                        })
                        .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                System.exit(-1);
                            }
                        });
                builder.create().show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
