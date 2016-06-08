#ShallCheek_TanTan
有一阵耍探探看着效果图还可以，然后又在网上看到了一个动态切换的开源库<a title="Swipecards" href="https://github.com/Diolor/Swipecards" target="_blank">Swipecards</a>便拿来修改模仿探探滑动界面

博客<a title="Swipecards" href="http://blog.csdn.net/shallcheek/article/details/46606523" target="_blank">地址</a>



```java
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:22.2.0'
    compile 'com.github.bumptech.glide:glide:3.5.2'
}
```

##看看效果对比 
####探探的效果
![这里写图片描述](http://img.blog.csdn.net/20150623144428229)
####模仿的效果
最新的模板
<img src="./img/device-2016-06-08-145242.jpg" width="40%"><img>

ps:毕竟只模仿大概的界面可能也有BUG没好好测试!

##Example

```java
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

