package qz.bigdata.crawler.tool;

import qz.bigdata.crawler.monitor.*;

/**
 * Created by fys on 2015/4/17.
 */
public class CrawlerController {
    public static void main(String[] args){
        if(args.length != 3) {
            System.out.println("[action] [project_name] [project_version]");
            System.out.println("[action] should be suspend | resume | stop.");
            return;
        }
        else{
            String command = args[0];
            String project = args[1];
            String version = args[2];
            IStatusController controller = new RedisCrawlerUtil();
            if(command.equals("suspend"))
            {
                controller.notifyToSuspend(project, version);
            }
            else if(command.equals("resume")){
                controller.notifyToResume(project, version);
            }
            else if(command.equals("stop")) {
                controller.notifyToStop(project, version);
            }
        }
    }
}
