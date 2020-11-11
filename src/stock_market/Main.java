package stock_market;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
	public static void main (String [] args) {
		String [][] actions = {{"1992/07/14 11:12:30", "BUY", "12.3", "AAPL", "500"}, {"1992/09/13 11:15:20", "SELL", "15.3", "AAPL", "100"}, {"1992/10/14 15:14:20", "BUY", "20", "MSFT", "300"}, {"1992/10/17 16:14:30", "SELL", "20.2", "MSFT", "200"}, {"1992/10/19 15:14:20", "BUY", "21", "MSFT", "500"}, {"1992/10/23 16:14:30", "SELL", "18.2", "MSFT", "600"}, {"1992/10/25 10:15:20", "SELL", "20.3", "AAPL", "300"}, {"1992/10/25 16:12:10", "BUY", "18.3", "MSFT", "500"}};
		//String [][] actions = {{"1992/07/14 11:12:30", "BUY", "12.3", "AAPL", "500"}};
		String [][] stock_actions = {{"1992/08/14", "0.10", "", "AAPL"}, {"1992/09/01", "", "3", "AAPL"}, {"1992/10/15", "0.20", "", "MSFT"},{"1992/10/16", "0.20", "", "ABC"}};
	
		Portfolio my_portfolio = new Portfolio();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		
		int i_act1 = 0, i_act2 = 1, i_stock_act = 0;
		int act_length =  actions.length, stock_act_length = stock_actions.length;
		Date dateAct1, dateAct2, dateStockAct;
		
		while (i_stock_act != stock_act_length && i_act1 != act_length && i_act2 <= act_length - 1) {
			try {
				dateAct1 = format.parse(actions[i_act1][0]);
				dateAct2 = format.parse(actions[i_act2][0]);
				dateStockAct = format.parse(stock_actions[i_stock_act][0]);

				// If date1 occurs before date2 or if the two dates are equal
				if (dateAct1.compareTo(dateStockAct) < 0 || dateAct1.compareTo(dateStockAct) == 0) {
					execute_actions(my_portfolio, format.format(dateAct1), actions[i_act1][1], actions[i_act1][2],
							actions[i_act1][3], actions[i_act1][4]);
					while (dateAct1.compareTo(dateAct2) == 0) {
						execute_actions(my_portfolio, format.format(dateAct2), actions[i_act2][1], actions[i_act2][2],
								actions[i_act2][3], actions[i_act2][4]);
						
						if (i_act2 < act_length - 1) {
							i_act2++;
							dateAct2 = format.parse(actions[i_act2][0]);
						}
					}
					i_act1 = i_act2;
					i_act2 = i_act1 + 1;
				} else {
					execute_stock_actions(my_portfolio, format.format(dateStockAct), stock_actions[i_stock_act][1],
							stock_actions[i_stock_act][2], stock_actions[i_stock_act][3]);
					if (i_stock_act <= stock_act_length - 1) {
						i_stock_act++;
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		while (i_act1 != act_length && i_act2 <= act_length - 1) {
//			try {
//				dateAct1 = format.parse(actions[i_act1][0]);
//				dateAct2 = format.parse(actions[i_act2][0]);
//				
//				while (dateAct1.compareTo(dateAct2) == 0) {
//					execute_actions(my_portfolio, format.format(dateAct2), actions[i_act2][1], actions[i_act2][2],
//							actions[i_act2][3], actions[i_act2][4]);
//					
//					if (i_act2 < act_length - 1) {
//						i_act2++;
//						dateAct2 = format.parse(actions[i_act2][0]);
//					}
//				}
//				i_act1 = i_act2;
//				i_act2 = i_act1 + 1;
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
			
			execute_actions(my_portfolio, actions[i_act1][0].substring(0, 10), actions[i_act1][1], actions[i_act1][2],
					actions[i_act1][3], actions[i_act1][4]);
			if (i_act1 <= act_length - 1) {
				i_act1++;
			}
		}
		while (i_stock_act != stock_act_length) {
			execute_stock_actions(my_portfolio, stock_actions[i_stock_act][0].substring(0, 10), stock_actions[i_stock_act][1],
					stock_actions[i_stock_act][2], stock_actions[i_stock_act][3]);
			if (i_stock_act <= stock_act_length - 1) {
				i_stock_act++;
			}
		}
	}
	
	public static void execute_actions (Portfolio portfolio, String date, String action, String strPrice, String ticker, String strShares) {
		double price = Double.parseDouble(strPrice);
		int shares = Integer.parseInt(strShares);
		
		if (action.equalsIgnoreCase("BUY")) {
			portfolio.buyStock(ticker, price, shares);
			portfolio.printStatus(date);
			System.out.printf("%2sTransactions:\n", "");
			System.out.printf("%4s- You bought %s shares of %s at a price of $%.2f per share\n", "", strShares, ticker, price);
		} else if (action.equalsIgnoreCase("SELL")) {
			double profit = portfolio.sellStock(ticker, price, shares);
			portfolio.printStatus(date);
			System.out.printf("%2sTransactions:\n", "");
			if (profit < 0) {
				System.out.printf("%4s- You sold %s shares of %s at a price of $%.2f per share for a loss of $%.2f\n", "", strShares, ticker, price, profit);
			} else {
				System.out.printf("%4s- You sold %s shares of %s at a price of $%.2f per share for a profit of $%.2f\n", "", strShares, ticker, price, profit);
			}
		}
	}
	
	public static void execute_stock_actions (Portfolio portfolio, String date, String strDiv, String strSplit, String ticker) {
		
		if (!strDiv.equals("")) {
			if (portfolio.checkDividend(ticker, Double.parseDouble(strDiv))) {
				portfolio.printStatus(date);
				System.out.printf("%2sTransactions:\n", "");
				System.out.printf("%4s- %s paid out $%s dividend per share, and you have %d shares\n", "", ticker, strDiv, portfolio.stocks.get(ticker).getShares());
			}
		} else if (!strSplit.equals("")) {
			if (portfolio.checkSplit(ticker, Double.parseDouble(strSplit))) {
				portfolio.printStatus(date);
				System.out.printf("%2sTransactions:\n", "");
				System.out.printf("%4s- %s split %s to 1, and you have %d shares\n", "", ticker, strSplit, portfolio.stocks.get(ticker).getShares());
			}
		}
	}
}
