package com.Task.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.utilities.ProgressTracker;
import org.jetbrains.annotations.NotNull;

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
        class SignTxFlow extends SignTransactionFlow {
            private SignTxFlow(FlowSession otherPartyFlow, ProgressTracker progressTracker) {
                super(otherPartyFlow, progressTracker);
            }


            @Override
            protected void checkTransaction(@NotNull SignedTransaction stx) throws FlowException {

                // });
            }

        }
        final SignTxFlow signTxFlow = new SignTxFlow(counterpartySession, SignTransactionFlow.Companion.tracker());
        final SecureHash txId = subFlow(signTxFlow).getId();

        return subFlow(new ReceiveFinalityFlow(counterpartySession, txId));
      /*  System.out.println("Task received from: " + counterpartySession.getCounterparty().getName().getOrganisation());
        return subFlow(new ReceiveFinalityFlow(counterpartySession)*/
    }
}