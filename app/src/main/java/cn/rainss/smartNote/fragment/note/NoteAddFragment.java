package cn.rainss.smartNote.fragment.note;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.github.ielse.imagewatcher.ImageWatcherHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sendtion.xrichtext.RichTextEditor;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.toast.XToast;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.rainss.smartNote.R;
import cn.rainss.smartNote.adapter.entity.Note;
//import cn.rainss.smartNote.comm.MyGlideEngine;
import cn.rainss.smartNote.core.BaseFragment;
//import cn.rainss.smartNote.utils.CommonUtil;
//import cn.rainss.smartNote.utils.GlideSimpleLoader;
//import cn.rainss.smartNote.utils.ImageUtils;
//import cn.rainss.smartNote.utils.SDCardUtil;
//import cn.rainss.smartNote.utils.StringUtils;
import cn.rainss.smartNote.utils.TimeUtils;
import cn.rainss.smartNote.utils.XToastUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


@Page(anim = CoreAnim.none, name = "添加笔记")
public class NoteAddFragment extends BaseFragment {
    private static final int REQUEST_CODE_CHOOSE = 23;//定义请求码常量
    private int flag;//区分是新建笔记还是编辑笔记
    private static final int cutTitleLength = 20;//截取的标题长度

    private ProgressDialog loadingDialog;
    private ProgressDialog insertDialog;
    private int screenWidth;
    private int screenHeight;
    private Disposable subsLoading;
    private Disposable subsInsert;
    private ImageWatcherHelper iwHelper;

    private Note note;
    private String myTitle;
    private String myContent;
    private String myGroupName;
    private String myNoteTime;
    /**
     * 绑定页面元素
     */
    @BindView(R.id.save)
    FloatingActionButton floatingButton;
    @BindView(R.id.note_title)
    EditText noteTitleEdit;
    @BindView(R.id.note_time)
    TextView noteTimeTextview;
    @BindView(R.id.note_type)
    TextView noteTypeTextview;
    @BindView(R.id.note_content)
    RichTextEditor noteContentEdit;

    /**
     * 获取页面
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_note_add;
    }

    /**
     * 页面初始化
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initViews() {
        //设当前时间
        noteTimeTextview.setText(TimeUtils.timeToString(new Date()));
        //设置默认分类类型
        noteTypeTextview.setText(ResUtils.getString(R.string.default_type));

//        iwHelper = ImageWatcherHelper.with(getActivity(), new GlideSimpleLoader());
//        screenWidth = CommonUtil.getScreenWidth(getActivity());
//        screenHeight = CommonUtil.getScreenHeight(getActivity());
//        insertDialog = new ProgressDialog(getActivity());
//        insertDialog.setMessage("正在插入图片...");
//        insertDialog.setCanceledOnTouchOutside(false);
//        openSoftKeyInput();
//        try {
//            Intent intent = getActivity().getIntent();
//            flag = intent.getIntExtra("flag", 0);//0新建，1编辑
//            if (flag == 1) {//编辑
//                Bundle bundle = intent.getBundleExtra("data");
//                note = (Note) bundle.getSerializable("note");
//
//                if (note != null) {
//                    myTitle = note.getTitle();
//                    myContent = note.getContent();
//                    myNoteTime = TimeUtils.timeToString(note.getCreateTime());
//                    //Group group = groupDao.queryGroupById(note.getGroupId());
////                    if (group != null){
////                        myGroupName = group.getName();
////                        tv_new_group.setText(myGroupName);
////                    }
//
//                    loadingDialog = new ProgressDialog(getActivity());
//                    loadingDialog.setMessage("数据加载中...");
//                    loadingDialog.setCanceledOnTouchOutside(false);
//                    loadingDialog.show();
//
//                    noteTimeTextview.setText(TimeUtils.timeToString(note.getCreateTime()));
//                    noteTitleEdit.setText(note.getTitle());
//                    noteContentEdit.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            dealWithContent();
//                        }
//                    });
//                }
//            } else {
//                //新建笔记;
//                if (myGroupName == null || "全部笔记".equals(myGroupName)) {
//                    myGroupName = "默认笔记";
//                }
//                noteTypeTextview.setText(myGroupName);
//                myNoteTime = CommonUtil.date2string(new Date());
//                noteTitleEdit.setText(myNoteTime);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("图片") {
            @Override
            public void performAction(View view) {
                //XToastUtils.toast("按钮被点击");
//                closeSoftKeyInput();//关闭软键盘
//                callGallery();
            }
        });
        return titleBar;
    }
//
//    private void dealWithContent() {
//        //showEditData(note.getContent());
//        noteContentEdit.clearAllLayout();
//        showDataSync(note.getContent());
//
//        // 图片删除事件
//        noteContentEdit.setOnRtImageDeleteListener(new RichTextEditor.OnRtImageDeleteListener() {
//
//            @Override
//            public void onRtImageDelete(String imagePath) {
//                if (!TextUtils.isEmpty(imagePath)) {
//                    boolean isOK = SDCardUtil.deleteFile(imagePath);
//                    if (isOK) {
//                        XToastUtils.toast("删除成功：" + imagePath);
//                    }
//                }
//            }
//        });
//        // 图片点击事件
//        noteContentEdit.setOnRtImageClickListener(new RichTextEditor.OnRtImageClickListener() {
//            @Override
//            public void onRtImageClick(View view, String imagePath) {
//                try {
//                    myContent = getEditData();
//                    if (!TextUtils.isEmpty(myContent)) {
//                        List<String> imageList = StringUtils.getTextFromHtml(myContent, true);
//                        if (!TextUtils.isEmpty(imagePath)) {
//                            int currentPosition = imageList.indexOf(imagePath);
//                            XToastUtils.toast("点击图片：" + currentPosition + "：" + imagePath);
//
//                            List<Uri> dataList = new ArrayList<>();
//                            for (int i = 0; i < imageList.size(); i++) {
//                                dataList.add(ImageUtils.getUriFromPath(imageList.get(i)));
//                            }
//                            iwHelper.show(dataList, currentPosition);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    /**
//     * 显示数据
//     */
//    protected void showEditData(ObservableEmitter<String> emitter, String html) {
//        try {
//            List<String> textList = StringUtils.cutStringByImgTag(html);
//            for (int i = 0; i < textList.size(); i++) {
//                String text = textList.get(i);
//                emitter.onNext(text);
//            }
//            emitter.onComplete();
//        } catch (Exception e) {
//            e.printStackTrace();
//            emitter.onError(e);
//        }
//    }
//
//    /**
//     * 异步方式显示数据
//     */
//    private void showDataSync(final String html) {
//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) {
//                showEditData(emitter, html);
//            }
//        })
//                //.onBackpressureBuffer()
//                .subscribeOn(Schedulers.io())//生产事件在io
//                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onComplete() {
//                        if (loadingDialog != null) {
//                            loadingDialog.dismiss();
//                        }
//                        if (noteContentEdit != null) {
//                            //在图片全部插入完毕后，再插入一个EditText，防止最后一张图片后无法插入文字
//                            noteContentEdit.addEditTextAtIndex(noteContentEdit.getLastIndex(), "");
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        if (loadingDialog != null) {
//                            loadingDialog.dismiss();
//                        }
//                        XToastUtils.toast("解析错误：图片不存在或已损坏");
//                    }
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        subsLoading = d;
//                    }
//
//                    @Override
//                    public void onNext(String text) {
//                        try {
//                            if (noteContentEdit != null) {
//                                if (text.contains("<img") && text.contains("src=")) {
//                                    //imagePath可能是本地路径，也可能是网络地址
//                                    String imagePath = StringUtils.getImgSrc(text);
//                                    //Log.e("---", "###imagePath=" + imagePath);
//                                    //插入空的EditText，以便在图片前后插入文字
//                                    noteContentEdit.addEditTextAtIndex(noteContentEdit.getLastIndex(), "");
//                                    noteContentEdit.addImageViewAtIndex(noteContentEdit.getLastIndex(), imagePath);
//                                } else {
//                                    noteContentEdit.addEditTextAtIndex(noteContentEdit.getLastIndex(), text);
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//    }
//
//    /**
//     * 打开软键盘
//     */
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void openSoftKeyInput() {
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        //boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
//        if (imm != null && !imm.isActive() && noteContentEdit != null) {
//            noteContentEdit.requestFocus();
//            //第二个参数可设置为0
//            //imm.showSoftInput(et_content, InputMethodManager.SHOW_FORCED);//强制显示
//            imm.showSoftInputFromInputMethod(noteContentEdit.getWindowToken(),
//                    InputMethodManager.SHOW_FORCED);
//        }
//    }
//
//    /**
//     * 关闭软键盘
//     */
//    private void closeSoftKeyInput() {
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        //boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
//        if (imm != null && imm.isActive() && getActivity().getCurrentFocus() != null) {
//            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
//                    InputMethodManager.HIDE_NOT_ALWAYS);
//            //imm.hideSoftInputFromInputMethod();//据说无效
//            //imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0); //强制隐藏键盘
//            //如果输入法在窗口上已经显示，则隐藏，反之则显示
//            //imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }
//
//    /**
//     * 退出处理
//     */
//    private void dealwithExit() {
//        try {
//            String noteTitle = noteTitleEdit.getText().toString();
//            String noteContent = getEditData();
//            //分类
//            String groupName = noteTypeTextview.getText().toString();
//            //时间
//            String noteTime = noteTimeTextview.getText().toString();
//            if (flag == 0) {//新建笔记
//                if (noteTitle.length() > 0 || noteContent.length() > 0) {
//                    //saveNoteData(false);
//                }
//            } else if (flag == 1) {//编辑笔记
//                if (!noteTitle.equals(myTitle) || !noteContent.equals(myContent)
//                        || !groupName.equals(myGroupName) || !noteTime.equals(myNoteTime)) {
//                    //saveNoteData(false);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        getActivity().finish();
//        //退回上一个页面
//        popToBack();
//    }
//
//    /**
//     * 负责处理编辑数据提交等事宜，请自行实现
//     */
//    private String getEditData() {
//        StringBuilder content = new StringBuilder();
//        try {
//            List<RichTextEditor.EditData> editList = noteContentEdit.buildEditData();
//            for (RichTextEditor.EditData itemData : editList) {
//                if (itemData.inputStr != null) {
//                    content.append(itemData.inputStr);
//                } else if (itemData.imagePath != null) {
//                    content.append("<img src=\"").append(itemData.imagePath).append("\"/>");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return content.toString();
//    }
//
//    @Override
//    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == getActivity().RESULT_OK) {
//            if (data != null) {
//                if (requestCode == 1){
//                    //处理调用系统图库
//                } else if (requestCode == this.REQUEST_CODE_CHOOSE){
//                    //异步方式插入图片
//                    insertImagesSync(data);
//                }
//            }
//        }
//    }
//
//    /**
//     * 异步方式插入图片
//     */
//    private void insertImagesSync(final Intent data){
//        insertDialog.show();
//
//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) {
//                try{
//                    noteContentEdit.measure(0, 0);
//                    List<Uri> mSelected = Matisse.obtainResult(data);
//                    // 可以同时插入多张图片
//                    for (Uri imageUri : mSelected) {
//                        //不知道
//                        String imagePath = SDCardUtil.getFilePathFromUri(getActivity(),  imageUri);
//                        //Log.e(TAG, "###path=" + imagePath);
//                        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, screenWidth, screenHeight);//压缩图片
//                        //bitmap = BitmapFactory.decodeFile(imagePath);
//                        imagePath = SDCardUtil.saveToSdCard(bitmap);
//                        //Log.e(TAG, "###imagePath="+imagePath);
//                        emitter.onNext(imagePath);
//                    }
//
//                    // 测试插入网络图片 http://pics.sc.chinaz.com/files/pic/pic9/201904/zzpic17414.jpg
//                    //emitter.onNext("http://pics.sc.chinaz.com/files/pic/pic9/201903/zzpic16838.jpg");
//                    emitter.onNext("http://b.zol-img.com.cn/sjbizhi/images/10/640x1136/1572123845476.jpg");
//                    emitter.onNext("https://img.ivsky.com/img/tupian/pre/201903/24/richu_riluo-013.jpg");
//
//                    emitter.onComplete();
//                }catch (Exception e){
//                    e.printStackTrace();
//                    emitter.onError(e);
//                }
//            }
//        })
//                //.onBackpressureBuffer()
//                .subscribeOn(Schedulers.io())//生产事件在io
//                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onComplete() {
//                        if (insertDialog != null && insertDialog.isShowing()) {
//                            insertDialog.dismiss();
//                        }
//                        XToastUtils.toast("图片插入成功");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        if (insertDialog != null && insertDialog.isShowing()) {
//                            insertDialog.dismiss();
//                        }
//                        XToastUtils.toast("图片插入失败:"+e.getMessage());
//                    }
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        subsInsert = d;
//                    }
//
//                    @Override
//                    public void onNext(String imagePath) {
//                        noteContentEdit.insertImage(imagePath);
//                    }
//                });
//    }
//    /**
//     * 调用图库选择
//     */
//    private void callGallery(){
////        //调用系统图库
////        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");// 相片类型
////        startActivityForResult(intent, 1);
//
//        Matisse.from(this)
//                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))//照片视频全部显示MimeType.allOf()
//                .countable(true)//true:选中后显示数字;false:选中后显示对号
//                .maxSelectable(3)//最大选择数量为9
//                //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))//图片显示表格的大小
//                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)//图像选择和预览活动所需的方向
//                .thumbnailScale(0.85f)//缩放比例
//                .theme(R.style.Matisse_Zhihu)//主题  暗色主题 R.style.Matisse_Dracula
//                .imageEngine(new MyGlideEngine())//图片加载方式，Glide4需要自定义实现
//                .capture(true) //是否提供拍照功能，兼容7.0系统需要下面的配置
//                //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
//                .captureStrategy(new CaptureStrategy(true,"com.sendtion.matisse.fileprovider"))//存储到哪里
//                .forResult(REQUEST_CODE_CHOOSE);//请求码
//    }
}
