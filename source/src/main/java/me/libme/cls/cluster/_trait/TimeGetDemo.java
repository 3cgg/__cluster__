package me.libme.cls.cluster._trait;

import me.libme.fn.netty.server.HttpRequest;
import me.libme.fn.netty.server.fn._dispatch.PathListenerInitializeQueue;
import me.libme.kernel._c.util.JDateUtils;

import java.util.Date;

/**
 * Created by J on 2018/1/28.
 */
public class TimeGetDemo implements TimeGet {



    public TimeGetDemo() {

        PathListenerInitializeQueue.get().offer(()->{
            System.out.println("set property lazily...");

        });

    }

    @Override
    public String time(String name, HttpRequest httpRequest) {
        return name + " - " + JDateUtils.formatWithSeconds(new Date());
    }


}
