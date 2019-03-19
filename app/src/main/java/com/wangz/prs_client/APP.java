package com.wangz.prs_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ListView;


public class APP extends AppCompatActivity {
    ListView menuDrawer ; //侧滑菜单视图
    DrawerAdapter menuDrawerAdapter ; // 侧滑菜单ListView的Adapter
    DrawerLayout mDrawerLayout ; // DrawerLayout组件
    //当前的内容视图下（即侧滑菜单关闭状态下），ActionBar上的标题,
    String currentContentTitle ;
    ActionBarDrawerToggle mDrawerToggle ; //侧滑菜单状态监听器

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //开始时显示全局标题“PRS”
        currentContentTitle = getResources().getString(R.string.global_title) ;

        //为侧滑菜单设置Adapter，并为ListView添加单击事件监听器
        menuDrawer = (ListView)findViewById(R.id.left_drawer) ;
        menuDrawerAdapter = new DrawerAdapter(this) ;
        menuDrawer.setAdapter(menuDrawerAdapter);
        menuDrawer.setOnItemClickListener(new DrawerItemClickListener());

        //为DrawerLayout注册状态监听器
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new DrawerMenuToggle(
                this, mDrawerLayout, R.drawable.ic_launcher_background, R.string.drawer_open, R.string.drawer_close) ;
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //设置ActionBar的指示图标可见，设置ActionBar上的应用图标位置处可以被单击
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(currentContentTitle);
        //隐藏ActionBar上的应用图标，只显示文字label
        getSupportActionBar().setDisplayShowHomeEnabled(false);


    }

    /**侧滑菜单单击事件监听器*/
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            selectItem(position);

        }

        public void selectItem(int position){

            //为内容视图加载新的Fragment
            Bundle bd = new Bundle() ;
            String selected_item = "selected_item";
            bd.putString(ContentFragment.SELECTED_ITEM,menuDrawerAdapter.getItem(position).menuTitle);
            bd.putString(selected_item,menuDrawerAdapter.getItem(position).menuTitle);
            FragmentManager fragmentManager =getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();


            //替换Fragment
            switch ((String)bd.get(selected_item)){
                case "管理":
                    Fragment contentFragment = new ContentFragmentManager( ) ;
                    contentFragment.setArguments(bd);


                    transaction.replace(R.id.content_frame, contentFragment);
                    break;
                case "推荐":
                    Fragment contentFragment1 = new ContentFragment( ) ;
                    contentFragment1.setArguments(bd);


                    transaction.replace(R.id.content_frame, contentFragment1);
                    break;
                case "发现":
                    Fragment contentFragment2 = new TestTrans( ) ;
                    contentFragment2.setArguments(bd);
                    transaction.replace(R.id.content_frame, contentFragment2);
                    break;
                case "交易":
                    Fragment contentFragment3 = new ContentFragmentTrans( ) ;
                    contentFragment3.setArguments(bd);
                    transaction.replace(R.id.content_frame, contentFragment3);
                    break;

                default:
                    break;
            }
            transaction.commit();


            //将选中的菜单项置为高亮
            menuDrawer.setItemChecked(position, true);
            //将ActionBar中标题更改为选中的标题项
            setTitle(menuDrawerAdapter.getItem(position).menuTitle);
            //将当前的侧滑菜单关闭，调用DrawerLayout的closeDrawer（）方法即可
            mDrawerLayout.closeDrawer(menuDrawer);
        }

        public void setTitle( String title ){
            currentContentTitle = title ; // 更改当前的CurrentContentTitle标题内容
            getSupportActionBar().setTitle(title);

        }
    }

    /**侧滑菜单状态监听器（开、关），通过继承ActionBarDrawerToggle实现*/
    private class DrawerMenuToggle extends ActionBarDrawerToggle{

        /**
         * @param drawerLayout ：就是加载的DrawerLayout容器组件
         * @param drawerImageRes ： 要使用的ActionBar左上角的指示图标
         * @param openDrawerContentDescRes 、closeDrawerContentDescRes：开启和关闭的两个描述字段，没有太大的用处
         *
         * */
        public DrawerMenuToggle(Activity activity, DrawerLayout drawerLayout,
                                int drawerImageRes, int openDrawerContentDescRes,
                                int closeDrawerContentDescRes) {

            super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes,closeDrawerContentDescRes);

        }

        /** 当侧滑菜单达到完全关闭的状态时，回调这个方法 */
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            //当侧滑菜单关闭后，显示ListView选中项的标题，如果并没有点击ListView中的任何项，那么显示原来的标题
            getSupportActionBar().setTitle(currentContentTitle);
            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }

        /** 当侧滑菜单完全打开时，这个方法被回调 */
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            getSupportActionBar().setTitle(R.string.app_name); //当侧滑菜单打开时ActionBar显示全局标题
            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }
    };

    /**为了能够让ActionBarDrawerToggle监听器
     * 能够在Activity的整个生命周期中都能够以正确的逻辑工作
     * 需要添加下面两个方法*/
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**最后做一些菜单上处理*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //第一个if 要加上，为的是让ActionBarDrawerToggle以正常的逻辑工作
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_websearch){
            Toast.makeText(this, "webSearch 菜单项被单击", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**每次调用 invalidateOptionsMenu() ，下面的这个方法就会被回调*/
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // 如果侧滑菜单的状态监听器在侧滑菜单打开和关闭时都调用了invalidateOptionsMenu()方法，
        //当侧滑菜单打开时将ActionBar上的某些菜单图标隐藏起来，使得这时仅显示全局标题
        //本应用中是将ActiongBar上的action菜单项隐藏起来

        boolean drawerOpen = mDrawerLayout.isDrawerOpen(menuDrawer);//判定当前侧滑菜单的状态
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**《当用户按下了"手机上的返回功能按键"的时候会回调这个方法》*/
    @Override
    public void onBackPressed() {
        boolean drawerState =  mDrawerLayout.isDrawerOpen(menuDrawer);
        if (drawerState) {
            mDrawerLayout.closeDrawers();
            return;
        }
        //也就是说，当按下返回功能键的时候，不是直接对Activity进行弹栈，而是先将菜单视图关闭
        super.onBackPressed();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
        // 程序退出到后台 仍然保持运行
    }



}
