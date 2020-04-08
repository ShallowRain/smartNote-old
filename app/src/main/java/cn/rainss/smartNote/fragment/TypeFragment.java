package cn.rainss.smartNote.fragment;

import android.text.InputType;
import android.view.View;

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
                .iconRes(R.drawable.icon_warning)
                .title(R.string.tip_warning)
                .content(R.string.content_warning)
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                                | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .input(
                        getString(R.string.hint_please_input_password),
                        "",
                        false,
                        (new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                XToastUtils.toast(input.toString());
                            }
                        }))
                .inputRange(3, 5)
                .positiveText(R.string.lab_continue)
                .negativeText(R.string.lab_change)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        XToastUtils.toast("你输入了:" + dialog.getInputEditText().getText().toString());
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        XToastUtils.toast("取消了");
                    }
                })
                .cancelable(false)
                .show();
    }
}
