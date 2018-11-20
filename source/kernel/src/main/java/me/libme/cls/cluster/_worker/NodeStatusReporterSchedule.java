package me.libme.cls.cluster._worker;

import me.libme.cls.cluster.*;
import me.libme.cls.cluster._trait.reporter.NodeStatusReporter;
import me.libme.kernel._c.util.NetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by J on 2018/11/15.
 */
public class NodeStatusReporterSchedule implements Action {

    private static final Logger LOGGER= LoggerFactory.getLogger(NodeStatusReporterSchedule.class);

    private ScheduledExecutorService scheduledExecutorService= null;

    private ClusterConfig clusterConfig= null;

    private NodeStatusReporter nodeStatusReporter=null;

    @Override
    public void prepare() throws Exception {
        scheduledExecutorService= Executors.newScheduledThreadPool(1);
        clusterConfig= ClusterInfo.defaultConfig();
        nodeStatusReporter= PathListenerClientFactory.factory(NodeStatusReporter.class,NodeStatusReporter.PATH);

    }

    @Override
    public void start() throws Exception {

        scheduledExecutorService.scheduleAtFixedRate(()->{
            try {
                InetAddress inetAddress= NetUtil.getLocalAddress();
                NodeSnapshot nodeSnapshot=new NodeSnapshot();
                nodeSnapshot.setNodeName(clusterConfig.getWorker().getName());
                nodeSnapshot.setIp(inetAddress.getHostAddress());
                nodeSnapshot.setHostName(inetAddress.getHostName());
                nodeStatusReporter.report(nodeSnapshot,null);
            }catch (Exception e){
                LOGGER.error(e.getMessage(),e);
            }

        },10,30, TimeUnit.SECONDS);
    }

    @Override
    public void shutdown() throws Exception {
        scheduledExecutorService.shutdown();
    }



}
