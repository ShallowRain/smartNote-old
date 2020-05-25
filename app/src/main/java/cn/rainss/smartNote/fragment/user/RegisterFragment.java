package cn.rainss.smartNote.fragment.user;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.xuexiang.xaop.util.MD5Utils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import butterknife.BindView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.rainss.smartNote.R;
import cn.rainss.smartNote.core.BaseFragment;
import cn.rainss.smartNote.utils.XToastUtils;

/**
 *
 * @since 2019-10-15 22:38
 */
@Page(name = "注册页面")
public class RegisterFragment extends BaseFragment{

    @BindView(R.id.input_phone)
    EditText phone;
    @BindView(R.id.send_code)
    Button sendCodeBtn;
    @BindView(R.id.input_code)
    EditText inputCode;
    @BindView(R.id.input_password)
    TextInputEditText inputPassword;
    @BindView(R.id.input_reEnterPassword)
    TextInputEditText inputReEnterPassword;
    @BindView(R.id.btn_signup)
    AppCompatButton btnSignup;
    @BindView(R.id.link_login)
    TextView link_Login;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_register;
    }

    @Override
    protected void initViews() {
        Bmob.initialize(getContext(), "86ea6be45370ad9cdb1081bf51b9831c");
        sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送短信
                String phoneNumber = phone.getText().toString().trim();
                if(phoneNumber != null && !phoneNumber.equals("")){
                    BmobSMS.requestSMSCode(phoneNumber, "DataSDK", new QueryListener<Integer>() {
                        @Override
                        public void done(Integer smsId, BmobException e) {
                            if (e == null) {
                                XToastUtils.toast("发送验证码成功，短信ID：" + smsId);
                            } else {
                                XToastUtils.toast("发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage());
                            }
                        }
                    });
                } else {
                    XToastUtils.toast("请输入手机号码");
                }
            }
        });
        //注册按钮
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        link_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewPage(LoginFragment.class);
            }
        });
    }

}
