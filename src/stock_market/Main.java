package stock_market;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Main {
	public static void main (String [] args) {
		// Original Test Cases
		String [][] actions = {{"1992/07/14 11:12:30", "BUY", "12.3", "AAPL", "500"}, {"1992/09/13 11:15:20", "SELL", "15.3", "AAPL", "100"}, {"1992/10/14 15:14:20", "BUY", "20", "MSFT", "300"}, {"1992/10/17 16:14:30", "SELL", "20.2", "MSFT", "200"}, {"1992/10/19 15:14:20", "BUY", "21", "MSFT", "500"}, {"1992/10/23 16:14:30", "SELL", "18.2", "MSFT", "600"}, {"1992/10/25 10:15:20", "SELL", "20.3", "AAPL", "300"}, {"1992/10/25 16:12:10", "BUY", "18.3", "MSFT", "500"}};
		String [][] stock_actions = {{"1992/08/14", "0.10", "", "AAPL"}, {"1992/09/01", "", "3", "AAPL"}, {"1992/10/15", "0.20", "", "MSFT"},{"1992/10/16", "0.20", "", "ABC"}};
		
		// Additional Tests
		//String [][] actions = {{"1992/07/14 11:12:30", "BUY", "12.3", "AAPL", "500"}};
		//String [][] actions = {};
		//String [][] actions = {{"1992/07/14 11:12:30", "BUY", "12.3", "AAPL", "500"} , {"1992/07/14 11:15:20", "SELL", "15.3", "AAPL", "100"}, {"1992/10/14 15:14:20", "BUY", "20", "MSFT", "300"}, {"1992/10/17 16:14:30", "SELL", "20.2", "MSFT", "200"}};
		//String [][] actions = {{"1992/07/14 11:12:30", "BUY", "12.3", "AAPL", "500"} , {"1992/07/14 11:15:20", "SELL", "15.3", "AAPL", "100"}, {"1992/07/14 15:14:20", "BUY", "20", "MSFT", "300"}, {"1992/07/14 16:14:30", "SELL", "20.2", "MSFT", "200"}};
		//String [][] stock_actions = {{"1992/11/14", "0.10", "", "AAPL"}};
		//String [][] stock_actions = {};
		//String [][] stock_actions = {{"1992/06/14", "0.10", "", "AAPL"}, {"1992/07/01", "", "3", "AAPL"}, {"1992/10/15", "0.20", "", "MSFT"},{"1992/10/16", "0.20", "", "ABC"}};
		
		Portfolio my_portfolio = new Portfolio();
		SimpleDateFormat compare = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		int i_act1 = 0, i_act2 = 1, i_stock_act = 0;
		int act_length =  actions.length, stock_act_length = stock_actions.length;
		Date dateAct1, dateAct2, dateStockAct;
		LinkedList<String> transactions = new LinkedList<String>();
		
		while (i_stock_act != stock_act_length && i_act1 != act_length) {
			try {
				dateAct1 = compare.parse(actions[i_act1][0]);
				dateStockAct = compare.parse(stock_actions[i_stock_act][0]);

				if (dateAct1.compareTo(dateStockAct) <= 0) {
					// Execute Action 1
					transactions.add(execute_actions(my_portfolio, format.format(dateAct1), actions[i_act1][1], actions[i_act1][2],
							actions[i_act1][3], actions[i_act1][4]));
					i_act1++;
					
					// Execute only if we have a valid pointer/index for Action 2
					if (i_act2 <= act_length - 1){
						dateAct2 = compare.parse(actions[i_act2][0]);
						
						// If there are multiple actions on the same day
						while (dateAct1.compareTo(dateAct2) == 0 && i_act2 != act_length) {
							transactions.add(execute_actions(my_portfolio, format.format(dateAct2), actions[i_act2][1], actions[i_act2][2],
									actions[i_act2][3], actions[i_act2][4]));
							
							i_act2++;
							if (i_act2 < act_length - 1) {
								dateAct2 = compare.parse(actions[i_act2][0]);
							} 
						}
						i_act1 = i_act2;
						i_act2 = i_act1 + 1;
					} 
					
					// If we have multiple actions AND a stock action on the same day
					if (dateAct1.compareTo(dateStockAct) == 0) {
						transactions.add(execute_stock_actions(my_portfolio, stock_actions[i_stock_act][0], stock_actions[i_stock_act][1],
								stock_actions[i_stock_act][2], stock_actions[i_stock_act][3]));
						
						if (i_stock_act <= stock_act_length - 1) {
							i_stock_act++;
						}
					}
					
					printTransactions(my_portfolio, transactions, format.format(dateAct1));
					
				} else {
					transactions.add(execute_stock_actions(my_portfolio, format.format(dateStockAct), stock_actions[i_stock_act][1],
							stock_actions[i_stock_act][2], stock_actions[i_stock_act][3]));

					if (i_stock_act <= stock_act_length - 1) {
						i_stock_act++;
					}
					
					printTransactions(my_portfolio, transactions, format.format(dateStockAct));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// Once we've reached the end of action or stock_actions and no longer have to compare the two arrays. We execute the remaining elements individually.
		while (i_act1 != act_length) {
			try {
				dateAct1 = compare.parse(actions[i_act1][0]);
				
				transactions.add(execute_actions(my_portfolio, format.format(dateAct1), actions[i_act1][1], actions[i_act1][2],
						actions[i_act1][3], actions[i_act1][4]));
				i_act1++;
				
				if (i_act2 <= act_length - 1){
					dateAct2 = compare.parse(actions[i_act2][0]);
					
					while (dateAct1.compareTo(dateAct2) == 0 && i_act2 != act_length) {
						transactions.add(execute_actions(my_portfolio, format.format(dateAct2), actions[i_act2][1], actions[i_act2][2],
								actions[i_act2][3], actions[i_act2][4]));
						
						i_act2++;
						if (i_act2 < act_length - 1) {
							dateAct2 = compare.parse(actions[i_act2][0]);
						} 
					}	
					i_act1 = i_act2;
					i_act2 = i_act1 + 1;
				} 
								
				printTransactions(my_portfolio, transactions, format.format(dateAct1));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		while (i_stock_act != stock_act_length) {
			try {
				dateStockAct = compare.parse(stock_actions[i_stock_act][0]);
				
				transactions.add(execute_stock_actions(my_portfolio, format.format(dateStockAct), stock_actions[i_stock_act][1],
						stock_actions[i_stock_act][2], stock_actions[i_stock_act][3]));

				if (i_stock_act <= stock_act_length - 1) {
					i_stock_act++;
				}
				
				printTransactions(my_portfolio, transactions, format.format(dateStockAct));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void printTransactions(Portfolio portfolio, LinkedList<String> transactions, String date) {
		if (transactions.peek() != null) {
			portfolio.printStatus(date);
			System.out.printf("%2sTransactions:\n", "");
			for (String str : transactions) {
				if (str != null) {
					System.out.print(str);
				}
			}
		}
		
		transactions.clear();
	}

	private static String execute_actions (Portfolio portfolio, String date, String action, String strPrice, String ticker, String strShares) {
		double price = Double.parseDouble(strPrice);
		int shares = Integer.parseInt(strShares);
		
		if (action.equalsIgnoreCase("BUY")) {
			portfolio.buyStock(ticker, price, shares);
			return String.format("%4s- You bought %s shares of %s at a price of $%.2f per share\n", "", strShares, ticker, price);
		} else if (action.equalsIgnoreCase("SELL")) {
			double profit = portfolio.sellStock(ticker, price, shares);
			if (profit < 0) {
				return String.format("%4s- You sold %s shares of %s at a price of $%.2f per share for a loss of $%.2f\n", "", strShares, ticker, price, profit);
			} else {
				return String.format("%4s- You sold %s shares of %s at a price of $%.2f per share for a profit of $%.2f\n", "", strShares, ticker, price, profit);
			}
		}
		return String.format("%4s- Error, action %s could not be found", "", action);
	}
	
	private static String execute_stock_actions (Portfolio portfolio, String date, String strDiv, String strSplit, String ticker) {
		
		if (!strDiv.equals("")) {
			if (portfolio.checkDividend(ticker, Double.parseDouble(strDiv))) {
				return String.format("%4s- %s paid out $%s dividend per share, and you have %d shares\n", "", ticker, strDiv, portfolio.stocks.get(ticker).getShares());
			}
		} else if (!strSplit.equals("")) {
			if (portfolio.checkSplit(ticker, Double.parseDouble(strSplit))) {
				return String.format("%4s- %s split %s to 1, and you have %d shares\n", "", ticker, strSplit, portfolio.stocks.get(ticker).getShares());
			}
		}
		return null;
	}
}
