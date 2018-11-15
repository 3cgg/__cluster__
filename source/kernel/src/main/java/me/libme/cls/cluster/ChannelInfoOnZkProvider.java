package me.libme.cls.cluster;

import me.libme.fn.netty.client.DynamicClientChannelExecutor;
import me.libme.fn.netty.client.SimpleChannelExecutor;
import me.libme.kernel._c.json.JJSON;
import me.libme.kernel._c.util.JStringUtils;
import me.libme.module.zookeeper.ZooKeeperConnector;
import me.libme.module.zookeeper.fn.ls.NodeMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by J on 2018/1/30.
 */
public class ChannelInfoOnZkProvider implements DynamicClientChannelExecutor.SimpleChannelExecutorProvider {

    private static final Logger LOGGER= LoggerFactory.getLogger(ChannelInfoOnZkProvider.class);


    private final String path;

    private final ZooKeeperConnector.ZookeeperExecutor executor;

    private final AtomicReference<SimpleChannelExecutor> channelExecutor=new AtomicReference<>();

    private final Lock lock=new ReentrantLock();

    private AtomicReference<Boolean> openLock=new AtomicReference<>(false);

    public ChannelInfoOnZkProvider(String path,ZooKeeperConnector.ZookeeperExecutor executor) {
        ExecutorService executorService=Executors.newSingleThreadExecutor(r -> new Thread(r,"Leader-Channel-Watcher"));
        this.path = path;
        this.executor=executor;
        connect(JJSON.get().parse(JStringUtils.utf8(this.executor.getPath(this.path)),NodeMeta.class));
        this.executor.watchPath(this.path,zooNode -> connect(JJSON.get().parse(JStringUtils.utf8(zooNode.getData()),NodeMeta.class)), executorService);

    }

    private void connect(NodeMeta nodeMeta) {
        try{
            lock.lock();
            openLock.set(true); // !important

            String ip=nodeMeta.getIp();
            ClusterConfig clusterConfig=ClusterInfo.defaultConfig();
            int serverPort= clusterConfig.getMaster().getPort();

            SimpleChannelExecutor simpleChannelExecutor=new SimpleChannelExecutor(ip,serverPort);
            channelExecutor.set(simpleChannelExecutor);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            System.exit(-1);
        }finally {
            openLock.set(false); // !important
            lock.unlock();

        }
    }


    @Override
    public SimpleChannelExecutor provider() {

        if(openLock.get()){
            try{
                lock.lock();
            }finally {
                lock.unlock();
            }
        }
        return channelExecutor.get();
    }



}
