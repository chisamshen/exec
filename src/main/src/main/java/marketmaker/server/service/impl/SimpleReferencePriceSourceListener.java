package marketmaker.server.service.impl;

import marketmaker.server.pojo.event.QuotePriceChangeEvent;
import marketmaker.server.service.ReferencePriceSourceListener;
import marketmaker.util.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleReferencePriceSourceListener implements ReferencePriceSourceListener {

    private Logger log = LoggerFactory.getLogger(SimpleReferencePriceSourceListener.class);

    private CacheManager cacheManager = CacheManager.getInstance();

    @Override
    public void referencePriceChanged(int securityId, double price) {
        log.info("Detect price changed for Security Id: {}, new price is: {}", securityId, price);
        cacheManager.push(new QuotePriceChangeEvent(securityId, price));
    }

}
