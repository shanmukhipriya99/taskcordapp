package com.Task.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.Task.contracts.CATContract;
import com.Task.states.TaskState;
import com.sun.xml.bind.v2.model.core.ID;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
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
    private final Party subContractor;
    private final int taskID;
    private final String taskDesc;
    private final int amount;
    private final String assignee;
    private final Instant deadline;
    private final ProgressTracker.Step Create_And_Assign_Task = new ProgressTracker.Step("Creating and assigning a task.");
    private final ProgressTracker.Step Signing_Transaction = new ProgressTracker.Step("Signing transaction with our private key.");
    private final ProgressTracker progressTracker = new ProgressTracker();


    public CATInitiatorFlow(Party Client, Party subContractor, int taskID, String taskDesc, int amount, String assignee, Instant deadline) {
        this.Client = Client;
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

        return null;
    }
}
