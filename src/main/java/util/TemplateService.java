package util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jahia.services.content.JCRNodeWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class TemplateService {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateService.class);

    private VelocityEngine ve = new VelocityEngine();

    private Template template;

    private VelocityContext context = new VelocityContext();

    public String getMessageFromTemplate(JCRNodeWrapper journalist, String pathToFile) throws IOException, RepositoryException {
        try {
            ve.init();
            template = ve.getTemplate(pathToFile);
        } catch (Exception e) {
            LOG.error("Error during velocity initialization!");
        }
        Map<String, String> data = journalist.getPropertiesAsString();

        Writer  writer = new StringWriter();
        for (String key: data.keySet()) {
            context.put(key, data.get(key));
        }
        template.merge(context,  writer);
        return  writer.toString();
    }
}
