package com.suncheng.myapplication.net;

import android.os.AsyncTask;

import com.suncheng.myapplication.framework.Constants;
import com.suncheng.myapplication.model.Article;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suncheng on 2016/9/6.
 */
public class JsoupController {
    private OnDataCallBack mCallback;

    public void getArticleList(OnDataCallBack callBack, final int pageNum)
    {
        if(callBack == null) {
            return;
        }
        mCallback = callBack;
        final String url = Constants.POCO_URL + pageNum;
        new ProgressAsyncTask().execute(url);
    }


    class ProgressAsyncTask extends AsyncTask<String, Integer, List<Article>> {
        public ProgressAsyncTask() {
            super();
        }

        @Override
        protected List<Article> doInBackground(String... params) {
            List<Article> list = new ArrayList<>();
            Document doc = null;
            try {
                doc = Jsoup.connect(params[0]).get();
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

        @Override
        protected void onPostExecute(List<Article> result) {
            if(mCallback != null) {
                mCallback.onSuccess(result, 0, "ok");
            }
        }

    }

}
