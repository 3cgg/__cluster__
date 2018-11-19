package me.libme.cls.cluster;

import me.libme.cls.cluster._worker.NodeReporterInZk;
import me.libme.cls.cluster._worker.NodeStatusReporterSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by J on 2018/11/15.
 */
public class SimpleWorker implements Worker {

    private static final Logger LOGGER= LoggerFactory.getLogger(SimpleWorker.class);

    private NodeStatusReporterSchedule nodeStatusReporterSchedule=new NodeStatusReporterSchedule();

    private NodeReporterInZk nodeReporterInZk=new NodeReporterInZk();

    private List<Action> actions=new ArrayList<>();
    {
        actions.add(nodeStatusReporterSchedule);
        actions.add(nodeReporterInZk);
    }

    private AtomicBoolean prepared=new AtomicBoolean(false);

    private SimpleWorker() {
    }

    @Override
    public void prepare() throws Exception {
        LOGGER.info("prepare worker...");
        if(prepared.compareAndSet(false,true)) {
            for(Action action:actions){
                LOGGER.info("do prepare ... on "+action.getClass().getName());
                action.prepare();
            }
        }
    }

    @Override
    public void start() throws Exception {
        LOGGER.info("start worker...");
        prepare();
        for(Action action:actions){
            LOGGER.info("do start ... on "+action.getClass().getName());
            action.start();
        }
    }

    @Override
    public void shutdown() throws Exception {
        LOGGER.info("shutdown worker...");
        for(Action action:actions){
            LOGGER.info("do shutdown ... on "+action.getClass().getName());
            action.shutdown();
        }
    }

    public static SimpleWorkerBuilder builder(){
        return new SimpleWorkerBuilder();
    }


    public static class SimpleWorkerBuilder{


        private List<Action> actions=new ArrayList<>();

        public SimpleWorkerBuilder addAction(Action action){
            actions.add(action);
            return this;
        }

        public SimpleWorker build(){
            SimpleWorker simpleWorker=new SimpleWorker();
            actions.forEach(action -> simpleWorker.actions.add(action));
            return simpleWorker;
        }

    }


}
