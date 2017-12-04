package journalist.actions;

import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRPublicationService;
import org.jahia.services.content.JCRSessionFactory;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.rules.AddedNodeFact;
import org.jahia.services.content.rules.PublishedNodeFact;
import org.jahia.services.usermanager.JahiaUserManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import java.util.List;
import java.util.Properties;

public final class UserDao {

    private static Logger LOG = LoggerFactory.getLogger(UserDao.class);

    private static JahiaUserManagerService userManagerService = JahiaUserManagerService.getInstance();

    private static JCRPublicationService publicationService = JCRPublicationService.getInstance();

    public static boolean deleteUser(JCRNodeWrapper user, JCRSessionWrapper session) throws RepositoryException {
        String name = user.getPropertyAsString("Name");
        boolean returnValue = userManagerService.deleteUser(user.getPath(), session);
        session.refresh(true);
        session.save();
        LOG.info("User with name '" + name + "' have been deleted.");
        return returnValue;
    }

    public void modify(PublishedNodeFact publishedNodeFact) throws RepositoryException {
        JCRSessionWrapper session = JCRSessionFactory.getInstance()
                .getCurrentSystemSession("default", null, null);
        JCRNodeWrapper journalist = publishedNodeFact.getNode();
        List<String> nodeTypes = journalist.getNodeTypes();
        JCRNodeWrapper user = getJournalistUser(journalist.getPropertyAsString("userUUID"), session);

        if (nodeTypes.contains("jmix:markedForDeletion")) {
            deleteUser(user, session);
        } else {
            Properties properties = getPropertiesFromNode(journalist);
            for (String key : properties.stringPropertyNames()) {
                user.setProperty(key, properties.getProperty(key));
            }
            session.refresh(true);
            session.save();
            publicationService.publishByMainId(user.getUUID());

            LOG.info("User with name '" + user.getPropertyAsString("Name") + "' have been updated.");
        }
    }

    private Properties getPropertiesFromNode(JCRNodeWrapper node) {
        Properties properties = new Properties();

        for (String propertyName : JournalistProperties.PROPERTIES) {
            if (node.getPropertyAsString(propertyName) != null) {
                properties.setProperty(propertyName, node.getPropertyAsString(propertyName));
            }
        }
        return properties;
    }

    public static JCRNodeWrapper getJournalistUser(String uuid, JCRSessionWrapper session) throws RepositoryException {
        JCRNodeWrapper user = null;
        Query query;
        if (uuid != null) {
            query = session.getWorkspace().getQueryManager().createQuery(
                    "select * from [jnt:user] where [jcr:uuid]='" + uuid + "'", Query.JCR_SQL2);
            user = (JCRNodeWrapper) query.execute().getNodes().nextNode();
        }
        return user;
    }

    public void createUser(AddedNodeFact addedNodeFact) throws RepositoryException {
        JCRSessionWrapper session = JCRSessionFactory.getInstance()
                .getCurrentSystemSession("default", null, null);
        JCRNodeWrapper journalistNode = addedNodeFact.getNode();
        Properties properties = getPropertiesFromNode(journalistNode);

        JCRNodeWrapper user = userManagerService.createUser(journalistNode.getName(),
                properties.getProperty("Password"), properties, session);
        journalistNode.setProperty("userUUID", user.getUUID());
        journalistNode.saveSession();
        user.getSession().refresh(true);
        user.getSession().save();
        session.refresh(true);
        session.save();
        publicationService.publishByMainId(journalistNode.getUUID());
        publicationService.publishByMainId(user.getUUID());
        LOG.info("New User with name' " + user.getName() + "' have been created.");
    }
}
