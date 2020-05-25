package cn.rainss.smartNote.schedule.receiver;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Vibrator;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import cn.rainss.smartNote.schedule.activity.MainActivity;

public class ClockReceiver extends BroadcastReceiver {

    private Vibrator vibrator;   //手机震动

    @Override
    public void onReceive(Context context, Intent intent) {

        vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);

        vibrator.vibrate(new long[]{500, 1000, 500, 2000}, 0);

        //Toast.makeText(context, "收到定时广播", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(context, MainActivity.class);
        String  content = intent.getStringExtra("content");

        //弹窗显示信息
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("日程提示信息：小主，你的时间到了，下面是你的日程信息！");
            builder.setMessage(content);
            builder.setCancelable(false);
            builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    vibrator.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alertDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY));
        }else {
            alertDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        }
        alertDialog.show();
        Window dialogWindow = alertDialog.getWindow();
        WindowManager m = dialogWindow.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        Point point = new Point();
        d.getSize(point);
        // 设置宽度
        p.width = (int) (point.x * 0.95); // 宽度设置为屏幕的0.95
        p.gravity = Gravity.CENTER;//设置位置
        dialogWindow.setAttributes(p);

        context.startService(i);
    }

}
