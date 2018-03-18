package scalalg.me.libme.cls

import me.libme.kernel._c.json.JJSON
import me.libme.kernel._c.util.{JIOUtils, JStringUtils}

/**
  * Created by J on 2018/1/21.
  */
object ClsConfig {


  var config:java.util.Map[String,AnyRef]=null

  {
    config = JJSON.get.parse(JStringUtils.utf8(JIOUtils.getBytes(Thread.currentThread.getContextClassLoader.getResourceAsStream("cluster-conf.json"))));
  }

  def get(key:String):AnyRef={

    config.get(key)

  }

  def backend():java.util.Map[String,AnyRef]={
    config
  }


}
