package filter;

import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.AbstractFilter;
import org.jahia.services.render.filter.RenderChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedirectUserToEditModeFilter extends AbstractFilter {

    private final static Logger LOG = LoggerFactory.getLogger(RedirectUserToEditModeFilter.class);


    @Override
    public String execute(String previousOut, RenderContext renderContext, Resource resource, RenderChain chain) throws Exception {

        String url = renderContext.getRequest().getRequestURL().toString();
        String uri= renderContext.getRequest().getRequestURI().toString();
        String redirectUrl = url.replace(uri, "")
                .concat(uri.replace("live", "default"));
        if (uri.contains("/sites/bger/home/testFilter")) {
            renderContext.getResponse().sendRedirect(redirectUrl);
        }
        LOG.info("!!!!!!!!!User have been successfully redirected from '" + url + "' to '" + redirectUrl + "'");
        return previousOut;
    }

}