package cn.rainss.smartNote.adapter;

import androidx.annotation.NonNull;

import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.rainss.smartNote.R;
import cn.rainss.smartNote.adapter.entity.Type;

/**
 * 分类列表适配器
 */
public class TypeItemViewAdapter extends BaseRecyclerAdapter<Type> {
    //格式化时间
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.adapter_type_card_view_list_item;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, Type item) {
        //if not null
        if( item != null){
            //绑定视图数据
            holder.text(R.id.type_id,String.valueOf(item.getId()));
            holder.text(R.id.type_name,item.getName());
            //格式化日期
            holder.text(R.id.type_time,df.format(item.getCreateTime()));
        }
    }
}
