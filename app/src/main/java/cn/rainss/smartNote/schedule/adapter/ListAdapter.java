package cn.rainss.smartNote.schedule.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import cn.rainss.smartNote.R;
import cn.rainss.smartNote.schedule.model.Schedule;
import cn.rainss.smartNote.schedule.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private List<Schedule> schedules;
    private CountDownTimer timer;
    private Context context;

    private ListAdapter.OnItemClickListener onItemClickListener;

    public ListAdapter(Context context,List<Schedule> schedules) {
        this.context = context;
        this.schedules = schedules;
    }

    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.notes_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        // 绑定数据
        holder.id.setText(schedules.get(position).getId() + "");
        holder.title.setText(schedules.get(position).getTitle());
        holder.content.setText(schedules.get(position).getContent());
        holder.time.setText("编辑于"+ schedules.get(position).getTime());
        holder.priority.setText("优先级："+ schedules.get(position).getPriority());

        if (schedules.size() != 0){
            Long currentMilisTime = TimeUtil.getCurrentMilisTime();
            if (schedules.get(position).getClockTime() <= currentMilisTime){
                holder.remaintime.setText("任务已过期");
            }else {
                timer = new CountDownTimer(schedules.get(position).getClockTime(),1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        holder.remaintime.setText(TimeUtil.transformateFromMilisToStringDate(TimeUtil.getCurrentMilisTime(),
                                schedules.get(position).getClockTime()));
                    }

                    @Override
                    public void onFinish() {
                        timer.cancel();
                        holder.remaintime.setText("任务已过期");
                    }
                }.start();
            }
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, pos);
                }
                //表示此事件已经消费，不会触发单击事件
                return true;
            }
        });

    }


    @Override
    public int getItemCount() {
        return schedules == null ? 0 : schedules.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public Schedule getItem(int position) {
        return schedules.get(position);
    }




    public void updataView(List<Schedule> newSchedules) {
        if(schedules == null) {
            schedules = new ArrayList<>();
        }
        this.schedules = newSchedules;
        notifyDataSetChanged();
    }


    public void removeAllItem() {

        timer.cancel();
        schedules.clear();
        notifyDataSetChanged();
    }

    //从List移除对象
    public void removeItem(int position) {
        if(schedules == null || schedules.isEmpty()) {
            return;
        }
        schedules.remove(position);
        timer.cancel();
        notifyItemRemoved(position);
//        notifyDataSetChanged();
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {
        public  TextView  id;
        private TextView title;
        private TextView content;
        private TextView time;
        private TextView remaintime;
        private TextView priority;

        public MyViewHolder(View view)
        {
            super(view);
            id = view.findViewById(R.id.note_id);
            title = view.findViewById(R.id.note_title);
            content = view.findViewById(R.id.note_content);
            time = view.findViewById(R.id.note_time);
            remaintime = view.findViewById(R.id.note_remaintime);
            priority = view.findViewById(R.id.note_priority);
        }
    }



    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnItemClickListener(ListAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

}
