package com.template.states;

import com.template.contracts.TemplateContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;

import java.util.Arrays;
import java.util.List;

// *********
// * State *
// *********
@BelongsToContract(TemplateContract.class)
public class TaskState implements ContractState {

    public TaskState() {

    }

    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList();
    }
}