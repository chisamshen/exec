package marketmaker.server.pojo.event;

public class QuotePriceChangeEvent implements Event {

    public QuotePriceChangeEvent(int securityId, double referencePrice) {
        this.securityId = securityId;
        this.referencePrice = referencePrice;
    }

    private int securityId;

    private double referencePrice;

    public int getSecurityId() {
        return securityId;
    }

    public void setSecurityId(int securityId) {
        this.securityId = securityId;
    }

    public double getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(double referencePrice) {
        this.referencePrice = referencePrice;
    }
}
