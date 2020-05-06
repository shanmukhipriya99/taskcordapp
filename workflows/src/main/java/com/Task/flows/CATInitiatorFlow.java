package com.Task.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.Task.contracts.CATContract;
import com.Task.states.TaskState;
import com.google.common.collect.ImmutableSet;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import static com.Task.contracts.CATContract.ID;
import java.time.Instant;

// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class CATInitiatorFlow extends FlowLogic<SignedTransaction> {
    private final Party Client;
    private final Party mainContractor;
    private final Party subContractor;
    private final int taskID;
    private final String taskDesc;
    private final int amount;
    private final String assignee;
    private final Instant deadline;
    private final ProgressTracker.Step GENERATING_TRANSACTION = new ProgressTracker.Step("Creating and assigning task.");
    private final ProgressTracker.Step VERIFYING_TRANSACTION = new ProgressTracker.Step("Verifying task constraints.");
    private final ProgressTracker.Step SIGNING_TRANSACTION = new ProgressTracker.Step("Signing task with our private key.");
    private final ProgressTracker.Step GATHERING_SIGS = new ProgressTracker.Step("Gathering the counterparty's signatures.") {
        @Override
        public ProgressTracker childProgressTracker() {
            return CollectSignaturesFlow.Companion.tracker();
        }
    };
    private final ProgressTracker.Step FINALISING_TRANSACTION = new ProgressTracker.Step("Recording task.") {
        @Override
        public ProgressTracker childProgressTracker() {
            return FinalityFlow.Companion.tracker();
        }
    };
    private final ProgressTracker progressTracker = new ProgressTracker(
            GENERATING_TRANSACTION,
            VERIFYING_TRANSACTION,
            SIGNING_TRANSACTION,
            GATHERING_SIGS,
            FINALISING_TRANSACTION
    );


    public CATInitiatorFlow(Party Client, Party mainContractor, Party subContractor, int taskID, String taskDesc, int amount, String assignee, Instant deadline) {
        this.Client = Client;
        this.mainContractor = mainContractor;
        this.subContractor = subContractor;
        this.taskID = taskID;
        this.taskDesc = taskDesc;
        this.amount = amount;
        this.assignee = assignee;
        this.deadline = deadline;
    }


    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // Initiator flow logic goes here.
        if (getOurIdentity().getName().getOrganisation().equals("mainContractor")) {
            System.out.println("Main contractor is the initiator.");
        } else {
            throw new FlowException("Not initiated by the main contractor");
        }
        TaskState outputState = new TaskState(Client, getOurIdentity(), subContractor, taskID, taskDesc, amount, assignee, deadline);
        //Stage1
        progressTracker.setCurrentStep(GENERATING_TRANSACTION);
        //Generating unsigned task
        TransactionBuilder txBuilder = new TransactionBuilder(Client).addOutputState(outputState,ID).addCommand(new CATContract.Task(),getOurIdentity().getOwningKey());
        //Stage2
        progressTracker.setCurrentStep(VERIFYING_TRANSACTION);
        txBuilder.verify(getServiceHub());
        //Stage3
        progressTracker.setCurrentStep(SIGNING_TRANSACTION);
        //Sign the transaction
        SignedTransaction TaskTxn = getServiceHub().signInitialTransaction(txBuilder);
        //Stage4
        progressTracker.setCurrentStep(GATHERING_SIGS);
        //Send the state to counterparty and receive with signatures
        FlowSession TaskSession = initiateFlow(subContractor);
        SignedTransaction fullySignedTx = subFlow(new CollectSignaturesFlow(TaskTxn, ImmutableSet.of(TaskSession), CollectSignaturesFlow.Companion.tracker()));
        //Stage5
        progressTracker.setCurrentStep(FINALISING_TRANSACTION);
        return subFlow(new FinalityFlow(TaskTxn, TaskSession));

    }
}
