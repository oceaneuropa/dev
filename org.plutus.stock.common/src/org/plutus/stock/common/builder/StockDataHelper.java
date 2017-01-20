package org.plutus.stock.common.builder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.origin.common.resource.Resource;
import org.origin.common.workingcopy.WorkingCopy;
import org.origin.common.workingcopy.WorkingCopyUtil;
import org.plutus.stock.common.StockConstants;
import org.plutus.stock.common.util.SetupUtil;
import org.plutus.stock.model.runtime.StockData;

public class StockDataHelper {

	public static StockDataHelper INSTANCE = new StockDataHelper();

	/**
	 * Get the /<stock_home/>/data/<symbol>/<symbol>.stockdata file.
	 * 
	 * @param symbol
	 * @return
	 */
	public Path getStockDataFile(String symbol) {
		Path stockDataHome = SetupUtil.getStockHome();
		String stockFolderName = symbol;
		String stockDataFileName = symbol + StockConstants.STOCK_DATA_DOT_FILE_EXTENSION;
		return SetupUtil.getDataFolder(stockDataHome).resolve(stockFolderName).resolve(stockDataFileName);
	}

	/**
	 * Get stock data file from stock data list.
	 * 
	 * @param stockDataList
	 */
	public File getStockDataFile(List<StockData> stockDataList) {
		File file = null;
		if (stockDataList != null) {
			for (StockData stockData : stockDataList) {
				File currFile = getStockDataFile(stockData);
				if (currFile != null) {
					file = currFile;
					break;
				}
			}
		}
		return file;
	}

	/**
	 * Get stock data file from stock data.
	 * 
	 * @param stockData
	 */
	public File getStockDataFile(StockData stockData) {
		File file = null;
		if (stockData != null) {
			WorkingCopy workingCopy = WorkingCopyUtil.getWorkingCopy(stockData);
			if (workingCopy != null) {
				URI uri = workingCopy.getURI();
				file = new File(uri);
			}
		}
		return file;
	}

	/**
	 * Get stock data list from working copy.
	 * 
	 * @param symbol
	 * @return
	 * @throws IOException
	 */
	public List<StockData> getStockData(String symbol, Comparator<StockData> comparator) throws IOException {
		List<StockData> stockDataList = null;
		Path path = getStockDataFile(symbol);
		if (path != null) {
			WorkingCopy workingCopy = WorkingCopyUtil.getWorkingCopy(path.toUri());
			if (workingCopy != null) {
				stockDataList = workingCopy.getContents(StockData.class);
			}
		}
		if (stockDataList == null) {
			stockDataList = new ArrayList<StockData>();
		}
		if (comparator != null) {
			Collections.sort(stockDataList, comparator);
		}
		return stockDataList;
	}

	/**
	 * Convert stock data list to stock data map.
	 * 
	 * @param stockDataList
	 * @return
	 */
	public Map<Date, StockData> listToMap(List<StockData> stockDataList) {
		Map<Date, StockData> stockDataMap = new HashMap<Date, StockData>();
		if (stockDataList != null) {
			for (StockData stockData : stockDataList) {
				stockDataMap.put(stockData.getDate(), stockData);
			}
		}
		return stockDataMap;
	}

	/**
	 * Save stock data list to local file.
	 * 
	 * @param stockDataList
	 * @param file
	 * @throws IOException
	 */
	public void save(List<StockData> stockDataList, File file) throws IOException {
		if (stockDataList == null || file == null) {
			return;
		}
		WorkingCopy workingCopy = WorkingCopyUtil.getWorkingCopy(file);
		if (workingCopy != null) {
			Resource resource = workingCopy.getResource();
			if (resource != null) {
				resource.getContents().clear();
				if (stockDataList != null) {
					resource.getContents().addAll(stockDataList);
				}
			}
			try {
				workingCopy.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
