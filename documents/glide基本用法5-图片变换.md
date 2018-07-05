如果图片大小为wrap_content，因为ImageView默认的scaleType是FIT_CENTER，Glide默认会执行图片变换，填充父布局

Glide给我们提供了专门的API来添加和取消图片变换，想要解决这个问题只需要使用如下代码即可：

    Glide.with(this)
         .load(url)
         .dontTransform()
         .into(imageView);


但是使用dontTransform()方法存在着一个问题，就是调用这个方法之后，所有的图片变换操作就全部失效了，
那如果我有一些图片变换操作是必须要执行的该怎么办呢？不用担心，总归是有办法的，
这种情况下我们只需要借助override()方法强制将图片尺寸指定成原始大小就可以了，代码如下所示：

    Glide.with(this)
         .load(url)
         .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
         .into(imageView);

通过override()方法将图片的宽和高都指定成Target.SIZE_ORIGINAL，问题同样被解决了。程序的最终运行结果和上图是完全一样的

由此我们可以看出，之所以会出现这个问题，和Glide的图片变换功能是撇不开关系的。那么也是通过这个问题，
我们对Glide的图片变换有了一个最基本的认识

图片变换的基本用法

顾名思义，图片变换的意思就是说，Glide从加载了原始图片到最终展示给用户之前，又进行了一些变换处理，
从而能够实现一些更加丰富的图片效果，如图片圆角化、圆形化、模糊化等等。

添加图片变换的用法非常简单，我们只需要调用transform()方法，并将想要执行的图片变换操作作为参数传入transform()方法即可，如下所示：

    Glide.with(this)
         .load(url)
         .transform(...)
         .into(imageView);

至于具体要进行什么样的图片变换操作，这个通常都是需要我们自己来写的。不过Glide已经内置了两种图片变换操作，我们可以直接拿来使用，一个是CenterCrop，一个是FitCenter。

但这两种内置的图片变换操作其实都不需要使用transform()方法，Glide为了方便我们使用直接提供了现成的API：

    Glide.with(this)
         .load(url)
         .centerCrop()
         .into(imageView);

    Glide.with(this)
         .load(url)
         .fitCenter()
         .into(imageView);

当然，centerCrop()和fitCenter()方法其实也只是对transform()方法进行了一层封装而已，它们背后的源码仍然还是借助transform()方法来实现的

那么这两种内置的图片变换操作到底能实现什么样的效果呢？

FitCenter的效果就是会将图片按照原始的长宽比充满全屏

CenterCrop 对原图的中心区域进行裁剪，裁剪的大小可以通过override(500, 500) 进行设置

详情：https://blog.csdn.net/guolin_blog/article/details/71524668