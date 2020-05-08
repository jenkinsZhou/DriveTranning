package com.tourcoo.training.ui.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import com.tourcoo.training.R;
import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.core.base.activity.BaseTitleActivity;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.retrofit.BaseLoadingObserver;
import com.tourcoo.training.core.retrofit.UploadProgressBody;
import com.tourcoo.training.core.retrofit.UploadRequestListener;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;
import com.tourcoo.training.entity.feedback.FeedBackEntity;
import com.trello.rxlifecycle3.android.ActivityEvent;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * @author :JenkinsZhou
 * @description :问题反馈
 * @company :途酷科技
 * @date 2019年05月05日9:07
 * @Email: 971613168@qq.com
 */
public class FeedbackActivity extends BaseTitleActivity implements View.OnClickListener {
    private UploadImageAdapter uploadImageAdapter;
    private OptionsPickerView pvCustomOptions;
    private FeedBackEntity selectReason;
    private ArrayList<FeedBackEntity> options1Items = new ArrayList<>();
    private TextView tvQuestionType;
    private String currentImagePath;
    private String mImages;
    private KProgressHUD hud;
    private RecyclerView mRecyclerView;
    private List<String> imagePathList = new ArrayList<>();
    private List<String> imageUrlList = new ArrayList<>();
    private List<LocalMedia> selectList = new ArrayList<>();
    private EditText etDetail;
    private Message message;
    private MyHandler mHandler = new MyHandler(this);

    @Override
    public int getContentLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tvQuestionType = findViewById(R.id.tvQuestionType);
        mRecyclerView = findViewById(R.id.rvUploadImage);
        tvQuestionType.setOnClickListener(this);
        etDetail = findViewById(R.id.etDetail);
        etDetail.clearFocus();
        findViewById(R.id.btnSubmit).setOnClickListener(this);
        init();

    }

    private void init() {
        GridLayoutManager manager = new GridLayoutManager(mContext, 4, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        uploadImageAdapter = new UploadImageAdapter(mContext, onAddPicClickListener);
        uploadImageAdapter.setOnEmptyCallBack(new UploadImageAdapter.OnEmptyCallBack() {
            @Override
            public void empty() {
//                showAddIcon();
            }
        });
        mRecyclerView.setAdapter(uploadImageAdapter);
        initItemClick();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("问题反馈");
    }

    private void initItemClick() {
        uploadImageAdapter.setOnItemClickListener(new UploadImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            PictureSelector.create(mContext).themeStyle(R.style.PicturePickerStyle).openExternalPreview(position, selectList);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }






    private UploadImageAdapter.OnAddPictureClickListener onAddPicClickListener = new UploadImageAdapter.OnAddPictureClickListener() {
        @Override
        public void onAddPicClick() {
            selectPic();
        }
    };


    private void selectPic() {
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.PicturePickerStyle)
                // 最大图片选择数量
                .maxSelectNum(8)
                // 最小选择数量
                .minSelectNum(1)
                // 每行显示个数
                .imageSpanCount(4)
                // 多选 or 单选
                .selectionMode(PictureConfig.MULTIPLE)
                // 是否可预览图片
                .previewImage(true)
                // 是否可播放音频
                .enablePreviewAudio(false)
                // 是否显示拍照按钮
                .isCamera(true)
                // 图片列表点击 缩放效果 默认true
                .isZoomAnim(true)
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                // 是否裁剪
                .enableCrop(false)
                // 是否压缩
                .compress(true)
                //同步true或异步false 压缩 默认同步
                .synOrAsy(true)
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                // glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .glideOverride(160, 160)
                // 是否显示uCrop工具栏，默认不显示
                .hideBottomControls(false)
                // 是否显示gif图片
                .isGif(false)
                // 裁剪框是否可拖拽
                .freeStyleCropEnabled(false)
                // 是否传入已选图片
                .selectionMedia(selectList)
                // 小于100kb的图片不压缩
                .minimumCompressSize(100)
                //结果回调onActivityResult code
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    /**
     * 上传图片
     *
     * @param imageList
     */
    private void uploadImage(List<String> imageList) {
        String defaultValue = "点击选择";
        if (defaultValue.equals(getTextValue(tvQuestionType))) {
            ToastUtil.show("请选择问题类型");
            return;
        }
        if (imageList == null) {
            ToastUtil.show("您还没选择图片");
            return;
        }
        if (imageList.isEmpty()) {
            String images = StringUtils.join(imageUrlList, ",");
            TourCooLogUtil.i(TAG, TAG + "图片URL集合:" + images);
            mImages = images;
            requestFeedback();
            return;
        }
        //还有需要上传的图片
        File file;
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        //注意，file是后台约定的参数，如果是多图，files，如果是单张图片，file就行
        currentImagePath = imageList.get(0);
        file = new File(currentImagePath);
        builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        RequestBody requestBody = builder.build();

        UploadProgressBody uploadProgressBody = new UploadProgressBody(requestBody, new UploadRequestListener() {
            @Override
            public void onProgress(float progress, long current, long total) {
                message = mHandler.obtainMessage();
                message.what = 1;
                message.arg1 = (int) (progress * 100);
                mHandler.sendMessage(message);

            }

            @Override
            public void onFail(Throwable e) {
                TourCooLogUtil.e("异常：" + e.toString());
                closeHudProgressDialog();
            }
        });
        showHudProgressDialog(imageList.size());
       /* ApiRepository.getInstance().getApiService().uploadFiles(uploadProgressBody).enqueue(new Callback<BaseEntity<UploadEntity>>() {
            @Override
            public void onResponse(Call<BaseEntity<UploadEntity>> call, Response<BaseEntity<UploadEntity>> response) {
                closeHudProgressDialog();
                BaseEntity<UploadEntity> entity = response.body();
                if (entity != null) {
                    if (entity.code == CODE_REQUEST_SUCCESS && entity.data != null) {
                        //todo
                        TourCooLogUtil.i("图片URL：", entity.data.getUrl());
                        //上传图片成功，将图片URL添加到集合
                        imageUrlList.add(entity.data.getUrl());
                        if (imageList.isEmpty()) {
                            //图片全部上传完毕
                            ToastUtil.showSuccess("图片全部上传完毕");
                            return;
                        }
                        //移除当前图片
                        for (int i = imageList.size() - 1; i >= 0; i--) {
                            imageList.remove(currentImagePath);
                            TourCooLogUtil.i(TAG, TAG + "已经移除当前图片:" + currentImagePath);
                        }
                        //否则递归调用自己
                        uploadImage(imageList);
                    } else {
                        ToastUtil.showFailed(entity.msg);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseEntity<UploadEntity>> call, Throwable t) {
                Toast.makeText(mContext, "图片上传失败，稍后重试", Toast.LENGTH_SHORT).show();
                closeHudProgressDialog();
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvQuestionType:
                requestFeedbackReasonListAndShow();
                break;
            case R.id.btnSubmit:
                if (TextUtils.isEmpty(etDetail.getText().toString())) {
                    ToastUtil.show("请输入问题");
                    return;
                }
                showLoading("正在上传...");
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closeLoading();
                    }
                }, 2000);
               /* if (selectList.isEmpty()) {
                    requestFeedback();
                } else {
                    uploadImage(imagePathList);
                }*/
                break;
            default:
                break;
        }
    }


  /*  private void showTypeDialog() {
        new ActionSheetDialog.ListIOSBuilder(this)
                .addItems(reasonArray)
                .setItemsTextColorResource(R.color.greenCommon)
                .setBackgroundColor(TourCooUtil.getColor(R.color.whiteCommon))
                .setCancel(R.string.cancel)
                .setCancelMarginTop(SizeUtil.dp2px(8))
                .setCancelTextColorResource(R.color.greenCommon)
                .setOnItemClickListener(mOnItemClickListener)
                .create()
//                .setDimAmount(0.6f)
//                .setAlpha(0.6f)
                .show();
    }
*/
/*
    private ActionSheetDialog.OnItemClickListener mOnItemClickListener = new ActionSheetDialog.OnItemClickListener() {
        @Override
        public void onClick(BaseDialog dialog, View itemView, int position) {
            setTextValue(tvQuestionType, reasonArray[position]);
            dialog.dismiss();
        }
    };*/

    private static class MyHandler extends Handler {
        WeakReference<FeedbackActivity> mActivityWeakReference;

        MyHandler(FeedbackActivity dataActivity) {
            mActivityWeakReference = new WeakReference<>(dataActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mActivityWeakReference.get().updateProgress(msg.arg1);
                    break;
                default:
                    break;
            }
        }
    }


    private void updateProgress(int progress) {
        TourCooLogUtil.i("进度：" + progress);
        if (hud != null) {
            hud.setProgress(progress);
        }
    }


    private void initProgressDialog() {
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                .setCancellable(false)
                .setAutoDismiss(false)
                .setMaxProgress(100);
        hud.setProgress(0);
    }


    private void showHudProgressDialog(int current) {
        if (hud != null) {
            hud.setProgress(0);
        } else {
            initProgressDialog();
        }
        hud.setProgress(0);
        hud.setLabel("正在上传第" + current + "张图片");
        hud.show();
    }


    private void closeHudProgressDialog() {
        if (hud != null && hud.isShowing()) {
            hud.setProgress(0);
            hud.dismiss();
        }
        hud = null;
    }


    private void setTextValue(TextView textView, String value) {
        textView.setText(value);
    }


    /**
     * 取消订单
     *
     * @param
     */
    private void requestFeedback() {
        String defaultValue = "点击选择";
        if (defaultValue.equals(getTextValue(tvQuestionType))) {
            ToastUtil.showSuccess("请选择问题类型");
            return;
        }
        String detail = etDetail.getText().toString();
        String type = getTextValue(tvQuestionType);
        /*ApiRepository.getInstance().requestFeedback(detail, mImages, type).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                setResult(RESULT_OK);
                                ToastUtil.showSuccess("问题已提交,感谢您的反馈");
                                finish();
                            } else {
                                ToastUtil.showFailed(entity.msg);
                            }
                        }
                    }
                });*/
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    uploadImageAdapter.setList(selectList);
                    imagePathList.clear();
                    for (LocalMedia localMedia : selectList) {
                        imagePathList.add(localMedia.getCompressPath());
                    }
                    uploadImageAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }


    private void requestFeedbackReasonListAndShow() {
        ApiRepository.getInstance().requestFeedbackReasonList().compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(new BaseLoadingObserver<BaseResult<List<FeedBackEntity>>>() {
            @Override
            public void onSuccessNext(BaseResult<List<FeedBackEntity>> entity) {
                if (entity == null) {
                    return;
                }
                if (entity.code == RequestConfig.CODE_REQUEST_SUCCESS) {
                    initPickerAndShow(entity.getData()) ;
                } else {
                    ToastUtil.show(entity.msg);
                }


            }
        });
    }

    private void initPickerAndShow(List<FeedBackEntity> reasonList) {
        if(reasonList == null){
            ToastUtil.show("未获取到问题");
            return;
        }
        options1Items.clear();
        options1Items.addAll(reasonList);
        pvCustomOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                selectReason = options1Items.get(options1);
                //todo
                showSelectReason(selectReason);
                //关键点 选择年份后重新请求数据
//                requestStudyData(selectYear + "");
            }
        }).setCyclic(false, false, false)
                .isDialog(false)
                .setContentTextSize(18)
                .setLineSpacingMultiplier(2.0f)
                .setDividerColor(CommonUtil.getColor(R.color.transparent))
                .setTextColorOut(CommonUtil.getColor(R.color.gray999999))
                .setTextColorCenter(CommonUtil.getColor(R.color.black333333))
                .build()
        ;

        pvCustomOptions.setPicker(options1Items);
        pvCustomOptions.show();
        //添加数据
    }

    private void showSelectReason(FeedBackEntity selectReson) {
        tvQuestionType.setText(CommonUtil.getNotNullValue(selectReson.getName()));
    }

}
