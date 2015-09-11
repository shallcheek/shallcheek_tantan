# shallcheek_tantan
网上看到了一个动态切换的开源库，便拿来修改模仿出主界面
开源库卡片切换: https://github.com/Diolor/Swipecards 和开源图片浏览工具添加

```

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:22.2.0'
    compile 'com.github.bumptech.glide:glide:3.5.2'
}

```

##看看效果对比 
####探探的效果：
![这里写图片描述](http://img.blog.csdn.net/20150623144428229)
####模仿的效果
ps:毕竟只模仿大概的东西小细节没有去弄，毕竟也是挺费时间
![这里写图片描述](http://img.blog.csdn.net/20150623144821365)
关键的代码我也没有写多少只是修改了些界面而已 

##监听函数

```
       flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
      //设置适配器
        flingContainer.setAdapter(adapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                al.remove(0);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onLeftCardExit(Object dataObject) {
                makeToast(MainActivity.this, "不喜欢");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                makeToast(MainActivity.this, "喜欢");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                al.add(new CardMode("循环测试", 18, list.get(itemsInAdapter % imageUrls.length - 1)));
                adapter.notifyDataSetChanged();
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });
```

博客地址:http://blog.csdn.net/shallcheek/article/details/46606523

