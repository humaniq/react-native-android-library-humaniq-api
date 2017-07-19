package com.humaniq.apilib.models;

/**
 *
 */

import com.google.gson.annotations.SerializedName;

public class UserTransaction {



  @SerializedName("type")
  private int type;

  @SerializedName("status")
  private int status;

  @SerializedName("timestamp")
  private long timestamp;

  @SerializedName("amount")
  private long amount;

  @SerializedName("from_address")
  private String fromAddress;

  @SerializedName("to_address")
  private String toAddress;


  @SerializedName("from_user")
  private User fromUser;

  @SerializedName("to_user")
  private User toUser;


  //public String toJsonString() {
  //  return new StringBuilder("{").append("\"transaction\":")
  //      .append(transaction)
  //      .append(",")
  //      .append("\"type\":")
  //      .append(type)
  //      .append(",")
  //      .append("\"status\":")
  //      .append(status)
  //      .append(",")
  //      .append("\"amount\":")
  //      .append(amount)
  //      .append(",")
  //      .append("\"balance\":")
  //      .append(balance)
  //      .append(",")
  //      .append("\"received_transactions\":")
  //      .append(receivedTransactions)
  //      .append(",")
  //      .append("\"sent_transactions\":")
  //      .append(sentTransactions)
  //      .append(",")
  //      .append("\"invalidTransactions\":")
  //      .append(invalidTransactions)
  //      .append(",")
  //      .append("\"timestamp\":")
  //      .append(timestamp)
  //      .append("}")
  //      .toString();
  //}
}