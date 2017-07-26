package com.humaniq.apilib.network.models.request.blockchain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gritsay on 7/26/17.
 */

public class TransferRequest {

  @SerializedName("from_id")
  private String fromUserId;

  @SerializedName("to_id")
  private String toUserId;

  @SerializedName("amount")
  private int amount;

  public TransferRequest(String fromUserId, String toUserId, int amount) {
    this.fromUserId = fromUserId;
    this.toUserId = toUserId;
    this.amount = amount;
  }

  public String getFromUserId() {
    return fromUserId;
  }

  public void setFromUserId(String fromUserId) {
    this.fromUserId = fromUserId;
  }

  public String getToUserId() {
    return toUserId;
  }

  public void setToUserId(String toUserId) {
    this.toUserId = toUserId;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }
}
