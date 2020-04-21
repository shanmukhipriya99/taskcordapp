package com.Task.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatedBy;

// ******************
// * Responder flow *
// ******************
@InitiatedBy(Initiator.class)
public class TTResponderFlow extends FlowLogic<Void> {
    private FlowSession counterpartySession;

    public TTResponderFlow(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        // Responder flow logic goes here.

        return null;
    }
}
