package cn.rainss.smartNote.fragment.more;

import android.view.View;
import android.view.ViewGroup;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.actionbar.TitleUtils;

import cn.rainss.smartNote.R;
import cn.rainss.smartNote.core.BaseFragment;

@Page(anim = CoreAnim.none)
public class MoreFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    protected void initViews() {

    }
    @Override
    protected TitleBar initTitle() {
        return null;
    }
}
