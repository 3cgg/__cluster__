package me.libme.cls.cluster._worker;

import me.libme.cls.cluster.Action;
import me.libme.cls.cluster.ClusterConfig;
import me.libme.cls.cluster.ClusterInfo;
import me.libme.kernel._c.json.JJSON;
import me.libme.module.zookeeper.ZKExecutor;
import me.libme.module.zookeeper.ZooKeeperConnector;
import org.apache.zookeeper.CreateMode;

/**
 * Created by J on 2018/11/19.
 */
public class NodeReporterInZk implements Action {

    private ZooKeeperConnector.ZookeeperExecutor executor;

    private ClusterConfig clusterConfig= null;

    private String indicatePath=null;

    @Override
    public void prepare() throws Exception {
        executor=ZKExecutor.defaultExecutor();
        clusterConfig= ClusterInfo.defaultConfig();
        indicatePath="/worker-node/"+clusterConfig.getWorker().getName();
    }

    @Override
    public void start() throws Exception {
        if(executor.exists(indicatePath)){
            executor.deletePath(indicatePath);
        }
        executor.createPath(indicatePath,
                JJSON.get().format(clusterConfig.getWorker())
                ,CreateMode.EPHEMERAL);
    }

    @Override
    public void shutdown() throws Exception {
        executor.deletePath(indicatePath);
    }


}
