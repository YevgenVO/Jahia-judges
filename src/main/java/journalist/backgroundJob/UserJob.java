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
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;

public class UserJob extends BackgroundJob {

    private static final Logger LOG = LoggerFactory.getLogger(UserJob.class);

    @Override
    public void executeJahiaJob(JobExecutionContext jobExecutionContext) throws Exception {
        JCRSessionWrapper liveSession = JCRSessionFactory.getInstance()
                .getCurrentSystemSession("live", null, null);
        JCRSessionWrapper defaultSession = JCRSessionFactory.getInstance()
                .getCurrentSystemSession("default", null, null);

        JCRNodeWrapper journalist = null;
        Query query;
        query = liveSession.getWorkspace().getQueryManager().createQuery(
                "select * from [jnt:jurnalistData] where disabled='true'", Query.JCR_SQL2);

        for (NodeIterator it = query.execute().getNodes(); it.hasNext(); ) {
            journalist = (JCRNodeWrapper) it.nextNode();

            if (journalist.getPropertyAsString("disabled") == "true") {
                String userUUID = journalist.getPropertyAsString("userUUID");
                String userName = "";
                String journalistName = "";
                if (userUUID != null && userUUID != "") {
                    JCRNodeWrapper userLive = UserDao.getJournalistUser(userUUID, liveSession);
                    JCRNodeWrapper userdefault = UserDao.getJournalistUser(userUUID, defaultSession);
                    if (userLive != null) {
                        userName = userLive.getName();
                        userLive.remove();
                    }
                    if (userdefault != null) userdefault.remove();
                }
                if (journalist != null) {
                    journalistName = journalist.getPropertyAsString("Name");
                    JCRNodeWrapper defaultJournalist = getNodeByUUID(journalist.getUUID(), defaultSession);
                    if (defaultJournalist != null) defaultJournalist.remove();
                    journalist.remove();
                }
                liveSession.refresh(true);
                liveSession.save();
                defaultSession.refresh(true);
                defaultSession.save();

                LOG.info("User with name " + userName + " have been deleted.");
                LOG.info("Journalist with name " + journalistName + " have been deleted.");
            }
        }
    }

    private JCRNodeWrapper getNodeByUUID(String uuid, JCRSessionWrapper session) {
        JCRNodeWrapper node = null;
        try {
            Query query = session.getWorkspace().getQueryManager().createQuery(
                    "select * from [jnt:jurnalistData] where [jcr:uuid]='" + uuid + "'", Query.JCR_SQL2);
            node = (JCRNodeWrapper) query.execute().getNodes().nextNode();

        } catch (RepositoryException e) {
            LOG.error("Journalist wiht uuid=" + uuid + " can not be found in " + session.getWorkspace() + " workspace.");
        }
        return node;
    }
}
