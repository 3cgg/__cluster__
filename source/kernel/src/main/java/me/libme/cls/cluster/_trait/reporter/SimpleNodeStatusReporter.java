package me.libme.cls.cluster._trait.reporter;

import me.libme.cls.cluster.NodeSnapshot;
import me.libme.fn.netty.server.HttpRequest;
import me.libme.kernel._c.cache.JMapCacheService;

/**
 * Created by J on 2018/11/15.
 */
public class SimpleNodeStatusReporter implements NodeStatusReporter {

    private JMapCacheService<String ,NodeSnapshot> nodeSnapshotMap=new JMapCacheService<>();

    @Override
    public String report(NodeSnapshot nodeSnapshot, HttpRequest httpRequest) {
        nodeSnapshotMap.put(nodeSnapshot.getNodeName(),nodeSnapshot);
        return "True";
    }



}
