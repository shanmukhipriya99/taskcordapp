package com.Task.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

// ******************
// * Responder flow *
// ******************
@InitiatedBy(CATInitiatorFlow.class)
public class CATResponderFlow extends FlowLogic<Void> {
    private final FlowSession SubSession;
//    private final FlowSession ClientSession;

    public CATResponderFlow(FlowSession SubSession/*, FlowSession ClientSession*/) {
        this.SubSession = SubSession;
//        this.ClientSession = ClientSession;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        // Responder flow logic goes here.
        SignedTransaction signedtx1 = subFlow(new SignTransactionFlow(SubSession) {
            protected void checkTransaction(SignedTransaction stx) throws FlowException {
                // Implement responder flow transaction checks here
            }
        });
//        SignedTransaction signedtx2 = subFlow(new SignTransactionFlow(ClientSession) {
//            protected void checkTransaction(SignedTransaction stx) throws FlowException {
//                // Implement responder flow transaction checks here
//            }
//        });
//        System.out.println("Task received from: " + counterpartySession.getCounterparty().getName().getOrganisation());
        subFlow(new ReceiveFinalityFlow(SubSession, signedtx1.getId()));
//        subFlow(new ReceiveFinalityFlow(ClientSession, signedtx2.getId()));
        return null;
    }
}
