package com.tourcoo.training.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.gson.Gson
import com.tourcoo.training.R
import com.tourcoo.training.core.util.CommonUtil
import com.tourcoo.training.core.util.ToastUtil
import com.tourcoo.training.entity.account.AccountHelper
import com.tourcoo.training.entity.news.NewsEntity
import com.tourcoo.training.ui.account.LoginActivity
import com.tourcoo.training.ui.home.news.NewsDetailHtmlActivity
import com.tourcoo.training.ui.home.news.NewsDetailVideoActivity
import com.tourcoo.training.ui.home.news.NewsTabFragment.Companion.EXTRA_NEWS_BEAN

class SchemeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheme)

        if (AccountHelper.getInstance().isLogin) {
            isSchemeIntentData(this)
        } else {
            CommonUtil.startActivity(this, LoginActivity::class.java)
        }
        finish()
    }

    private fun isSchemeIntentData(mContext: Context): Boolean {

        val uri = intent.data
        if (uri != null) {
            val path = uri.path
            val module = uri.getQueryParameter("json")

            ToastUtil.show("$path     $module")

            val bean = Gson().fromJson(module, NewsEntity::class.java)
            bean.title = bean.tiTle

            val bundle = Bundle()
            bundle.putSerializable(EXTRA_NEWS_BEAN, bean)
            if (!TextUtils.isEmpty(CommonUtil.getNotNullValue(bean.videoUrl))) {
                CommonUtil.startActivity(mContext, NewsDetailVideoActivity::class.java, bundle)
            } else {
                CommonUtil.startActivity(mContext, NewsDetailHtmlActivity::class.java, bundle)
            }
            return true
        } else {
            return false
        }
    }

}
