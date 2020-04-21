package com.Task.contracts;

import com.Task.states.TaskState;
import javafx.concurrent.Task;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;
import java.security.PublicKey;
import java.time.Instant;
import java.util.List;
import static net.corda.core.contracts.ContractsDSL.requireThat;

// ************
// * Contract *
// ************
public class RTContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String ID = "com.Task.contracts.RTContract";

    // A transaction is valid if the verify() function of the contract of all the transaction's input and output states
    // does not throw an exception.
    @Override
    public void verify(LedgerTransaction tx) {
        if (tx.getCommands().size() != 1) {
            throw new IllegalArgumentException("Command input can be only one");
        }
        Command command = tx.getCommand(0);
        CommandData commandType = command.getValue();
        List<PublicKey> reqSigners = command.getSigners();
        if (commandType instanceof Task) {
            // Shape Rules
            if (tx.getInputStates().size() != 0) {
                throw new IllegalArgumentException("No input required");
            }
            if (tx.getOutputs().size() != 1) {
                throw new IllegalArgumentException("Only one output is possible");
            }
            // Content Rules
            ContractState outputState = tx.getOutput(0);
            if (!(outputState instanceof TaskState)) {
                throw new IllegalArgumentException("Output state should belong to TaskState type");
            }
            TaskState rtstate = (TaskState) outputState;
            // Amount must be positive
            // Deadline should be greater than the current date
            // Can be initiated by main contractor only
            requireThat(require -> {
                require.using("Amount must be positive", rtstate.getAmount() > 0);
                require.using("Deadline must should be a future data", rtstate.getDeadline().isAfter(Instant.now()));
                //require.using("Can be initiated by main contractor only", rtstate.);
                return null;
            });
            // Signer Rules
            PublicKey clientKey = rtstate.getClient().getOwningKey();
            PublicKey mainKey = rtstate.getClient().getOwningKey();
            PublicKey subKey = rtstate.getClient().getOwningKey();
            if (!(reqSigners.contains(clientKey))) {
                throw new IllegalArgumentException("Client must sign.");
            }
            if (!(reqSigners.contains(mainKey))) {
                throw new IllegalArgumentException("Main contractor must sign.");
            }
            if (!(reqSigners.contains(subKey))) {
                throw new IllegalArgumentException("Sub contractor must sign.");
            }
        }
    }

    // Used to indicate the transaction's intent.
    public interface Commands extends CommandData {
        class Action implements Commands {}
    }
}