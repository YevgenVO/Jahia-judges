package actions;

import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.query.QueryResultWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;

import javax.jcr.Node;
import javax.jcr.query.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ReverseNames extends Action {

    @Override
    public ActionResult doExecute(HttpServletRequest req, RenderContext renderContext, Resource resource,
                                  JCRSessionWrapper session, Map<String, List<String>> parameters,
                                  URLResolver urlResolver) throws Exception {
        Query query = session.getWorkspace().getQueryManager().createQuery("select * from [jnt:judgeInform]", Query.JCR_SQL2);
        QueryResultWrapper queryResult = (QueryResultWrapper) query.execute();
        for (Node node : queryResult.getNodes()) {
            String name = node.getProperty("name").getString();
            node.setProperty("name", node.getProperty("Surname").getString());
            node.setProperty("Surname", name);
        }
        session.save();
        return new ActionResult(HttpServletResponse.SC_OK, "/sites/bger/home/judge2");
    }
}
