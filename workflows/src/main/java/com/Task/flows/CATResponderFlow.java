package com.Task.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

// ******************
// * Responder flow *
// ******************
@InitiatedBy(CATInitiatorFlow.class)
public class CATResponderFlow extends FlowLogic<SignedTransaction> {
    private FlowSession counterpartySession;

    public CATResponderFlow(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // Responder flow logic goes here.
        System.out.println("Task received from: " + counterpartySession.getCounterparty().getName().getOrganisation());
        return subFlow(new ReceiveFinalityFlow(counterpartySession));
    }
}
