package com.tourcoo.training.ui.home.news;

import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * webview 工具类
 */
public class HtmlUtils {


    /**
     * 处理图片视频填充手机宽度
     */
    public static String imageFillWidth(String content) {
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
//        val data = "<html><head><style>img{width:100% !important;}</style></head><body style='margin:0;padding:0'>${doc}</body></html>"
        return doc.toString();
    }


    /**
     * 找到html中所有的图片url
     *
     * @param htmlCode
     * @return
     */
    public static List<String> returnImageUrlsFromHtml(String htmlCode) {
        List<String> imageSrcList = new ArrayList<String>();
        Pattern p = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic|\\b)\\b)[^>]*>", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);
        String quote = null;
        String src = null;
        while (m.find()) {
            quote = m.group(1);
            src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("//s+")[0] : m.group(2);
            imageSrcList.add(src);
        }
        if (imageSrcList.size() == 0) {
            return null;
        }
        return imageSrcList;
    }


    /**
     * 设置网页中图片的点击事件
     * 与 AndroidInterface 配合使用,获取点击回调事件
     *
     * @param view
     */
    public static void setWebImageClick(WebView view, String method) {
        String jsCode = "javascript:(function(){" +
                "var imgs=document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<imgs.length;i++){" +
                "imgs[i].pos = i;" +
                "imgs[i].onclick=function(){" +
                "window." + method + ".openImage(this.src,this.pos);" +
                "}}})()";
        view.loadUrl(jsCode);
    }


    public static String getHtmlString(String title, String time, String source, String content) {

        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\">\n" +
                "    <title>" + title + "</title>\n" +
                "    <style>\n" +
                "        * {\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            -webkit-tap-highlight-color: rgba(0, 0, 0, 0);\n" +
                "            box-sizing: border-box;\n" +
                "        }\n" +
                "        html{\n" +
                "            font-size: 312.5%;\n" +
                "        }\n" +
                "        body{\n" +
                "            overflow-x: hidden;\n" +
                "        }\n" +
                "        .detailWrapper{\n" +
                "\n" +
                "        }\n" +
                "        .detailWrapper .detail_head{\n" +
                "            padding:.2rem;\n" +

                "        }\n" +
                "        .detailWrapper .detail_head .head_top{\n" +
                "            font-size:.32rem;\n" +
                "            font-weight:bold;\n" +
                "        }\n" +
                "        .detailWrapper .detail_head .head_bottom{\n" +
                "            margin-top:.2rem;\n" +
                "            font-size: .26rem;\n" +
                "            color: #ccc;\n" +
                "        }\n" +
                "        .detailWrapper .detail_head .head_bottom span:nth-of-type(2){\n" +
                "            margin-left:.2rem;\n" +
                "        }\n" +
                "        .detailWrapper .detail_content{\n" +
                "            margin-top:.1rem;\n" +
                "            padding:.2rem;\n" +
                "             font-size: .32rem!important;\n" +
                "             line-hight: .15rem!important;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"detailWrapper\">\n" +
                "        <div class=\"detail_head\">\n" +
                "            <div class=\"head_top\">" + title + "</div>\n" +
                "            <div class=\"head_bottom\">\n" +
                "                <span>" + time + "</span>\n" +
                "                <span>" + source + "</span>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"detail_content\">\n" +
                content +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

}

