package com.humaniq.apilib.network.models.request.wallet;

/**
 * Created by gritsay on 7/12/17.
 */

import com.google.gson.annotations.SerializedName;

public class AddressState {
  @SerializedName("user_id") private String userId;

  private int status;

  private int currency;

  @SerializedName("backup_currency") private int backupCurrency;

  private String address;

  private long balance;

  @SerializedName("available_balance") private long availableBalance;

  private int gwei;

  private long received;

  private long sent;

  @SerializedName("service_charge") private int serviceCharche;

  @SerializedName("signup_timestamp") private int signupTimestamp;

  @SerializedName("sent_tx") private int sentTx;

  @SerializedName("received_tx") private int receivedTx;

  @SerializedName("pending_sent_tx") private int pendingSentTx;

  @SerializedName("pending_received_tx") private int pendingReceivedTx;

  @SerializedName("invalid_tx") private int invalidTx;

  @SerializedName("total_tx") private int totalTx;

  @SerializedName("received_bonus_tx") private int receivedBonusTx;

  @SerializedName("available_bonus") private int availableBonus;

  @SerializedName("used_bonus") private int usedBonus;

  @Override public String toString() {
    return super.toString();
  }
}

