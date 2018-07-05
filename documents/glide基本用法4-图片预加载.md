preload()方法
preload()方法有两个方法重载，一个不带参数，表示将会加载图片的原始尺寸，另一个可以通过参数指定加载图片的宽和高。
preload()方法的用法也非常简单，直接使用它来替换into()方法即可，如下所示：

    Glide.with(this)
         .load(url)
         .diskCacheStrategy(DiskCacheStrategy.SOURCE)
         .preload();

需要注意的是，我们如果使用了preload()方法，最好要将diskCacheStrategy的缓存策略指定成DiskCacheStrategy.SOURCE。
因为preload()方法默认是预加载的原始图片大小，而into()方法则默认会根据ImageView控件的大小来动态决定加载图片的大小。
因此，如果不将diskCacheStrategy的缓存策略指定成DiskCacheStrategy.SOURCE的话，
很容易会造成我们在预加载完成之后再使用into()方法加载图片，却仍然还是要从网络上去请求图片这种现象

调用了预加载之后，我们以后想再去加载这张图片就会非常快了，因为Glide会直接从缓存当中去读取图片并显示出来，代码如下所示：

    Glide.with(this)
         .load(url)
         .diskCacheStrategy(DiskCacheStrategy.SOURCE)
         .into(imageView);

注意，这里我们仍然需要使用diskCacheStrategy()方法将硬盘缓存策略指定成DiskCacheStrategy.SOURCE，
以保证Glide一定会去读取刚才预加载的图片缓存

downloadOnly()方法

和preload()方法类似，downloadOnly()方法也是可以替换into()方法的，不过downloadOnly()方法的用法明显要比preload()方法复杂不少。
顾名思义，downloadOnly()方法表示只会下载图片，而不会对图片进行加载。当图片下载完成之后，我们可以得到图片的存储路径，以便后续进行操作。

那么首先我们还是先来看下基本用法。downloadOnly()方法是定义在DrawableTypeRequest类当中的，它有两个方法重载，一个接收图片的宽度和高度，另一个接收一个泛型对象，如下所示：

* downloadOnly(int width, int height)
* downloadOnly(Y target)

这两个方法各自有各自的应用场景，其中downloadOnly(int width, int height)是用于在子线程中下载图片的，而downloadOnly(Y target)是用于在主线程中下载图片的。


那么我们先来看downloadOnly(int width, int height)的用法。当调用了downloadOnly(int width, int height)方法后会立即返回一个FutureTarget对象，然后Glide会在后台开始下载图片文件。接下来我们调用FutureTarget的get()方法就可以去获取下载好的图片文件了，如果此时图片还没有下载完，那么get()方法就会阻塞住，一直等到图片下载完成才会有值返回。

下面我们通过一个例子来演示一下吧，代码如下所示：

    public void downloadImage(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://cn.bing.com/az/hprichbg/rb/TOAD_ZH-CN7336795473_1920x1080.jpg";
                    final Context context = getApplicationContext();
                    FutureTarget<File> target = Glide.with(context)
                                                     .load(url)
                                                     .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                    final File imageFile = target.get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, imageFile.getPath(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


