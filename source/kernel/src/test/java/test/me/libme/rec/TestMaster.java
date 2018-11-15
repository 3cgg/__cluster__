package test.me.libme.rec;

import me.libme.cls.cluster.Master;

/**
 * Created by J on 2018/11/14.
 */
public class TestMaster {


    public static void main(String[] args) throws Exception {

        Master.builder().build().start();


    }


}
