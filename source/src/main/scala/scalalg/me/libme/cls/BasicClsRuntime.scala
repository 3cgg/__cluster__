package scalalg.me.libme.cls

import java.util
import java.util.concurrent.atomic.AtomicReference

import me.libme.kernel._c.util.CliParams
import me.libme.module.kafka.ProducerConnector
import me.libme.module.zookeeper.ZooKeeperConnector

/**
  * Created by J on 2018/1/28.
  */
 class BasicClsRuntime private{


  val zookeeperExecutor=new AtomicReference[ZooKeeperConnector#ZookeeperExecutor]

  val producerExecutor=new AtomicReference[ProducerConnector#ProducerExecutor[String,String]]

  private val cliParams=new AtomicReference[CliParams]

  def isCluster():Boolean={
    cliParams.get().getBoolean("--cls.cluster")
  }

  def isKafka():Boolean={
    cliParams.get().getBoolean("--cls.kafka")
  }

  def serverPort():Int={
    cliParams.get().getInt("--cls.master.netty.port")
  }

  def server():Int={
    cliParams.get().getInt("--cls.master.netty.host")
  }

  def allParam():util.Map[String,Object]={
    util.Collections.unmodifiableMap(cliParams.get().toMap)
  }



}
object BasicClsRuntime{


  private val defaultSession = new AtomicReference[BasicClsRuntime]



  def builder():Builder=new Builder

  class Builder{

    private[this] var args:Seq[String]=_

    def args(args:Seq[String]):Builder={
      this.args=args;
      this
    }

    def args(args:Array[String]):Builder={
      this.args=args;
      this
    }

    def getOrCreate():BasicClsRuntime= synchronized {

      if(defaultSession.get()!=null){
        return defaultSession.get();
      }


      val config: util.Map[String, AnyRef] = ClsConfig.backend

      var cliParams: CliParams = new CliParams(args.toArray)
      import scala.collection.JavaConversions._
      for (entry <- config.entrySet) {
        if (!cliParams.contains(entry.getKey)) cliParams = cliParams.append(entry.getKey, entry.getValue)
      }

      val basicClsRuntime=new BasicClsRuntime
      basicClsRuntime.cliParams.set(cliParams)

      //zookeeper
      val zookeeperExecutor = Util.zookeeper(cliParams)
      basicClsRuntime.zookeeperExecutor.set(zookeeperExecutor)

      //kafka
      if(basicClsRuntime.isKafka()){
        val producerExecutor = Util.producerExecutor(cliParams)
        basicClsRuntime.producerExecutor.set(producerExecutor)
      }

      defaultSession.set(basicClsRuntime)

      return basicClsRuntime
    }



  }

}