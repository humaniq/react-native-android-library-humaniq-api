package com.humaniq.apilib.models.response;

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
}