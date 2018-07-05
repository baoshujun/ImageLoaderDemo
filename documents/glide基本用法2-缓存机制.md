禁用内存缓存功能

    Glide.with(this)
         .load(url)
         .skipMemoryCache(true)
         .into(imageView);

可以看到，只需要调用skipMemoryCache()方法并传入true，就表示禁用掉Glide的内存缓存功能

禁止Glide对图片进行硬盘

    Glide.with(this)
         .load(url)
         .diskCacheStrategy(DiskCacheStrategy.NONE)
         .into(imageView);

调用diskCacheStrategy()方法并传入DiskCacheStrategy.NONE，就可以禁用掉Glide的硬盘缓存功能了

这个diskCacheStrategy()方法基本上就是Glide硬盘缓存功能的一切，它可以接收四种参数：

* DiskCacheStrategy.NONE： 表示不缓存任何内容。
* DiskCacheStrategy.SOURCE： 表示只缓存原始图片。
* DiskCacheStrategy.RESULT： 表示只缓存转换过后的图片（默认选项）。
* DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。

一个概念大家需要了解，就是当我们使用Glide去加载一张图片的时候，Glide默认并不会将原始图片展示出来，
而是会对图片进行压缩和转换,总之就是经过种种一系列操作之后得到的图片，就叫转换过后的图片。
而Glide默认情况下在硬盘缓存的就是转换过后的图片，我们通过调用diskCacheStrategy()方法则可以改变这一默认行为。


高级技巧

比如之前有一位群里的朋友就跟我说过，他们项目的图片资源都是存放在七牛云上面的，而七牛云为了对图片资源进行保护，
会在图片url地址的基础之上再加上一个token参数。也就是说，一张图片的url地址可能会是如下格式

    http://url.com/image.jpg?token=d9caa6e02c990b0a


而使用Glide加载这张图片的话，也就会使用这个url地址来组成缓存Key。

但是接下来问题就来了，token作为一个验证身份的参数并不是一成不变的，很有可能时时刻刻都在变化。而如果token变了，那么图片的url也就跟着变了，图片url变了，缓存Key也就跟着变了。结果就造成了，明明是同一张图片，就因为token不断在改变，导致Glide的缓存功能完全失效了。

这其实是个挺棘手的问题，而且我相信绝对不仅仅是七牛云这一个个例，大家在使用Glide的时候很有可能都会遇到这个问题。

解决办法

创建一个MyGlideUrl继承自GlideUrl，代码如下所示：

    public class MyGlideUrl extends GlideUrl {

        private String mUrl;

        public MyGlideUrl(String url) {
            super(url);
            mUrl = url;
        }

        @Override
        public String getCacheKey() {
            return mUrl.replace(findTokenParam(), "");
        }

        private String findTokenParam() {
            String tokenParam = "";
            int tokenKeyIndex = mUrl.indexOf("?token=") >= 0 ? mUrl.indexOf("?token=") : mUrl.indexOf("&token=");
            if (tokenKeyIndex != -1) {
                int nextAndIndex = mUrl.indexOf("&", tokenKeyIndex + 1);
                if (nextAndIndex != -1) {
                    tokenParam = mUrl.substring(tokenKeyIndex + 1, nextAndIndex + 1);
                } else {
                    tokenParam = mUrl.substring(tokenKeyIndex);
                }
            }
            return tokenParam;
        }

    }

 可以看到，这里我们重写了getCacheKey()方法，在里面加入了一段逻辑用于将图片url地址中token参数的这一部分移除掉。这样getCacheKey()方法得到的就是一个没有token参数的url地址，从而不管token怎么变化，最终Glide的缓存Key都是固定不变的了。

 当然，定义好了MyGlideUrl，我们还得使用它才行，将加载图片的代码改成如下方式即可：

    Glide.with(this)
         .load(new MyGlideUrl(url))
         .into(imageView);

 也就是说，我们需要在load()方法中传入这个自定义的MyGlideUrl对象，而不能再像之前那样直接传入url字符串了。不然的话Glide在内部还是会使用原始的GlideUrl类，而不是我们自定义的MyGlideUrl类。

 这样我们就将这个棘手的缓存问题给解决掉了