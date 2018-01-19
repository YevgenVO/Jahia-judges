package listeners;

import org.jahia.services.content.DefaultEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.observation.EventIterator;

public class TestListener extends DefaultEventListener{

    private final static Logger LOG = LoggerFactory.getLogger(TestListener.class);

    @Override
    public int getEventTypes() {
        return 0;
    }

    @Override
    public void onEvent(EventIterator eventIterator) {
        LOG.info("!!!!!!!!!!!!!!!!!!!1inside TestListener onEvent()");
        if (eventIterator.hasNext()) {
            try {
                LOG.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + eventIterator.nextEvent().getPath());
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }
    }
}
