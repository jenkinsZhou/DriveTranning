package com.tourcoo.training.ui.training;

import android.os.Bundle;
import android.webkit.WebView;

import com.tourcoo.training.R;
import com.tourcoo.training.core.base.activity.BaseTitleActivity;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月28日15:43
 * @Email: 971613168@qq.com
 */
public class RichWebViewActivity extends BaseTitleActivity {

    public static final String EXTRA_RICH_TEXT = "EXTRA_RICH_TEXT";

    @Override
    public int getContentLayout() {
        return R.layout.activity_webview_rich_text;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        String richContent = getIntent().getStringExtra(EXTRA_RICH_TEXT);
        WebView webView = findViewById(R.id.webView);
        loadWebInfo(webView, CommonUtil.getNotNullValue(richContent));
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("注册协议");
    }


    /**
     * 处理图片视频填充手机宽度
     */
    private void loadWebInfo(WebView webView, String content) {
        Document doc = Jsoup.parse(content);
        //修改视频标签
        Elements embeds = doc.getElementsByTag("embed");
        for (Element element : embeds) {
            //宽度填充手机，高度自适应
            element.attr("width", "100%").attr("height", "auto");
        }
        //webview 无法正确识别 embed 为视频，所以这里把这个标签改成 video 手机就可以识别了
        doc.select("embed").tagName("video");

        //控制图片的大小
        Elements imgs = doc.getElementsByTag("img");
        for (int i = 0; i < imgs.size(); i++) {
            //宽度填充手机，高度自适应
            imgs.get(i).attr("style", "width: 100%; height: auto;");
        }

        //对数据进行包装,除去WebView默认存在的一定像素的边距问题
        String data = "<html><head><style>img{width:100% !important;}</style></head><body style='margin:0;padding:0'>" + doc + "</body></html>";

        //加载使用 jsoup 处理过的 html 文本
        webView.loadData(data, "text/html; charset=UTF-8", null);
    }

}
