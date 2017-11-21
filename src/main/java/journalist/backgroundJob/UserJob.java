package journalist.backgroundJob;

import journalist.actions.UserDao;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionFactory;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.scheduler.BackgroundJob;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.NodeIterator;
import javax.jcr.query.Query;

public class UserJob extends BackgroundJob {

    private static final Logger LOG = LoggerFactory.getLogger(UserJob.class);

    @Override
    public void executeJahiaJob(JobExecutionContext jobExecutionContext) throws Exception {
        LOG.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!inside executeJahiaJob method!");
        JCRSessionWrapper session = JCRSessionFactory.getInstance()
                .getCurrentSystemSession("default", null, null);

        JCRNodeWrapper journalist = null;
        Query query;
            query = session.getWorkspace().getQueryManager().createQuery(
                    "select * from [jnt:jurnalistData] where disabled='true' and [j:published]='true'", Query.JCR_SQL2);

        for(NodeIterator it = query.execute().getNodes(); it.hasNext(); ) {
                journalist = (JCRNodeWrapper) it.nextNode();
                String userUUID = journalist.getPropertyAsString("userUUID");
                JCRNodeWrapper user = UserDao.getJournalistUser(userUUID, session);
                UserDao.deleteUser(user, session);
                journalist.addMixin("");
            }
        }
}
