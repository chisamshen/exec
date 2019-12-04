package marketmaker.server.service.impl;

import marketmaker.server.service.QuoteCalculationEngine;
import marketmaker.server.service.ReferencePriceSource;
import marketmaker.server.service.ReferencePriceSourceListener;

public class SimpleQuoteCalculationEngine implements QuoteCalculationEngine {

    private ReferencePriceSource referencePriceSource;

    public SimpleQuoteCalculationEngine() {
        this.referencePriceSource = new SimpleReferencePriceSource();
        ReferencePriceSourceListener listener = new SimpleReferencePriceSourceListener();
        referencePriceSource.subscribe(listener);
    }

    @Override
    public double calculateQuotePrice(int securityId, double referencePrice, boolean buy, int quantity) {
        double quotePrice = 0;
        // Calculating logic here
        quotePrice = buy ? referencePrice + 1D : referencePrice - 1D;
        return quotePrice;
    }

    @Override
    public double askQuotePrice(int securityId, boolean buy, int quantity) {
        return this.calculateQuotePrice(securityId, referencePriceSource.get(securityId), buy, quantity);
    }

}
