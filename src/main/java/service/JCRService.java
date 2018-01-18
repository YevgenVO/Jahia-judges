package service;

import org.jahia.services.content.JCRNodeIteratorWrapper;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionFactory;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.query.QueryResultWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import java.util.Locale;
import java.util.Map;

public class JCRService {

    private JCRSessionFactory jcrSessionFactory = JCRSessionFactory.getInstance();

    private final static Logger LOG = LoggerFactory.getLogger(JCRService.class);

    public JCRSessionWrapper getSession(String workspace) {
        return getSession(workspace, null);
    }

    public JCRSessionWrapper getSession(String workspace, Locale locale) {
        return getSession(workspace, locale, null);
    }

    public JCRSessionWrapper getSession(String workspace, Locale locale, Locale fallbackLocale) {
        try {
            return jcrSessionFactory.getCurrentSystemSession(workspace, locale, fallbackLocale);
        } catch (RepositoryException e) {
            LOG.info("!!!!!!!!!!!!!!!!Can not get session.");
            return null;
        }
    }

    public JCRNodeIteratorWrapper getNodes(String nodeType, JCRSessionWrapper session) {
        QueryResultWrapper nodes = null;
        String query = "select * from [" + nodeType + "]";
        try {
        nodes = session.getWorkspace().getQueryManager().createQuery(query, Query.JCR_SQL2).execute();
            return nodes.getNodes();
        } catch (RepositoryException e) {
            LOG.info("!!!!!!!!!!!!!!!!!!!!!Cannot get nodes with nodeType='" + nodeType + "'");
        }
        return null;
    }

    public JCRNodeIteratorWrapper getNodesWithCondition(String nodeType, JCRSessionWrapper session, Map<String, String> conditionMap)  {
        StringBuilder condition = new StringBuilder();
        for (String key: conditionMap.keySet()) {
            condition.append(" " + key + "='" + conditionMap.get(key) + "'");
        }
        if (condition.length() > 0) condition.insert(0, " where");
        QueryResultWrapper nodes = null;
        String query = "select * from [" + nodeType + "]" + condition;
        try {
            nodes = session.getWorkspace().getQueryManager().
                    createQuery(query, Query.JCR_SQL2).execute();
            if (nodes.getNodes() == null || !nodes.getNodes().hasNext()) {
                LOG.info("!!!!!!!!!!!!!!!!!!!!!Query '" + query + "' doesn't have any result!");
                return null;
            }
            return nodes.getNodes();
        } catch (RepositoryException e) {
            LOG.info("!!!!!!!!!!!!!!!!!!!!!Cannot execute query '" + query);
            return null;
        }
    }

    public JCRNodeWrapper getFirstNodeWithCondition(String nodeType, JCRSessionWrapper session, Map<String, String> conditionMap) {
        return (JCRNodeWrapper) getNodesWithCondition(nodeType, session, conditionMap).next();
    }


    public void setJcrSessionFactory(JCRSessionFactory jcrSessionFactory) {
        this.jcrSessionFactory = jcrSessionFactory;
    }
}
