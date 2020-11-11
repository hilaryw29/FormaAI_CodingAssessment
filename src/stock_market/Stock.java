package stock_market;

import java.util.Date;

public class Stock {
	protected double buyPrice;
	protected final String ticker;
	protected int shares;
	
	public Stock (double buyPrice, String ticker, int shares) {
		this.buyPrice = buyPrice;
		this.ticker = ticker;
		this.shares = shares;
	}
	
	public int getShares () {
		return shares;
	}
	
	public double getBuyPrice() {
		return buyPrice;
	}
	
	public String getTicker () {
		return ticker;
	}
	
	public void buyShares (double pricePaid, int sharesBrought) {
		buyPrice = (shares*buyPrice + sharesBrought*pricePaid)/(shares + sharesBrought);
		shares += sharesBrought;
	}
	
	public void sellShares (int sharesSold) {
		shares -= sharesSold;
	}
	
	public void splitShares (double split) {
		shares = (int) (shares * split);
		buyPrice = buyPrice / split;
	}
}
