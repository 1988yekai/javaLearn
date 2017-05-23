package qz.bigdata.crawler.monitor;

/**
 * Created by fys on 2015/4/16.
 */
public interface IStatusController {
    boolean notifyToResume(String project, String version);
    boolean notifyToSuspend(String project, String version);
    boolean notifyToStop(String project, String version);
}
