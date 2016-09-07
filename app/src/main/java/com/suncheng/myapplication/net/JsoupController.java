package com.suncheng.myapplication.net;

import com.suncheng.myapplication.framework.Constants;
import com.suncheng.myapplication.model.Article;
import java.util.List;

/**
 * Created by suncheng on 2016/9/6.
 */
public class JsoupController extends BaseController {
    private JsoupDao mJsoupDao;

    public JsoupController(){
        this.mJsoupDao = new JsoupDao();
    }

    public void getArticleList(UpdateViewAsyncCallback callBack, final int pageNum) {
        doAsyncTask("get_article_list", callBack,
                new DoAsyncTaskCallback<Void, List<Article>>() {

                    @Override
                    public List<Article> doAsyncTask(Void... params) throws Exception {
                        String url = Constants.POCO_URL + pageNum;
                        return mJsoupDao.getArticleList(url);
                    }
                }, (Void) null);
    }

    public void getArticleDetail(UpdateViewAsyncCallback callBack, final String url) {
        doAsyncTask("get_article_detail", callBack,
                new DoAsyncTaskCallback<Void, List<String>>() {
                    @Override
                    public List<String> doAsyncTask(Void... params) throws Exception {
                        return mJsoupDao.getArticleDetail(url);
                    }
                }, (Void) null);
    }
}
