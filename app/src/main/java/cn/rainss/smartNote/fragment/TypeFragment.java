package cn.rainss.smartNote.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.rainss.smartNote.R;
import cn.rainss.smartNote.adapter.NewsCardViewListAdapter;
import cn.rainss.smartNote.adapter.TypeItemViewAdapter;
import cn.rainss.smartNote.adapter.entity.Type;
import cn.rainss.smartNote.core.BaseFragment;
import cn.rainss.smartNote.utils.SQLiteUtils;
import cn.rainss.smartNote.utils.XToastUtils;
import cn.rainss.smartNote.widget.MaterialLoadMoreView;

@Page(name = "分类")
public class TypeFragment extends BaseFragment {
    /**
     * 绑定页面元素
     */
    /**
     * 顶部数据
     */
//    @BindView(R.id.toolbar_recycler_view)
//    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.type_add)
    FloatingActionButton typeAdd;

    private TypeItemViewAdapter mAdapter;
    private Handler mHandler = new Handler();
    private boolean mEnableLoadMore;

    /**
     * 获取布局
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_type;
    }

    /**
     * 初始化布局视图
     */
    @Override
    protected void initViews() {
        recyclerView.setLayoutManager(new XLinearLayoutManager(recyclerView.getContext()));
        //设置动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter = new TypeItemViewAdapter());
        swipeRefreshLayout.setColorSchemeColors(0xff0099cc, 0xffff4444, 0xff669900, 0xffaa66cc, 0xffff8800);
    }

    /**
     * 添加分类弹窗
     */
    private void addTypeDialog() {
        new MaterialDialog.Builder(getContext())
                .customView(R.layout.toast_add_type, true)
                .title("提示")
                .positiveText(R.string.type_add_btn)
                .negativeText(R.string.type_cancel_btn)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText type_name = dialog.getCustomView().findViewById(R.id.type_name_text);
                        String typeStr = type_name.getText().toString();
                        //判断是否传入空值
                        if(typeStr != null && !typeStr.trim().equals("")){
                            SQLiteUtils sqLiteUtils = new SQLiteUtils(getActivity());
                            //格式化时间
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            //获取当前时间
                            String time = df.format(new Date());
                            SQLiteDatabase db = sqLiteUtils.getWritableDatabase();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("name",typeStr);
                            contentValues.put("create_time",time);
                            db.insert("type",null,contentValues);
                            db.close();
                            sqLiteUtils.close();
                            //刷新数据
                            refresh();
                        }else{
                            XToastUtils.toast(ResUtils.getString(R.string.content_warning));
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        XToastUtils.toast(R.string.add_type_cancel);
                    }
                })
                .show();
    }

    /**
     * 初始化监听方法
     */
    @Override
    protected void initListeners() {
        // 添加按钮监听事件
        typeAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addTypeDialog();
            }
        });
        // 刷新监听。
        swipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        refresh();
        //监听列表点击事件
       mAdapter.setOnItemClickListener(new RecyclerViewHolder.OnItemClickListener<Type>() {
           @Override
           public void onItemClick(View itemView, Type item, int position) {
                XToastUtils.toast(item.getName()+"\n"+item.getId());
           }
       });
    }
    /**
     * 刷新。
     */
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadData();
        }
    };
    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadData();
    }

    private void loadData() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.refresh(getData(1));
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, 1000);
    }


    @Override
    public void onDestroyView() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    /**
     * 获取数据
     * @param page
     * @return
     */
    private List<Type> getData(int page) {
        ArrayList<Type> types = new ArrayList<>();
        String sql = "select id,name,create_time from type order by id desc";
        SQLiteUtils sqliteUtils = new SQLiteUtils(getActivity());
        SQLiteDatabase db = sqliteUtils.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        while(cursor.moveToNext()){
            try {
                long id = cursor.getLong(0);
                String name = cursor.getString(1);
                //格式化时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date time = df.parse(cursor.getString(2));
                Type typeItem = new Type(id,name,time);
                types.add(typeItem);
            }catch (ParseException e){
                e.printStackTrace();
                Log.e("rains_error", "getData: 日期转换出错");
            }

        }
        return types;
    }
}
