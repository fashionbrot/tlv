package com.github.fashionbrot;

import com.github.fashionbrot.tlv.GzipUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @author fashionbrot
 */
public class GzipTest {


    @Test
    public void test() throws IOException {
        String abc = "soflkdsfdls;jflkdjsljflds收到付你好啊你好啊你好啊，，，你好啊你好啊你好啊你好啊你好啊你好啊你好" +
                "啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊" +
                "你好啊你好啊你好啊了款建设大街范德萨啦久啊离开家发多少是的范德萨范德萨dsfdsafdsa1111111111f撒娇法律监督撒" +
                "辣椒发啦电视剧发来的撒娇；撒范德萨发生范德萨打撒菲利克斯较大附件阿萨达萨罗咖啡机达萨罗激发打撒放大算啦撒了发动" +
                "机撒了范德萨发打撒进啦打撒付了款拒收到付量较大萨法到拉萨撒酒疯啦实际付款洒进来发沙发的山卡拉人哦iu企鹅无偶体温" +
                "枪euro饿我去如我饿111111111111111111122222222222222222224444444444444444444444444444444444" +
                "哦iu饿我去肉IE我去扑热哦武器扑热武器哦日配我去让娃都是佛i啊上佛菩萨范德萨破啊u法搜反扒撒u发啥哦对四方坡iU盾" +
                "soflkdsfdls;jflkdjsljflds收到付你好啊你好啊你好啊，，，你好啊你好啊你好啊你好啊你好啊你好啊你好" +
                "啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊" +
                "你好啊你好啊你好啊了款建设大街范德萨啦久啊离开家发多少是的范德萨范德萨dsfdsafdsa1111111111f撒娇法律监督撒" +
                "辣椒发啦电视剧发来的撒娇；撒范德萨发生范德萨打撒菲利克斯较大附件阿萨达萨罗咖啡机达萨罗激发打撒放大算啦撒了发动" +
                "机撒了范德萨发打撒进啦打撒付了款拒收到付量较大萨法到拉萨撒酒疯啦实际付款洒进来发沙发的山卡拉人哦iu企鹅无偶体温" +
                "枪euro饿我去如我饿111111111111111111122222222222222222224444444444444444444444444444444444" +
                "哦iu饿我去肉IE我去扑热哦武器扑热武器哦日配我去让娃都是佛i啊上佛菩萨范德萨破啊u法搜反扒撒u发啥哦对四方坡iU盾";
        System.out.println(abc.getBytes().length);
        byte[] compress = GzipUtil.compress(abc);
        System.out.println(compress.length);

        String decompress = GzipUtil.decompress(compress);
        System.out.println(decompress);
        Assert.assertEquals(abc,decompress);
    }


    @Test
    public void test1(){

    }



}
