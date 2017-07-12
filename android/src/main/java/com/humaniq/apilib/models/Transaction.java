package com.humaniq.apilib.models;

/**
 * Created by gritsay on 7/12/17.
 */

import com.google.gson.annotations.SerializedName;

public class Transaction {

    private String transaction;

    private int type;

    private int status;

    private long amount;

    private long balance;

    @SerializedName("received_transactions")
    private int receivedTransactions;

    @SerializedName("sent_transactions")
    private int sentTransactions;

    @SerializedName("invalid_transactions")
    private int invalidTransactions;

    private long timestamp;


    public String toJsonString() {
        return new StringBuilder("{")
                .append("\"transaction\":")
                .append(transaction)
                .append(",")
                .append("\"type\":")
                .append(type)
                .append(",")
                .append("\"status\":")
                .append(status)
                .append(",")
                .append("\"amount\":")
                .append(amount)
                .append(",")
                .append("\"balance\":")
                .append(balance)
                .append(",")
                .append("\"received_transactions\":")
                .append(receivedTransactions)
                .append(",")
                .append("\"sent_transactions\":")
                .append(sentTransactions)
                .append(",")
                .append("\"invalidTransactions\":")
                .append(invalidTransactions)
                .append(",")
                .append("\"timestamp\":")
                .append(timestamp)
                .append("}")
                .toString();
    }
}