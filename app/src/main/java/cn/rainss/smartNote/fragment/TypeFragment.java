package cn.rainss.smartNote.fragment;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.searchview.MaterialSearchView;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xutil.XUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.rainss.smartNote.R;
import cn.rainss.smartNote.core.BaseFragment;
import cn.rainss.smartNote.utils.XToastUtils;

@Page(name = "分类")
public class TypeFragment extends BaseFragment {

    /**
     * 绑定视图
     */

    /**
     * 获取布局
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_type;
    }

    /**
     * 初始化布局
     */
    @Override
    protected void initViews() {

    }

    @OnClick(R.id.type_add)
    public void typeAddClick(){
        showInputDialog();
    }

    /**
     * 带输入框的对话框
     */
    private void showInputDialog() {
        new MaterialDialog.Builder(getContext())
                .customView(R.layout.toast_add_type, true)
                .title("提示")
                .positiveText(R.string.type_add_btn)
                .negativeText(R.string.type_cancel_btn)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText type_name = dialog.getCustomView().findViewById(R.id.type_name_text);
                        //这里进行入库操作
                        XToastUtils.toast(type_name.getText().toString());
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
}
