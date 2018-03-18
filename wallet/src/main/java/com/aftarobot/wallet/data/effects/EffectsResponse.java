package com.aftarobot.wallet.data.effects;

public class EffectsResponse {
    private LinksEffects _links;

    public LinksEffects getLinks() { return this._links; }

    public void setLinks(LinksEffects _links) { this._links = _links; }

    private EmbeddedEffects _embedded;

    public EmbeddedEffects getEmbedded() { return this._embedded; }

    public void setEmbedded(EmbeddedEffects _embedded) { this._embedded = _embedded; }
}
