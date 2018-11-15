package me.libme.cls.cluster;

import me.libme.fn.netty.server.fn._dispatch.PathListenerInitializeQueue;
import me.libme.module.zookeeper.ZKExecutor;
import me.libme.module.zookeeper.ZooKeeperConnector;
import me.libme.module.zookeeper.fn.ls.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J on 2018/1/28.
 */
public class SimpleCluster implements Cluster {

    private static final Logger LOGGER= LoggerFactory.getLogger(SimpleCluster.class);

    private List<OpenResource> openResources=new ArrayList<>();

    private List<CloseResource> closeResources=new ArrayList<>();

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

    @Override
    public void start() throws Exception{
        LOGGER.info("start cluster...");
        ClusterConfig clusterConfig=ClusterInfo.defaultConfig();
        //zookeeper
        ZooKeeperConnector.ZookeeperExecutor executor= ZKExecutor.defaultExecutor();

        LeaderConfig conf=new LeaderConfig();
        conf.setBasePath(ClusterZkPaths.MASTER_PATH);
        conf.setName(clusterConfig.getName());

        clusterConfig.getProperties().forEach((key, value)->conf.put(key,value));
        conf.put(NettyServer.HOST,clusterConfig.getMaster().getHost());
        conf.put(NettyServer.PORT,clusterConfig.getMaster().getNetty().getPort());

        // leader register
        Node node=new Node();
        node.setIp(clusterConfig.getMaster().getHost());
        node.setHostName(clusterConfig.getMaster().getHostName());
        node.setName(clusterConfig.getMaster().getName());

        LeaderNodeRegister leaderNodeRegister=new LeaderNodeRegister("Leader Register",ClusterZkPaths.LEADER_INFO_PATH,executor,node);

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

    @Override
    public void shutdown() throws Exception{



    }





}
