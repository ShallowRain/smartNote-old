package cn.rainss.smartNote.schedule.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.rainss.smartNote.R;
import cn.rainss.smartNote.schedule.model.User;
import cn.rainss.smartNote.schedule.utils.SharedPreferencesUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferencesUtil.init(this);

        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.btn_login);
        signupLink = findViewById(R.id.link_signup);

        loginButton.setOnClickListener(this);
        signupLink.setOnClickListener(this);


    }


    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                login();
                break;
            case R.id.link_signup:
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }


    public void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("正在登录...");
        progressDialog.show();

        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();

        //登录逻辑
        BmobUser.loginByAccount(email, password, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    onLoginSuccess();
                    SharedPreferencesUtil.setUsername(email);
                    SharedPreferencesUtil.setPassword(password);
                    progressDialog.dismiss();
                }else {
                    progressDialog.dismiss();
                    onLoginFailed();
                    Toast.makeText(getBaseContext(),"登录失败，请检查账号密码是否错误后再试",Toast.LENGTH_SHORT).show();
                }
            }
        });



//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }




//    //重写返回键方法，让其按返回直接退出而不是去引导页
//    @Override
//    public void onBackPressed() {
//        finish();
//    }

    //登录成功
    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

        SharedPreferencesUtil.setIsLogin(true);
    }

    //登录失败
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "登录失败,请检查账号或密码是否错误再试", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }


    //判断editTexit输入的数据是否合法
    public boolean validate() {
        boolean valid = true;
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("您输入的邮箱地址有误");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 25) {
            passwordText.setError("密码在6到25位之间");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }



}
