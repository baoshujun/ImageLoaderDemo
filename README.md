Glide介绍
>Glide是一款由Bump Technologies开发的图片加载框架，目前，Glide最新的稳定版本是3.7.0，虽然4.0已经推出RC版了，
但是暂时问题还比较多。因此，这里主要介绍Glide 3.7.0版本的用法，这个版本的Glide相当成熟和稳定


要想使用Glide，首先需要将这个库引入到我们的项目当中。新建一个项目，然后在app/build.gradle文件当中添加如下依赖：

    dependencies {
        implementation 'com.github.bumptech.glide:glide:3.7.0
    }

另外，Glide中需要用到网络功能，因此你还得在AndroidManifest.xml中声明一下网络权限才行：

    <uses-permission android:name="android.permission.INTERNET" />

加载图片

    Glide.with(this).load(url).into(imageView);

下面我们就来详细解析一下这行代码。

首先，调用Glide.with()方法用于创建一个加载图片的实例。with()方法可以接收Context、Activity或者Fragment类型的参数。也就是说我们选择的范围非常广，不管是在Activity还是Fragment中调用with()方法，都可以直接传this。那如果调用的地方既不在Activity中也不在Fragment中呢？也没关系，我们可以获取当前应用程序的ApplicationContext，传入到with()方法当中。注意with()方法中传入的实例会决定Glide加载图片的生命周期，如果传入的是Activity或者Fragment的实例，那么当这个Activity或Fragment被销毁的时候，图片加载也会停止。如果传入的是ApplicationContext，那么只有当应用程序被杀掉的时候，图片加载才会停止。

接下来看一下load()方法，这个方法用于指定待加载的图片资源。Glide支持加载各种各样的图片资源，包括网络图片、本地图片、应用资源、二进制流、Uri对象等等。因此load()方法也有很多个方法重载，除了我们刚才使用的加载一个字符串网址之外，你还可以这样使用load()方法：

    // 加载本地图片
    File file = new File(getExternalCacheDir() + "/image.jpg");
    Glide.with(this).load(file).into(imageView);

    // 加载应用资源
    int resource = R.drawable.image;
    Glide.with(this).load(resource).into(imageView);

    // 加载二进制流
    byte[] image = getImageBytes();
    Glide.with(this).load(image).into(imageView);

    // 加载Uri对象
    Uri imageUri = getImageUri();
    Glide.with(this).load(imageUri).into(imageView);

最后看一下into()方法，这个方法就很简单了，我们希望让图片显示在哪个ImageView上，把这个ImageView的实例传进去就可以了

那么回顾一下Glide最基本的使用方式，其实就是关键的三步走：先with()，再load()，最后into()。熟记这三步，你就已经入门Glide了。

占位图

顾名思义，占位图就是指在图片的加载过程中，我们先显示一张临时的图片，等图片加载出来了再替换成要加载的图片

下面我们就来学习一下Glide占位图功能的使用方法，首先我事先准备好了一张loading.jpg图片，用来作为占位图显示。然后修改Glide加载部分的代码，如下所示：

    Glide.with(this)
         .load(url)
         .placeholder(R.drawable.loading)
         .into(imageView);


没错，就是这么简单。我们只是在刚才的三步走之间插入了一个placeholder()方法，然后将占位图片的资源id传入到这个方法中即可。另外，这个占位图的用法其实也演示了Glide当中绝大多数API的用法，其实就是在load()和into()方法之间串接任意想添加的功能就可以了

禁用缓存功能
    Glide.with(this)
         .load(url)
         .placeholder(R.drawable.loading)
         .diskCacheStrategy(DiskCacheStrategy.NONE)
         .into(imageView);

可以看到，这里串接了一个diskCacheStrategy()方法，并传入DiskCacheStrategy.NONE参数，这样就可以禁用掉Glide的缓存功能。

异常占位图
异常占位图就是指，如果因为某些异常情况导致图片加载失败，比如说手机网络信号不好，这个时候就显示这张异常占位图

    Glide.with(this)
         .load(url)
         .placeholder(R.drawable.loading)
         .error(R.drawable.error)
         .diskCacheStrategy(DiskCacheStrategy.NONE)
         .into(imageView);


Glide加载GIF图片

Glide加载GIF图并不需要编写什么额外的代码，Glide内部会自动判断图片格式，Glide加载GIF图并不需要编写什么额外的代码，Glide内部会自动判断图片格式

但是如果我想指定图片的格式该怎么办呢？就比如说，我希望加载的这张图必须是一张静态图片，我不需要Glide自动帮我判断它到底是静图还是GIF图。

想实现这个功能仍然非常简单，我们只需要再串接一个新的方法就可以了，如下所示：

    Glide.with(this)
         .load(url)
         .asBitmap()
         .placeholder(R.drawable.loading)
         .error(R.drawable.error)
         .diskCacheStrategy(DiskCacheStrategy.NONE)
         .into(imageView);

可以看到，这里在load()方法的后面加入了一个asBitmap()方法，这个方法的意思就是说这里只允许加载静态图片，不需要Glide去帮我们自动进行图片格式的判断了


那么类似地，既然我们能强制指定加载静态图片，就也能强制指定加载动态图片。比如说我们想要实现必须加载动态图片的功能，就可以这样写：

    Glide.with(this)
         .load(url)
         .asGif()
         .placeholder(R.drawable.loading)
         .error(R.drawable.error)
         .diskCacheStrategy(DiskCacheStrategy.NONE)
         .into(imageView);

这里调用了asGif()方法替代了asBitmap()方法

指定图片大小

    Glide.with(this)
     .load(url)
     .placeholder(R.drawable.loading)
     .error(R.drawable.error)
     .diskCacheStrategy(DiskCacheStrategy.NONE)
     .override(100, 100)
     .into(imageView);

仍然非常简单，这里使用override()方法指定了一个图片的尺寸，也就是说，Glide现在只会将图片加载成100*100像素的尺寸，而不会管你的ImageView的大小是多少了