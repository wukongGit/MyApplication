package com.suncheng.myapplication.net;

import android.util.Log;

import com.suncheng.myapplication.model.Article;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suncheng on 2016/9/7.
 */
public class JsoupDao {
    public List<Article> getArticleList(String removeUrl) {
        List<Article> list = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(removeUrl).get();
            Elements ListDiv = doc.getElementsByClass("mod-txtimg230-list");
            Elements subDiv = ListDiv.get(0).getElementsByClass("clearfix");
            Elements subItemDiv = subDiv.get(0).getElementsByTag("li");

            for (Element element :subItemDiv) {
                Element imgElement = element.getElementsByClass("img-box").get(0);
                Element item = imgElement.getElementsByTag("a").first();
                String title = item.attr("title");
                String url = item.attr("href");
                Element img = item.getElementsByTag("img").first();
                String imgUrl = img.attr("src");

                Element txtElement = element.getElementsByClass("txt-box").get(0);
                Element authorElement = txtElement.getElementsByClass("color6").get(0);
                Elements author = authorElement.getElementsByTag("a");
                String authorString = "";
                if(author != null && author.size() >= 2) {
                    authorString = author.get(1).attr("title");
                }
                Element detailElement = txtElement.getElementsByClass("clearfix").get(0);
                Elements detail = detailElement.getElementsByTag("span");
                String scan = "";
                String praise = "";
                String commit = "";
                if(detail != null && detail.size() >= 3) {
                    scan = detail.get(0).getElementsByClass("fl").text();
                    praise = detail.get(1).getElementsByClass("fl").text();
                    commit = detail.get(2).getElementsByClass("fl").text();
                }

                Article article = new Article();
                article.setAuthor(authorString);
                article.setTitle(title);
                article.setUrl(url);
                article.setImgUrl(imgUrl);
                article.setScan(scan);
                article.setPraise(praise);
                article.setCommit(commit);
                list.add(article);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getArticleDetail(String removeUrl) {
        List<String> list = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(removeUrl).get();
            Elements ListDiv = doc.getElementsByTag("script");

            for (Element element : ListDiv) {
                String script = element.toString();
                int hasIndex = script.indexOf("actBase.init");
                if(hasIndex > -1) {
                    int startIndex = script.indexOf("(", hasIndex);
                    int lastIndex = script.indexOf(")", hasIndex);
                    String json = script.substring(startIndex + 1, lastIndex);
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject actInfo = jsonObject.optJSONObject("actInfo");
                    JSONArray imgs = actInfo.optJSONArray("photoData");
                    int imgSize = imgs.length();
                    for(int i = 0; i < imgSize; i++) {
                        JSONObject imgObj = imgs.optJSONObject(i);
                        String imgUrl = imgObj.optString("originPhoto");
                        list.add(imgUrl);
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
}
