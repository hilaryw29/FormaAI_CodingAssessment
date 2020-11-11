package stock_market;

import java.util.Date;
import java.util.HashMap;

public class Portfolio {
	protected HashMap <String, Stock> stocks;
	protected double dividendIncome;
	
	public Portfolio () {
		this.stocks = new HashMap<String, Stock>();
		this.dividendIncome = 0;
	}
	
	public void buyStock (String ticker, double buyPrice, int shares) {
		if (stocks.containsKey(ticker)) {
			stocks.get(ticker).buyShares(buyPrice, shares);
		} else {
			stocks.put(ticker, new Stock (buyPrice, ticker, shares));
		}
	}
	
	// Check to make sure we'll only have valid entries (no selling non-existent stock or shares > num owned)
	public double sellStock (String ticker, double sellPrice, int shares) {
		Stock soldStock = stocks.get(ticker);
		
		// Stock exists in our current portfolio and sold shares is within our existing holding
		if (soldStock != null && shares <= soldStock.getShares()) {
			double profit = sellPrice*shares - soldStock.buyPrice*shares;
			soldStock.sellShares(shares);
			if (soldStock.getShares() == 0) {
				stocks.remove(ticker);
			}
			return profit;
		}
		return 0;
	}
	
	public boolean checkSplit (String ticker, double split) {
		Stock splitStock = stocks.get(ticker);
		
		if (splitStock != null) {
			splitStock.splitShares(split);
			return true;
		}
		return false;
	}
	
	public boolean checkDividend (String ticker, double dividend) {
		Stock divStock = stocks.get(ticker);
		
		if (divStock != null) {
			dividendIncome += dividend*divStock.getShares();
			return true;
		}
		return false;
	}
	
	public void printStatus (String date) {
		System.out.println("On " + date + ", you have:");
		for (Stock stock : stocks.values()) {
			System.out.printf("%4s- %d shares of %s at $%.2f per share\n", "", stock.getShares(), stock.ticker, stock.getBuyPrice());
		}
		System.out.printf("%4s- $%.2f of dividend income\n", "", dividendIncome);
	}
}
