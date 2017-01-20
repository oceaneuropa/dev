package org.plutus.stock.common.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SetupUtil {

	public static final String STOCK_HOME = "STOCK_HOME";
	public static final String DATA_FOLDER = "data";

	private static Path stockHome;

	/**
	 * 
	 * @return
	 */
	public static final Path getStockHome() {
		if (stockHome == null) {
			String stockHomeLocation = System.getProperty(STOCK_HOME);
			if (stockHomeLocation == null) {
				stockHomeLocation = System.getenv(STOCK_HOME);
			}
			if (stockHomeLocation == null) {
				throw new RuntimeException("$STOCK_HOME is not set");
			}

			stockHome = Paths.get(stockHomeLocation);
		}
		return stockHome;
	}

	/**
	 * 
	 * @param stockDataHome
	 * @return
	 */
	public static Path getDataFolder(Path stockDataHome) {
		return stockDataHome.resolve(DATA_FOLDER);
	}

}
