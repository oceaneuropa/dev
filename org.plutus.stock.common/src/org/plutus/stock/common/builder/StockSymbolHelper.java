package org.plutus.stock.common.builder;

public class StockSymbolHelper {

	public static StockSymbolHelper INSTANCE = new StockSymbolHelper();

	/**
	 * 
	 * @param symbol
	 */
	public String checkSymbol(String symbol) {
		if (symbol == null || symbol.trim().isEmpty()) {
			throw new RuntimeException("symbol is empty.");
		}
		symbol = symbol.trim();
		return symbol;
	}

}
