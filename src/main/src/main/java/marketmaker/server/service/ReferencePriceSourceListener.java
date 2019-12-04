package marketmaker.server.service;

import org.springframework.stereotype.Component;

/**
 * Callback interface for {@link ReferencePriceSource}
 */
public interface ReferencePriceSourceListener {

    /**
     * Called when a price has changed.
     *
     * @param securityId security identifier
     * @param price      reference price
     */
    void referencePriceChanged(int securityId, double price);

}
