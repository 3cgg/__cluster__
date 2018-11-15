package me.libme.cls.cluster._trait.reporter;

import me.libme.cls.cluster.NodeSnapshot;
import me.libme.fn.netty.server.HttpRequest;
import me.libme.fn.netty.server.fn._dispatch.PathListener;

/**
 * Created by J on 2018/11/15.
 */
public interface NodeStatusReporter extends PathListener {

    String PATH="/node/status/report";

    String report(NodeSnapshot nodeSnapshot, HttpRequest httpRequest);


}
