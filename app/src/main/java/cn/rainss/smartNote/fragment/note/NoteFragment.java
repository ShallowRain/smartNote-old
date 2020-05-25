package cn.rainss.smartNote.fragment.note;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.rainss.smartNote.R;
import cn.rainss.smartNote.activity.MainActivity;
import cn.rainss.smartNote.adapter.TypeItemViewAdapter;
import cn.rainss.smartNote.adapter.entity.Type;
import cn.rainss.smartNote.core.BaseFragment;
import cn.rainss.smartNote.dao.SQLiteUtils;
import cn.rainss.smartNote.utils.XToastUtils;

/**
 * 笔记管理模块
 */
@Page(name = "信息")
public class NoteFragment extends BaseFragment {
    /**
     * 绑定页面元素
     */
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.note_add)
    FloatingActionButton noteAdd;

    private TypeItemViewAdapter mAdapter;
    private Handler mHandler = new Handler();
    private boolean mEnableLoadMore;

    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }
    /**
     * 获取布局
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_note;
    }

    /**
     * 初始化布局视图
     */
    @Override
    protected void initViews() {
        recyclerView.setLayoutManager(new XLinearLayoutManager(recyclerView.getContext()));
        //设置动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setSwipeMenuCreator(swipeMenuCreator);
        //设置操作菜单点击事件
        recyclerView.setOnItemMenuClickListener(mMenuItemClickListener);
        recyclerView.setAdapter(mAdapter = new TypeItemViewAdapter());
        swipeRefreshLayout.setColorSchemeColors(0xff0099cc, 0xffff4444, 0xff669900, 0xffaa66cc, 0xffff8800);
    }


    /**
     * 初始化监听方法
     */
    @Override
    protected void initListeners() {
        // 添加按钮监听事件
        noteAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //添加按钮被点击，打开新窗口添加笔记
                //openNewPage(NoteAddFragment.class);
                Intent intent=new Intent();
                intent.setClass(getActivity(), cn.rainss.smartNote.note.ui.MainActivity.class);
                startActivity(intent);
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
            //loadData();
        }
    };
    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        //loadData();
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
    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int position) {
            // 设置按钮的宽度和高度
            int width = getResources().getDimensionPixelSize(R.dimen.dp_55);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            // 添加右侧操作
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity()).setBackground(R.drawable.selector_red)
                        .setImage(R.drawable.ic_action_delete)
                        .setText(ResUtils.getString(R.string.delete))
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。
            }
        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private OnItemMenuClickListener mMenuItemClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                View viewByPosition = recyclerView.getLayoutManager().findViewByPosition(position);
                //获取待删除分类的id
                TextView typeIdView = (TextView) viewByPosition.findViewById(R.id.type_id);
                String id = typeIdView.getText().toString();
                //获取sqlite对象
                SQLiteUtils sqLiteUtils = new SQLiteUtils(getActivity());
                SQLiteDatabase db = sqLiteUtils.getWritableDatabase();
                if(id != null && !id.trim().equals("")){
                    db.delete("type","id = ? ",new String[]{ id });
                }
                db.close();
                sqLiteUtils.close();
                //刷新数据
                refresh();
            }
        }
    };
}
