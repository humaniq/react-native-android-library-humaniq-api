package com.humaniq.apilib.network.models.request.wallet;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ognev on 7/19/17.
 */

public class Balance {

  @SerializedName("token") private Currency tokenCurrency;

  @SerializedName("default") private Currency defaultCurrency;

  @SerializedName("local") private Currency localCurrency;

  public Currency getTokenCurrency() {
    return tokenCurrency;
  }

  public Currency getDefaultCurrency() {
    return defaultCurrency;
  }

  public Currency getLocalCurrency() {
    return localCurrency;
  }

  private class Currency {
    @SerializedName("currency")
    private String currency;

    @SerializedName("amount")
    private String amount;
  }


}
