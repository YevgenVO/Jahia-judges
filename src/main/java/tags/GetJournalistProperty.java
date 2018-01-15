package tags;

import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.taglibs.AbstractJahiaTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.servlet.jsp.JspException;

public class GetJournalistProperty extends AbstractJahiaTag {

    private String name;

    private String var;

    private JCRNodeWrapper node;

    private static final Logger LOG = LoggerFactory.getLogger(GetJournalistProperty.class);

    public int doStartTag() throws JspException {

        try {
            pageContext.removeAttribute(name);
            if(node.getProperty(name).getDefinition().isMultiple()) {
                pageContext.setAttribute(var, node.getProperty(name).getValues());
            } else {
                pageContext.setAttribute(var, node.getProperty(name).getValue());
//                LOG.info("Property with name \"" + name + "\" = " + node.getPropertyAsString(name));
            }
        } catch (RepositoryException e) {
//            LOG.info("!!!!!!!!!!!!!!!!!Property with name \"" + name + "\" doesn't exists!");
        }

        return super.doStartTag();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setNode(JCRNodeWrapper node) {
        if(node == null) LOG.info("Journalist does not exist!");
        this.node = node;
    }
}
