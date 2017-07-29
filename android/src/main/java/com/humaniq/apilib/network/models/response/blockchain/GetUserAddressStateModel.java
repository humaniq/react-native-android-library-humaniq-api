package com.humaniq.apilib.network.models.response.blockchain;

import com.facebook.common.internal.DoNotStrip;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gritsay on 7/28/17.
 */

public class GetUserAddressStateModel {

  @SerializedName("223344556677") private String userId;
  @SerializedName("status") private String status;
  @SerializedName("currency") private String currency;
  @SerializedName("backup_currency") private String backupCurrency;
  @SerializedName("address") private String address;
  @SerializedName("balance") private String balance;
  @SerializedName("available_balance") private String availableBalance;
  @SerializedName("pending") private String pending;
  @SerializedName("gwei") private String gwei;
  @SerializedName("received") private String received;
  @SerializedName("sent") private String sent;
  @SerializedName("service_charge") private String serviceCharge;
  @SerializedName("signup_timestamp") private String signupTimestamp;
  @SerializedName("sent_tx") private String sentTransactions;
  @SerializedName("received_tx") private String receivedTransactions;
  @SerializedName("pending_sent_tx") private String pendingSentTransactions;
  @SerializedName("pending_received_tx") private String pendingReceivedTransactions;
  @SerializedName("invalid_tx") private String invalidTransactions;
  @SerializedName("total_tx") private String totalTransactions;
  @SerializedName("received_bonus_tx") private String receivedBonusTransactions;
  @SerializedName("available_bonus") private String availableBonus;
  @SerializedName("used_bonus") private String usedBonus;
  @SerializedName("rating") private String rating;
  @SerializedName("percentage") private String percentage;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getBackupCurrency() {
    return backupCurrency;
  }

  public void setBackupCurrency(String backupCurrency) {
    this.backupCurrency = backupCurrency;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getBalance() {
    return balance;
  }

  public void setBalance(String balance) {
    this.balance = balance;
  }

  public String getAvailableBalance() {
    return availableBalance;
  }

  public void setAvailableBalance(String availableBalance) {
    this.availableBalance = availableBalance;
  }

  public String getPending() {
    return pending;
  }

  public void setPending(String pending) {
    this.pending = pending;
  }

  public String getGwei() {
    return gwei;
  }

  public void setGwei(String gwei) {
    this.gwei = gwei;
  }

  public String getReceived() {
    return received;
  }

  public void setReceived(String received) {
    this.received = received;
  }

  public String getSent() {
    return sent;
  }

  public void setSent(String sent) {
    this.sent = sent;
  }

  public String getServiceCharge() {
    return serviceCharge;
  }

  public void setServiceCharge(String serviceCharge) {
    this.serviceCharge = serviceCharge;
  }

  public String getSignupTimestamp() {
    return signupTimestamp;
  }

  public void setSignupTimestamp(String signupTimestamp) {
    this.signupTimestamp = signupTimestamp;
  }

  public String getSentTransactions() {
    return sentTransactions;
  }

  public void setSentTransactions(String sentTransactions) {
    this.sentTransactions = sentTransactions;
  }

  public String getReceivedTransactions() {
    return receivedTransactions;
  }

  public void setReceivedTransactions(String receivedTransactions) {
    this.receivedTransactions = receivedTransactions;
  }

  public String getPendingSentTransactions() {
    return pendingSentTransactions;
  }

  public void setPendingSentTransactions(String pendingSentTransactions) {
    this.pendingSentTransactions = pendingSentTransactions;
  }

  public String getPendingReceivedTransactions() {
    return pendingReceivedTransactions;
  }

  public void setPendingReceivedTransactions(String pendingReceivedTransactions) {
    this.pendingReceivedTransactions = pendingReceivedTransactions;
  }

  public String getInvalidTransactions() {
    return invalidTransactions;
  }

  public void setInvalidTransactions(String invalidTransactions) {
    this.invalidTransactions = invalidTransactions;
  }

  public String getTotalTransactions() {
    return totalTransactions;
  }

  public void setTotalTransactions(String totalTransactions) {
    this.totalTransactions = totalTransactions;
  }

  public String getReceivedBonusTransactions() {
    return receivedBonusTransactions;
  }

  public void setReceivedBonusTransactions(String receivedBonusTransactions) {
    this.receivedBonusTransactions = receivedBonusTransactions;
  }

  public String getAvailableBonus() {
    return availableBonus;
  }

  public void setAvailableBonus(String availableBonus) {
    this.availableBonus = availableBonus;
  }

  public String getUsedBonus() {
    return usedBonus;
  }

  public void setUsedBonus(String usedBonus) {
    this.usedBonus = usedBonus;
  }

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public String getPercentage() {
    return percentage;
  }

  public void setPercentage(String percentage) {
    this.percentage = percentage;
  }
}