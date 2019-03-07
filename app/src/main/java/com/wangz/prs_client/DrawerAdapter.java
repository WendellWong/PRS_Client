package com.wangz.prs_client;

/**
 * Created by WZ on 2018/12/9.
 */
        import java.util.ArrayList;
        import java.util.List;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.TextView;

/**定义菜单项类*/
class TuiCoolMenuItem {
    String menuTitle ;
    int menuIcon ;

    //构造方法
    public TuiCoolMenuItem(String menuTitle , int menuIcon ){
        this.menuTitle = menuTitle ;
        this.menuIcon = menuIcon ;
    }

}
/**自定义设置侧滑菜单ListView的Adapter*/
public class DrawerAdapter extends BaseAdapter{

    //存储侧滑菜单中的各项的数据
    List<TuiCoolMenuItem> MenuItems = new ArrayList<TuiCoolMenuItem>( ) ;
    //构造方法中传过来的activity
    Context context ;

    //构造方法
    public DrawerAdapter( Context context ){

        this.context = context ;

        MenuItems.add(new TuiCoolMenuItem("", R.drawable.avatar)) ;
        MenuItems.add(new TuiCoolMenuItem("推荐", R.drawable.recommend)) ;
        MenuItems.add(new TuiCoolMenuItem("发现", R.drawable.discover)) ;
        MenuItems.add(new TuiCoolMenuItem("管理", R.drawable.theme)) ;
        MenuItems.add(new TuiCoolMenuItem("交易", R.drawable.site)) ;
//        MenuItems.add(new TuiCoolMenuItem("搜索", R.drawable.search)) ;
//        MenuItems.add(new TuiCoolMenuItem("离线", R.drawable.offline)) ;
//        MenuItems.add(new TuiCoolMenuItem("设置", R.drawable.setting)) ;
    }

    @Override
    public int getCount() {

        return MenuItems.size();

    }

    @Override
    public TuiCoolMenuItem getItem(int position) {

        return MenuItems.get(position) ;
    }

    @Override
    public long getItemId(int position) {

        return position ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView ;
        if(view == null){
            view =LayoutInflater.from(context).inflate(R.layout.menudrawer_item, parent, false);
            ((TextView) view).setText(getItem(position).menuTitle) ;
            ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(getItem(position).menuIcon, 0, 0, 0) ;
        }
        return view ;
    }

}