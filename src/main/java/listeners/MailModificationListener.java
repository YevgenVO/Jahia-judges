package listeners;

import org.jahia.api.Constants;
import org.jahia.services.content.DefaultEventListener;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRPropertyWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.JCRService;

import javax.jcr.RepositoryException;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MailModificationListener extends DefaultEventListener {

    public MailModificationListener() {
        super();
        setWorkspace(Constants.EDIT_WORKSPACE);
    }

    private MailService mailService;

    private static final Logger LOG = LoggerFactory.getLogger(MailModificationListener.class);

    private JCRService jcrService;

    public String[] getNodeTypes() {
        return new String[] {"jnt:jurnalistData"};
    }

    @Override
    public int getEventTypes() {
        return Event.PROPERTY_CHANGED + Event.PROPERTY_REMOVED + Event.PROPERTY_ADDED;
    }

    @Override
    public void onEvent(EventIterator eventIterator) {
        LOG.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!Inside onEvent()");
        operationTypes = new HashSet<>();
        operationTypes.add(Event.PROPERTY_ADDED);
        operationTypes.add(Event.PROPERTY_CHANGED);
        operationTypes.add(Event.PROPERTY_REMOVED);
        while (eventIterator.hasNext()) {
            try {
                Event event = eventIterator.nextEvent();
                String path = event.getPath();
                LOG.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!path=" + path);
                checkJournalistChanges(path, event);
            } catch (RepositoryException e) {
                LOG.info("!!!!!!!!!!!!!!!!!!!!!!!!!Error while getting event node!");
            }
        }
    }

    private void checkJournalistChanges(String path, Event event) {

        if (path.contains("/contents/journalists/") && isSupportedOperationType(event.getType())) {
            String propertyName = path.substring(path.lastIndexOf('/') + 1);
            JCRPropertyWrapper property;
            JCRSessionWrapper session = jcrService.getSession(getWorkspace());
            try {
                if (session.itemExists(path)) {
                    property = (JCRPropertyWrapper) session.getItem(path);
                } else {
                    property = (JCRPropertyWrapper) session.getNodeByIdentifier(event.getIdentifier());
                }
                JCRNodeWrapper journalist = property.getParent();
                if (journalist.isNodeType("jnt:jurnalistData") && (propertyName.equals("Email") || propertyName.equals("AdditionalEmail"))) {
                    sendMessage(journalist, session);
                }
            } catch (RepositoryException e) {
                LOG.info("!!!!!!!!!!!!!!!Cannot find parrent of property '" + propertyName + "' and path '" + path + "'");
            }
        }
    }

    private void sendMessage(JCRNodeWrapper journalist, JCRSessionWrapper session) throws RepositoryException {
        LOG.info("!!!!!!!!!!!!!!MailModificationListener senMessage()");
        Map<String, String> conditionMap = new HashMap<>();
        conditionMap.put("[j:nodename]", "mailSenderData");
        JCRNodeWrapper mailTemplate  = jcrService.getFirstNodeWithCondition("jnt:mailSenderData", session, conditionMap);
        if (mailTemplate != null) {
            Set<String> emails = new HashSet<>();
            emails.addAll(Arrays.asList(journalist.getPropertyAsString("AdditionalEmail").split(" ")));
            emails.add(journalist.getPropertyAsString("Email"));

            String templatePath = "resources/templates/journalist-emailChange-mail.vm";
            String templateName = "judges";
            Map<String, Object> bindings = new HashMap<>();
            bindings.putAll(mailTemplate.getPropertiesAsString());
            bindings.putAll(journalist.getPropertiesAsString());
            Locale locale = session.getLocale();
            if (session.getLocale() == null) locale = Locale.getDefault();
            for (String to : emails) {
                try {
                    mailService.sendMessageWithTemplate(templatePath, bindings, to, mailTemplate.getPropertyAsString("from")
                            , "", "", locale, templateName);
                } catch (ScriptException e) {
                    LOG.info("!!!!!!!!!!!!!!!!!!!!!!!Cannot send message.");
                }
                LOG.info("Mail to " + to + " have been sent!");
            }
        }
    }

    public void setJcrService(JCRService jcrService) {
        this.jcrService = jcrService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }
}
