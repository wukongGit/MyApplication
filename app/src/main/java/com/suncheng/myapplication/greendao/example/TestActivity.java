package com.suncheng.myapplication.greendao.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.suncheng.myapplication.R;
import com.suncheng.myapplication.framework.MyApplication;
import com.suncheng.myapplication.greendao.Entity.User;
import com.suncheng.myapplication.greendao.GreenDaoManager;
import com.suncheng.myapplication.greendao.gen.UserDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suncheng on 2016/9/19.
 */
public class TestActivity extends Activity implements View.OnClickListener {
    private EditText mNameET;
    private Button mAddBtn, mDeleteBtn, mUpdateBtn;
    private ListView mUserLV;

    private UserAdapter mUserAdapter;
    private List<User> mUserList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        initData();
    }

    private void initView() {
        mNameET = (EditText) findViewById(R.id.et_name);
        mAddBtn = (Button) findViewById(R.id.btn_add);
        mDeleteBtn = (Button) findViewById(R.id.btn_delete);
        mUpdateBtn = (Button) findViewById(R.id.btn_update);
        mUserLV = (ListView) findViewById(R.id.lv_user);

        mAddBtn.setOnClickListener(this);
        mDeleteBtn.setOnClickListener(this);
        mUpdateBtn.setOnClickListener(this);
    }

    private void initData() {
        mUserList = GreenDaoManager.getInstance().getSession().getUserDao().queryBuilder().build().list();
        mUserAdapter = new UserAdapter(this, mUserList);
        mUserLV.setAdapter(mUserAdapter);
    }

    /**
     * 根据名字更新某条数据的名字
     * @param prevName  原名字
     * @param newName  新名字
     */
    private void updateUser(String prevName,String newName){
        UserDao userDao = GreenDaoManager.getInstance().getSession().getUserDao();
        User findUser = userDao.queryBuilder()
                .where(UserDao.Properties.Name.eq(prevName)).build().unique();
        if(findUser != null) {
            findUser.setName(newName);
            GreenDaoManager.getInstance().getSession().getUserDao().update(findUser);
            Toast.makeText(MyApplication.getApp(), "修改成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MyApplication.getApp(), "用户不存在", Toast.LENGTH_SHORT).show();
        }

        mNameET.setText("");

        

        mUserList.clear();
        mUserList.addAll(userDao.queryBuilder().build().list());
        mUserAdapter.notifyDataSetChanged();
    }

    /**
     * 根据名字删除某用户
     * @param name
     */
    private void deleteUser(String name){
        UserDao userDao = GreenDaoManager.getInstance().getSession().getUserDao();
        User findUser = userDao.queryBuilder().where(UserDao.Properties.Name.eq(name)).build().unique();
        if(findUser != null){
            userDao.deleteByKey(findUser.getId());
        }
        mNameET.setText("");

        mUserList.clear();
        mUserList.addAll(userDao.queryBuilder().build().list());
        mUserAdapter.notifyDataSetChanged();
    }

    /**
     * 本地数据里添加一个User
     * @param id  id
     * @param name  名字
     */
    private void insertUser(Long id, String name) {
        UserDao userDao = GreenDaoManager.getInstance().getSession().getUserDao();
        User user = new User(id, name);
        userDao.insert(user);
        mNameET.setText("");

        mUserList.clear();
        mUserList.addAll(userDao.queryBuilder().build().list());
        mUserAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId){
            case R.id.btn_add:
                insertUser(null, mNameET.getText().toString());
                break;
            case R.id.btn_delete:
                deleteUser(mNameET.getText().toString());
                break;
            case R.id.btn_update:
                updateUser(mNameET.getText().toString(), "xiuxiu");
                break;
            default:
                break;
        }
    }
}

