package com.suncheng.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.suncheng.myapplication.adapter.NewsAdapter;
import com.suncheng.myapplication.framework.BaseActivity;
import com.suncheng.myapplication.model.Article;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ListView mListView;
    private NewsAdapter mAdapter;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setTitle("演示");
        setContentView(R.layout.activity_main);
        setBackListner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();

    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.news_list);
        mAdapter = new NewsAdapter(this);
        try {
            ProgressAsyncTask asyncTask=new ProgressAsyncTask();
            asyncTask.execute(10000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis() - exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class ProgressAsyncTask extends AsyncTask<Integer, Integer, List<Article>> {
        public ProgressAsyncTask() {
            super();
        }

        @Override
        protected List<Article> doInBackground(Integer... params) {
            List<Article> list = new ArrayList<>();
            Document doc = null;
            try {
                doc = Jsoup.connect("http://jcodecraeer.com/plus/list.php?tid=4").get();
                Elements ListDiv = doc.getElementsByClass("archive-item");
                for (Element element :ListDiv) {
                    Element detail = element.getElementsByClass("archive-detail").get(0);
                    String title = detail.getElementsByTag("h3").text();
                    String content = detail.getElementsByTag("p").text();
                    Article article = new Article();
                    article.setTitle(title);
                    article.setContent(content);
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
            mAdapter.setData(result);
            mListView.setAdapter(mAdapter);
        }

    }

}
