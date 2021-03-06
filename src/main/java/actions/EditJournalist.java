package actions;

import journalist.actions.UserDao;
import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRPublicationService;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.decorator.JCRUserNode;
import org.jahia.services.mail.MailService;
import org.jahia.services.query.QueryResultWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class EditJournalist extends Action {

    private static final Logger LOG = LoggerFactory.getLogger(EditJournalist.class);

    private static JCRPublicationService publicationService = JCRPublicationService.getInstance();

    private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    private  MailService mailService;

    private UserDao userDao = new UserDao();

//    public void setUserDao(UserDao userDao) {
//        this.userDao = userDao;
//    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public ActionResult doExecute(HttpServletRequest request, RenderContext renderContext, Resource resource,
                                  JCRSessionWrapper session, Map<String, List<String>> map,
                                  URLResolver urlResolver) throws Exception {
        ActionResult result= new ActionResult(HttpServletResponse.SC_OK, request.getParameter("currentPageUrl"));

        String query = "select * from [jnt:jurnalistData] where [jcr:uuid]='" + request.getParameter("uuid") + "'";
        JCRNodeWrapper journalistNode = (JCRNodeWrapper) session.getWorkspace().getQueryManager()
                .createQuery(query, Query.JCR_SQL2).execute().getNodes().nextNode();

        List<String> lan = new ArrayList<>();
        if (request.getParameter("Allemand") != null) lan.add("Allemand");
        if (request.getParameter("Francais") != null) lan.add("Franch");
        if (request.getParameter("Italien") != null) lan.add("Italy");
        String[] languages = new String[(lan.toArray().length)];
        languages = lan.toArray(languages);
        journalistNode.setProperty("Languages", languages);
        String email = request.getParameter("email");
        if (!email.matches(EMAIL_REGEX) ||
            request.getParameter("Adress").length() < 4 ||
            request.getParameter("Place").length() < 4 ||
            !request.getParameter("NPA").matches("\\d+")) {
            return result;
        }
        journalistNode.setProperty("Email", request.getParameter("email"));
        journalistNode.setProperty("Adress", request.getParameter("Adress"));
        journalistNode.setProperty("NPA", request.getParameter("NPA"));
        journalistNode.setProperty("Place", request.getParameter("Place"));
        journalistNode.setProperty("PhoneNum", request.getParameter("PhoneNum"));
        journalistNode.setProperty("CellphoneNumber", request.getParameter("CellphoneNumber"));
        Set<String> emails = new HashSet<>();
        emails.addAll(Arrays.asList(request.getParameterValues("AdditionalEmail")));
        emails.remove("");
        journalistNode.setProperty("AdditionalEmail", emails.toArray(new String[emails.size()]));
        journalistNode.setProperty("NewspapersConcerned", request.getParameter("NewspapersConcerned"));
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String passConfirmation = request.getParameter("passConfirmation");
        boolean changeUserPassword = false;
        JCRUserNode userNode = (JCRUserNode) userDao.getJournalistUser(journalistNode.getPropertyAsString("userUUID"), session);
        if (oldPassword != null && oldPassword != "" && (!userNode.verifyPassword(oldPassword) || !newPassword.equals(passConfirmation)
                || newPassword.length() < 6)) {
            return result;
        }

        if (userNode.verifyPassword(oldPassword)) {
            journalistNode.setProperty("Password", request.getParameter("newPassword"));
            changeUserPassword = true;
        }
        journalistNode.getSession().save();
        userDao.doModify(journalistNode);
        session.refresh(true);
        session.save();

        publicationService.publishByMainId(journalistNode.getIdentifier(), "live", "default",
                (Set)null, true, (List)null);
        if (changeUserPassword) {
            userNode.getSession().save();
            publicationService.publishByMainId(userNode.getUUID(), "live", "default",
                    (Set) null, true, (List) null);
        }
        LOG.info("Journalist with name " + journalistNode.getPropertyAsString("Name") + " have been modified!");
        sendMail(journalistNode, resource.getLocale(), session, request.getParameter("mailSenderData"));
        return result;
    }

    private void sendMail(JCRNodeWrapper journalist, Locale locale, JCRSessionWrapper session, String mailSenderData) throws RepositoryException, ScriptException {
        String query = "select * from [jnt:mailSenderData] where [j:nodename] = '" + mailSenderData + "'";
        QueryResultWrapper queryWrapper = session.getWorkspace().getQueryManager().
                createQuery(query, Query.JCR_SQL2).execute();
        if (queryWrapper.getNodes().hasNext()) {
            JCRNodeWrapper mailTemplate = (JCRNodeWrapper) queryWrapper.getNodes().next();
            Set<String> emails = new HashSet<>();
            emails.addAll(Arrays.asList(journalist.getPropertyAsString("AdditionalEmail").split(" ")));
            emails.add(journalist.getPropertyAsString("Email"));
            String from = mailTemplate.getPropertyAsString("from");

            String templatePath = "resources/templates/journalist-edit-mail.vm";
            String templateName = "judges";
            Map<String, Object> bindings = new HashMap<>();
            bindings.putAll(mailTemplate.getPropertiesAsString());
            bindings.putAll(journalist.getPropertiesAsString());
            for (String to : emails) {
                mailService.sendMessageWithTemplate(templatePath, bindings, to, from, "", "", locale, templateName);
                LOG.info("Mail to " + to + " have been sent!");
            }
        }
    }
}
