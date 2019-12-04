package marketmaker.server.service;

import org.springframework.stereotype.Component;

/**
 * Source for reference prices.
 */
public interface ReferencePriceSource {
    /**
     * Subscribe to changes to reference prices.
     *
     * @param listener callback interface for changes
     */
    void subscribe(ReferencePriceSourceListener listener);

    double get(int securityId);
}
