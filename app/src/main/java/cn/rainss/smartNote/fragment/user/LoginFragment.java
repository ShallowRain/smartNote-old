/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cn.rainss.smartNote.fragment.user;

import android.view.View;
import android.widget.TextView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import butterknife.BindView;
import cn.rainss.smartNote.R;
import cn.rainss.smartNote.core.BaseFragment;

@Page(name = "登录页面")
public class LoginFragment extends BaseFragment{

    @BindView(R.id.link_signup)
    TextView signup;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_login;
    }

    @Override
    protected void initViews() {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewPage(RegisterFragment.class);
            }
        });
    }

}
