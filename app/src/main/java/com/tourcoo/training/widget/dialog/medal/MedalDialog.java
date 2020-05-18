package com.tourcoo.training.widget.dialog.medal;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourcoo.training.R;
import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.entity.account.AccountTempHelper;
import com.tourcoo.training.entity.medal.MedalInfo;
import com.tourcoo.training.entity.medal.StudyMedalEntity;
import com.tourcoo.training.entity.study.StudyMedal;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.List;


/**
 * @author :JenkinsZhou
 * @description :勋章相关dialog
 * @company :途酷科技
 * @date 2020年04月07日10:37
 * @Email: 971613168@qq.com
 */
public class MedalDialog {
    private Context mContext;
    private Dialog dialog;
    private TextView tvPositive;
    private TextView tvTitle;
    private TextView tvContent;
    private ImageView ivMedalIcon;

    public MedalDialog(Context context) {
        this.mContext = context;
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
    }

    public MedalDialog create() {
        // 获取Dialog布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_medal_commen, null);
        // 设置Dialog最小宽度为屏幕宽度
//        view.setMinimumWidth(width);
        // 定义Dialog布局和参数
        dialog = new Dialog(mContext, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        tvPositive = view.findViewById(R.id.tvPositive);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvContent = view.findViewById(R.id.tvContent);
        ivMedalIcon = view.findViewById(R.id.ivMedalIcon);
        view.findViewById(R.id.ivCloseRect).setOnClickListener(v -> dismiss());
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager m = window.getWindowManager();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            //宽高可设置具体大小
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
            // 注释：dialog.onWindowAttributesChanged(lp);
            window.setAttributes(lp);
            // 获取屏幕宽、高用
            Display d = m.getDefaultDisplay();
            // 获取对话框当前的参数值
            WindowManager.LayoutParams p = window.getAttributes();
            Display display = m.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            // 宽度设置为屏幕的0.8
            p.width = (int) (width * 0.85);
            window.setAttributes(p);
        }
        return this;
    }


    private boolean isShow = false;
    private int index = -1;
    private int mType = -1;

    /**
     * @param type   类型  1：学习   2：证书   3：消费
     * @param number 与类型匹配的规则数值
     * @return
     */


    public MedalDialog setMedal(int type, int number, StudyMedalEntity studyMedalEntity) {
        mType = type;

        if(studyMedalEntity == null){
            isShow = false;
            return this;
        }

        switch (type) {
            case 1:
                //number 小时
                List<MedalInfo> list = studyMedalEntity.getStudyMedals();

                if (number >= 1 && number < 6) {
                    isShow = list.get(0).getStatus() == 0;
                    if (isShow) {
                        index = 0;
                        if (tvTitle != null) {
                            tvTitle.setText("恭喜您获得·安培新星勋章");
                        }

                        if (tvContent != null) {
                            tvContent.setText("累计学习时长已达1个学时");
                        }

                        requestMedalRecord("1001");
                    }
                    ivMedalIcon.setImageResource(R.drawable.img_01);
                } else if (number >= 6 && number < 12) {
                    isShow = list.get(1).getStatus() == 0;
                    if (isShow) {
                        index = 1;

                        if (tvTitle != null) {
                            tvTitle.setText("恭喜您获得·贵在坚持勋章");
                        }

                        if (tvContent != null) {
                            tvContent.setText("累计学习时长已达6个学时");
                        }


                        requestMedalRecord("1002");
                    }
                    ivMedalIcon.setImageResource(R.drawable.img_02);
                } else if (number >= 12 && number < 20) {
                    isShow = list.get(2).getStatus() == 0;
                    if (isShow) {
                        index = 2;
                        if (tvTitle != null) {
                            tvTitle.setText("恭喜您获得·学习之星勋章");
                        }

                        if (tvContent != null) {
                            tvContent.setText("累计学习时长已达12个学时");
                        }

                        requestMedalRecord("1003");
                    }
                    ivMedalIcon.setImageResource(R.drawable.img_03);
                } else if (number >= 20 && number < 30) {
                    isShow = list.get(3).getStatus() == 0;
                    if (isShow) {
                        index = 3;
                        if (tvTitle != null) {
                            tvTitle.setText("恭喜您获得·持之以恒勋章");
                        }

                        if (tvContent != null) {
                            tvContent.setText("累计学习时长已达20个学时");
                        }

                        requestMedalRecord("1004");
                    }
                    ivMedalIcon.setImageResource(R.drawable.img_04);
                } else if (number >= 30) {
                    isShow = list.get(4).getStatus() == 0;
                    if (isShow) {
                        index = 4;
                        if (tvTitle != null) {
                            tvTitle.setText("恭喜您获得·安培代言人勋章");
                        }

                        if (tvContent != null) {
                            tvContent.setText("累计学习时长已达30个学时");
                        }

                        requestMedalRecord("1005");
                    }
                    ivMedalIcon.setImageResource(R.drawable.img_05);
                }


                break;
            case 2:
                //number
                list = studyMedalEntity.getCertificateMedals();

                if (number >= 1 && number < 6) {
                    isShow = list.get(0).getStatus() == 0;
                    if (isShow) {
                        index = 0;
                        if (tvTitle != null) {
                            tvTitle.setText("恭喜您获得·牛刀小试勋章");
                        }

                        if (tvContent != null) {
                            tvContent.setText("累计获得1个学习证书");
                        }

                        requestMedalRecord("2001");
                    }
                    ivMedalIcon.setImageResource(R.drawable.img_06);
                } else if (number >= 6 && number < 12) {
                    isShow = list.get(1).getStatus() == 0;
                    if (isShow) {
                        index = 1;
                        if (tvTitle != null) {
                            tvTitle.setText("恭喜您获得·小有成就勋章");
                        }

                        if (tvContent != null) {
                            tvContent.setText("累计获得6个学习证书");
                        }
                        requestMedalRecord("2002");
                    }
                    ivMedalIcon.setImageResource(R.drawable.img_07);
                } else if (number >= 12 && number < 20) {
                    isShow = list.get(2).getStatus() == 0;
                    if (isShow) {
                        index = 2;
                        if (tvTitle != null) {
                            tvTitle.setText("恭喜您获得·登堂入室勋章");
                        }

                        if (tvContent != null) {
                            tvContent.setText("累计获得12个学习证书");
                        }
                        requestMedalRecord("2003");
                    }
                    ivMedalIcon.setImageResource(R.drawable.img_08);
                } else if (number >= 20 && number < 30) {
                    isShow = list.get(3).getStatus() == 0;
                    if (isShow) {
                        index = 3;
                        if (tvTitle != null) {
                            tvTitle.setText("恭喜您获得·安全达人勋章");
                        }

                        if (tvContent != null) {
                            tvContent.setText("累计获得20个学习证书");
                        }
                        requestMedalRecord("2004");
                    }
                    ivMedalIcon.setImageResource(R.drawable.img_09);
                } else if (number >= 30) {
                    isShow = list.get(4).getStatus() == 0;
                    if (isShow) {
                        index = 4;
                        if (tvTitle != null) {
                            tvTitle.setText("恭喜您获得·证书终结者勋章");
                        }

                        if (tvContent != null) {
                            tvContent.setText("累计获得30个学习证书");
                        }
                        requestMedalRecord("2005");
                    }
                    ivMedalIcon.setImageResource(R.drawable.img_10);
                }

                break;
            case 3:
                //number
                list = studyMedalEntity.getConsumptionMedals();

                if (number > 0 && number < 50) {
                    isShow = list.get(0).getStatus() == 0;
                    if (isShow) {
                        index = 0;
                        if (tvTitle != null) {
                            tvTitle.setText("恭喜您获得·安培萌新勋章");
                        }

                        if (tvContent != null) {
                            tvContent.setText("首次充值");
                        }
                        requestMedalRecord("3001");
                    }
                    ivMedalIcon.setImageResource(R.drawable.img_11);
                } else if (number >= 50 && number < 200) {
                    isShow = list.get(1).getStatus() == 0;
                    if (isShow) {
                        index = 1;
                        if (tvTitle != null) {
                            tvTitle.setText("恭喜您获得·安培新贵勋章");
                        }

                        if (tvContent != null) {
                            tvContent.setText("累计消费已达50元");
                        }
                        requestMedalRecord("3002");
                    }
                    ivMedalIcon.setImageResource(R.drawable.img_12);
                } else if (number >= 200 && number < 1000) {
                    isShow = list.get(2).getStatus() == 0;
                    if (isShow) {
                        index = 2;
                        if (tvTitle != null) {
                            tvTitle.setText("恭喜您获得·安培豪杰勋章");
                        }

                        if (tvContent != null) {
                            tvContent.setText("累计消费已达200元");
                        }
                        requestMedalRecord("3003");
                    }
                    ivMedalIcon.setImageResource(R.drawable.img_13);
                } else if (number >= 1000 && number < 3000) {
                    isShow = list.get(3).getStatus() == 0;
                    if (isShow) {
                        index = 3;
                        if (tvTitle != null) {
                            tvTitle.setText("恭喜您获得·安培大师勋章");
                        }

                        if (tvContent != null) {
                            tvContent.setText("累计消费已达1000元");
                        }
                        requestMedalRecord("3004");
                    }
                    ivMedalIcon.setImageResource(R.drawable.img_14);
                } else if (number >= 3000) {
                    isShow = list.get(4).getStatus() == 0;
                    if (isShow) {
                        index = 4;
                        if (tvTitle != null) {
                            tvTitle.setText("恭喜您获得·土豪不差钱勋章");
                        }

                        if (tvContent != null) {
                            tvContent.setText("累计消费已达3000元");
                        }
                        requestMedalRecord("3005");
                    }
                    ivMedalIcon.setImageResource(R.drawable.img_15);
                }

                break;
        }

        return this;
    }


    private void requestMedalRecord(String id) {

        ApiRepository.getInstance().receiveMedal(id).subscribe(
                new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccessNext(BaseResult entity) {
                        if (entity != null && entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS) {
                            if (dialog != null && isShow) {
                                dialog.show();
                            }

                            if (mType == 1) {
                                //number 小时
                                List<MedalInfo> list = AccountTempHelper.getInstance().getStudyMedalEntity().getStudyMedals();
                                if (index != -1) list.get(index).setStatus(1);

                            } else if (mType == 2) {
                                List<MedalInfo> list = AccountTempHelper.getInstance().getStudyMedalEntity().getCertificateMedals();
                                if (index != -1) list.get(index).setStatus(1);

                            } else if (mType == 3) {
                                List<MedalInfo> list = AccountTempHelper.getInstance().getStudyMedalEntity().getConsumptionMedals();
                                if (index != -1) list.get(index).setStatus(1);
                            }

                        } else {
                            if (entity == null) {
                                return;
                            }
                            ToastUtil.show(entity.msg);
                        }
                    }
                }

        );
    }


//      if (dialog != null && isShow) {
//        dialog.show();

    public boolean show() {
        return isShow;
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public MedalDialog setPositiveButtonListener(View.OnClickListener onClickListener) {
        if (tvPositive != null) {
            tvPositive.setOnClickListener(onClickListener);
        }
        return this;
    }


    public MedalDialog setTitle(View.OnClickListener onClickListener) {
        if (tvPositive != null) {
            tvPositive.setOnClickListener(onClickListener);
        }
        return this;
    }

    public MedalDialog setTitle(String title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
        return this;
    }

}
