package tags;


import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRPublicationService;
import org.jahia.services.content.JCRSessionFactory;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.JCRValueWrapper;
import org.jahia.taglibs.AbstractJahiaTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.servlet.jsp.JspException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChangeJournalistProperty extends AbstractJahiaTag {

    private static final Logger LOG = LoggerFactory.getLogger(ChangeJournalistProperty.class);

    private static JCRPublicationService publicationService = JCRPublicationService.getInstance();

    private String name;

    private String propertyValue;

    private JCRNodeWrapper journalist;

    public ChangeJournalistProperty() {
        super();
    }

    public int doStartTag() throws JspException {
        try {
            if (journalist == null) {
                LOG.info("!!!!!!!!!!!!!!!!!!!!!!Journalist does not exist!!!");
            } else if (journalist.getPropertiesAsString().get(name) == null) {
                LOG.info("!!!!!!!!!!!!!!!!!!!!!!Property '" + name + "' does not exist!!!");
            } else {
                    JCRSessionWrapper session = JCRSessionFactory.getInstance()
                            .getCurrentSystemSession("live", null, null);
                    if (journalist.getProperty(name).getDefinition().isMultiple()) {
                        Set<String> emails = new HashSet<>();
                        JCRValueWrapper[] values = journalist.getProperty(name).getValues();
                        emails.add(propertyValue);
                        for(int i = 1; i < values.length; i++) {
                            emails.add(values[i].getDefinition().toString());
                        }
                        emails.remove("");
                        journalist.setProperty("AdditionalEmail", emails.toArray(new String[emails.size()]));
                    } else {
                        LOG.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Value = " + propertyValue);
                        journalist.setProperty(name, propertyValue);
                    }
                        journalist.saveSession();
                        session.refresh(true);
                        session.save();
                        publicationService.publishByMainId(journalist.getUUID(), "live", "default",
                                (Set) null, true, (List) null);
                        LOG.info("Journalist with name " + journalist.getPropertyAsString("Name") + " have been modified!");

                }
        } catch (RepositoryException e) {
        LOG.info("!!!!!!!!!!!!!!!!!!!!!!!!!!Invalid value type.");
    }
        return super.doEndTag();
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJournalist(JCRNodeWrapper journalist) {
        this.journalist = journalist;
    }
}
