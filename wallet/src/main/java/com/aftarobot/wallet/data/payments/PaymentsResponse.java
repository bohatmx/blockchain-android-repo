package com.aftarobot.wallet.data.payments;

public class PaymentsResponse {
    private Links _links;

    public Links getLinks() { return this._links; }

    public void setLinks(Links _links) { this._links = _links; }

    private Embedded _embedded;

    public Embedded getEmbedded() { return this._embedded; }

    public void setEmbedded(Embedded _embedded) { this._embedded = _embedded; }
}
