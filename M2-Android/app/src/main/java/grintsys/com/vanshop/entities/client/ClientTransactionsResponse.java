package grintsys.com.vanshop.entities.client;

import java.util.List;

import grintsys.com.vanshop.entities.Metadata;

/**
 * Created by serpel on 5/9/2017.
 */

public class ClientTransactionsResponse {

    private Metadata metadata;

    private List<ClientTransactions> transactions;

    public ClientTransactionsResponse() { }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<ClientTransactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<ClientTransactions> transactions) {
        this.transactions = transactions;
    }

    @Override
    public int hashCode() {
        int result = metadata != null ? metadata.hashCode() : 0;
        result = 31 * result + (transactions != null ? transactions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClientTransactionsResponse{" +
                "metadata=" + metadata +
                ", payments=" + transactions +
                '}';
    }
}
