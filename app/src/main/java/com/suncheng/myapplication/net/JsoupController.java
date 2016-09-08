package com.suncheng.myapplication.net;

import com.suncheng.myapplication.framework.Constants;
import com.suncheng.myapplication.model.Article;
import java.util.List;

/**
 * Created by suncheng on 2016/9/6.
 */
public class JsoupController extends BaseController {
    private JsoupRemoteDao mJsoupRemoteDao;
    private JsoupLocalDao mJsoupLocalDao;

    public JsoupController(){
        this.mJsoupRemoteDao = new JsoupRemoteDao();
        this.mJsoupLocalDao = new JsoupLocalDao();
    }

    public void getArticleList(UpdateViewAsyncCallback callBack, final int pageNum) {
        doAsyncTask("get_article_list", callBack,
                new DoAsyncTaskCallback<Void, List<Article>>() {

                    @Override
                    public List<Article> doAsyncTask(Void... params) throws Exception {
                        String url = Constants.POCO_URL + pageNum;
                        return mJsoupRemoteDao.getArticleList(url);
                    }
                }, (Void) null);
    }

    public void getArticleDetail(UpdateViewAsyncCallback callBack, final String url) {
        doAsyncTask("get_article_detail", callBack,
                new DoAsyncTaskCallback<Void, List<String>>() {
                    @Override
                    public List<String> doAsyncTask(Void... params) throws Exception {
                        return mJsoupRemoteDao.getArticleDetail(url);
                    }
                }, (Void) null);
    }

    public void getArticleListLocal(UpdateViewAsyncCallback callBack)
    {
        doAsyncTask("get_article_list_local", callBack,
                new DoAsyncTaskCallback<Void , List<Article>>() {

                    @Override
                    public List<Article> doAsyncTask(Void... params) throws Exception
                    {
                        return mJsoupLocalDao.getArticleList();
                    }
                }, (Void)null);
    }

    public void setArticleListLocal(final List<Article> mArticleListPager)
    {
        doAsyncTask("set_article_list_local", new NullCallback(),
                new DoAsyncTaskCallback<Void , Void>() {

                    @Override
                    public Void doAsyncTask(Void... params) throws Exception
                    {
                        mJsoupLocalDao.setArticleList(mArticleListPager);
                        return null;
                    }
                }, (Void)null);
    }
}
