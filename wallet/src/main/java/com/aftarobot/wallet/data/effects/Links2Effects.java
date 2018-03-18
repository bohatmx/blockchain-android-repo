package com.aftarobot.wallet.data.effects;

import com.aftarobot.wallet.data.Operation;
import com.aftarobot.wallet.data.Precedes;
import com.aftarobot.wallet.data.Succeeds;

public class Links2Effects {
    private Operation operation;

    public Operation getOperation() { return this.operation; }

    public void setOperation(Operation operation) { this.operation = operation; }

    private Succeeds succeeds;

    public Succeeds getSucceeds() { return this.succeeds; }

    public void setSucceeds(Succeeds succeeds) { this.succeeds = succeeds; }

    private Precedes precedes;

    public Precedes getPrecedes() { return this.precedes; }

    public void setPrecedes(Precedes precedes) { this.precedes = precedes; }
}
