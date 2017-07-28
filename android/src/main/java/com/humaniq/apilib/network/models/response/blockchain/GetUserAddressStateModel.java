package com.humaniq.apilib.network.models.response.blockchain;

/**
 * Created by gritsay on 7/28/17.
 */

public class GetUserAddressStateModel {

  private String userId;
  private String status;
  private String currency;
  private String backupCurrency;
  private String address;
  private String balance;
  private String availableBalance;
  private String pending;
  private String received;
  private String sent;
  private String serviceCharge;
  private String signupTimestampIso;
  private String sentTransactions;
  private String receivedTransactions;
  private String pendingReceivedTransactions;
  private String invalidTransactions;
  private String totalTransactions;
  private String receivedBonusTransactions;
  private String availableBonus;
  private String usedBonus;
  private String rating;
  private String percentage;

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

  public String getSignupTimestampIso() {
    return signupTimestampIso;
  }

  public void setSignupTimestampIso(String signupTimestampIso) {
    this.signupTimestampIso = signupTimestampIso;
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
