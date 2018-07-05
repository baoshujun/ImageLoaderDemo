glide 定义监听

如果我们要进行自定义的话，通常只需要在两种Target的基础上去自定义就可以了，一种是SimpleTarget，
一种是ViewTarget

那么下面我们来看一下SimpleTarget的用法示例吧，其实非常简单：

    SimpleTarget<GlideDrawable> simpleTarget = new SimpleTarget<GlideDrawable>() {
        @Override
        public void onResourceReady(GlideDrawable resource, GlideAnimation glideAnimation) {
            imageView.setImageDrawable(resource);
        }
    };

    public void loadImage(View view) {
        String url = "http://cn.bing.com/az/hprichbg/rb/TOAD_ZH-CN7336795473_1920x1080.jpg";
        Glide.with(this)
             .load(url)
             .into(simpleTarget);
    }

这里我们创建了一个SimpleTarget的实例，并且指定它的泛型是GlideDrawable，然后重写了onResourceReady()方法。
在onResourceReady()方法中，我们就可以获取到Glide加载出来的图片对象了，也就是方法参数中传过来的
GlideDrawable对象。有了这个对象之后你可以使用它进行任意的逻辑操作，这里我只是简单地把它显示到了ImageView上。

当然，SimpleTarget中的泛型并不一定只能是GlideDrawable，如果你能确定你正在加载的是一张静态图而不是GIF图的话，
我们还能直接拿到这张图的Bitmap对象，如下所示：

    SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
            imageView.setImageBitmap(resource);
        }
    };

    public void loadImage(View view) {
        String url = "http://cn.bing.com/az/hprichbg/rb/TOAD_ZH-CN7336795473_1920x1080.jpg";
        Glide.with(this)
             .load(url)
             .asBitmap()
             .into(simpleTarget);
    }

可以看到，这里我们将SimpleTarget的泛型指定成Bitmap，然后在加载图片的时候调用了asBitmap()方法强制指定这是一张静态图，
这样就能在onResourceReady()方法中获取到这张图的Bitmap对象了。

接下来我们学习一下ViewTarget的用法

事实上，从刚才的继承结构图上就能看出，Glide在内部自动帮我们创建的GlideDrawableImageViewTarget就是ViewTarget的子类。
只不过GlideDrawableImageViewTarget被限定只能作用在ImageView上，而ViewTarget的功能更加广泛，它可以作用在任意的View上

这里我们还是通过一个例子来演示一下吧，比如我创建了一个自定义布局MyLayout，如下所示：

    public class MyLayout extends LinearLayout {

        private ViewTarget<MyLayout, GlideDrawable> viewTarget;

        public MyLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            viewTarget = new ViewTarget<MyLayout, GlideDrawable>(this) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation glideAnimation) {
                    MyLayout myLayout = getView();
                    myLayout.setImageAsBackground(resource);
                }
            };
        }

        public ViewTarget<MyLayout, GlideDrawable> getTarget() {
            return viewTarget;
        }

        public void setImageAsBackground(GlideDrawable resource) {
            setBackground(resource);
        }

    }

在MyLayout的构造函数中，我们创建了一个ViewTarget的实例，并将Mylayout当前的实例this传了进去。ViewTarget中需要指定两个泛型，
一个是View的类型，一个图片的类型（GlideDrawable或Bitmap）。然后在onResourceReady()方法中，我们就可以通过getView()方法获
取到MyLayout的实例，并调用它的任意接口了。比如说这里我们调用了setImageAsBackground()方法来将加载出来的图片作为MyLayout布局
的背景图

接下来看一下怎么使用这个Target吧，由于MyLayout中已经提供了getTarget()接口，我们只需要在加载图片的地方这样写就可以了：

    public class MainActivity extends AppCompatActivity {

        MyLayout myLayout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            myLayout = (MyLayout) findViewById(R.id.background);
        }

        public void loadImage(View view) {
            String url = "http://cn.bing.com/az/hprichbg/rb/TOAD_ZH-CN7336795473_1920x1080.jpg";
            Glide.with(this)
                 .load(url)
                 .into(myLayout.getTarget());
        }

    }


listener()方法
首先来看下listener()方法的基本用法吧，不同于刚才几个方法都是要替换into()方法的，listener()是结合into()方法一起使用的，
当然也可以结合preload()方法一起使用。最基本的用法如下所示：

    public void loadImage(View view) {
        String url = "http://cn.bing.com/az/hprichbg/rb/TOAD_ZH-CN7336795473_1920x1080.jpg";
        Glide.with(this)
                .load(url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                        boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                        Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }


这里我们在into()方法之前串接了一个listener()方法，然后实现了一个RequestListener的实例。其中RequestListener需要实现两个方法，
一个onResourceReady()方法，一个onException()方法。从方法名上就可以看出来了，当图片加载完成的时候就会回调onResourceReady()方法，
而当图片加载失败的时候就会回调onException()方法，onException()方法中会将失败的Exception参数传进来，这样我们就可以定位具体失败的原因了。


没错，listener()方法就是这么简单。不过还有一点需要处理，onResourceReady()方法和onException()方法都有一个布尔值的返回值，
返回false就表示这个事件没有被处理，还会继续向下传递，返回true就表示这个事件已经被处理掉了，从而不会再继续向下传递。
举个简单点的例子，如果我们在RequestListener的onResourceReady()方法中返回了true，那么就不会再回调Target的onResourceReady()方法了。