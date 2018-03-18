package com.aftarobot.wallet.data.assets;

import com.aftarobot.wallet.data.Next;
import com.aftarobot.wallet.data.Prev;
import com.aftarobot.wallet.data.Self;

public class LinksAssets {
    private Self self;

    public Self getSelf() { return this.self; }

    public void setSelf(Self self) { this.self = self; }

    private Next next;

    public Next getNext() { return this.next; }

    public void setNext(Next next) { this.next = next; }

    private Prev prev;

    public Prev getPrev() { return this.prev; }

    public void setPrev(Prev prev) { this.prev = prev; }
}
