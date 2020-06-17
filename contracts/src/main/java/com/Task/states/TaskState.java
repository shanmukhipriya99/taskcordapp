package com.Task.states;

import com.Task.contracts.CATContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

// *********
// * State *
// *********
@BelongsToContract(CATContract.class)
public class TaskState implements ContractState {
//    private final Party client;
    private final Party mainContractor;
    private final Party subContractor;
    private final int taskID;
    private final String taskDesc;
    private final int amount;
    private final String assignee;
    private final Instant deadline;

    public TaskState(/*Party client,*/ Party mainContractor, Party subContractor, int taskID, String taskDesc, int amount, String assignee, Instant deadline) {
//        this.client = client;
        this.mainContractor = mainContractor;
        this.subContractor = subContractor;
        this.taskID = taskID;
        this.taskDesc = taskDesc;
        this.amount = amount;
        this.assignee = assignee;
        this.deadline = deadline;
    }

//    public Party getClient() {
//        return client;
//    }

    public Party getMainContractor() {
        return mainContractor;
    }

    public Party getSubContractor() {
        return subContractor;
    }

    public int getTaskID() {
        return taskID;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public int getAmount() {
        return amount;
    }

    public String getAssignee() {
        return assignee;
    }

    public Instant getDeadline() {
        return deadline;
    }

    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(/*client, */mainContractor, subContractor);
    }
}