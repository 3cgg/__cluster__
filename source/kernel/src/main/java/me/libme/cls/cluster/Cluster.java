package me.libme.cls.cluster;

import me.libme.fn.netty.server.fn._dispatch.PathListenerInitializeQueue;
import me.libme.module.zookeeper.ZooKeeperConnector;
import me.libme.module.zookeeper.fn.ls.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scalalg.me.libme.cls.BasicClsRuntime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J on 2018/1/28.
 */
public class Cluster {

    private static final Logger LOGGER= LoggerFactory.getLogger(Cluster.class);

    private List<OpenResource> openResources=new ArrayList<>();

    private List<CloseResource> closeResources=new ArrayList<>();

    private final String[] args;

    public Cluster(String[] args) {
        this.args = args;
    }


    /**
     * open resource in the sequence of adding element
     * @param openResource
     * @return
     */
    public Cluster addOpenResource(OpenResource openResource){
        openResources.add(openResource);
        return this;
    }

    /**
     * close resource in the sequence of adding element
     * @param closeResource
     * @return
     */
    public Cluster addCloseResource(CloseResource closeResource){
        closeResources.add(closeResource);
        return this;
    }

    public void start() throws Exception{
        LOGGER.info("start cluster...");
        BasicClsRuntime basicClsRuntime = BasicClsRuntime.builder().args(args).getOrCreate();

        //zookeeper
        ZooKeeperConnector.ZookeeperExecutor executor= basicClsRuntime.zookeeperExecutor().get();

        LeaderConfig conf=new LeaderConfig();
        conf.setBasePath(ClusterZkPaths.BASE_PATH);
        conf.setName("Cluster");
        basicClsRuntime.allParam().forEach((key, value)->conf.put(key,value));

        // leader register
        LeaderNodeRegister leaderNodeRegister=new LeaderNodeRegister("Leader Register",ClusterZkPaths.LEADER_INFO_PATH,executor);

        //start netty server
        NettyServer nettyServer=new NettyServer();


        NodeLeader.LeaderBuilder builder = NodeLeader.builder()
                .conf(conf)
                .executor(executor)
                .addOpenResource(leaderNodeRegister)
                .addOpenResource(nettyServer)
                .addOpenResource(new OpenResource() {
                    @Override
                    public void open(NodeLeader nodeLeader) throws Exception {
                        PathListenerInitializeQueue.get().allInitialize();
                    }

                    @Override
                    public String name() {
                        return "Initialize Path Listener Object...";
                    }
                })
//                .addCloseResource(leaderNodeRegister)  //comment,avoid removing the path another node register itself
                .addCloseResource(nettyServer);
        openResources.forEach(openResource -> builder.addOpenResource(openResource));
        closeResources.forEach(closeResource -> builder.addCloseResource(closeResource));
        NodeLeader nodeLeader=builder.build();
        nodeLeader.start();

        LOGGER.info("start cluster OK!");
    }

    public void shutdown() throws Exception{



    }





}
