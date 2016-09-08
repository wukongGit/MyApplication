package com.suncheng.myapplication.net.controller;
import com.suncheng.myapplication.model.Article;
import com.suncheng.myapplication.net.controller.BaseLocalDao;

import java.util.List;

/**
 * Created by suncheng on 2016/9/7.
 */
public class JsoupLocalDao extends BaseLocalDao {
    public List<Article> getArticleList() {
        List<Article> mArticleListPager = null;
        Object obj = readObj(createFileName("Article"));
        if(obj!=null){
            mArticleListPager = (List<Article>)obj;
        }
        return mArticleListPager;
    }

    public void setArticleList( List<Article> mArticleListPager)
    {
        writeObj(createFileName("Article"), mArticleListPager);
    }
}
