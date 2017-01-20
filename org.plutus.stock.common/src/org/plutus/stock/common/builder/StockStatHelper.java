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
import org.plutus.stock.model.runtime.StockStat;

public class StockStatHelper {

	public static StockStatHelper INSTANCE = new StockStatHelper();

	/**
	 * Get the stock stat file.
	 * 
	 * /<stock_home/>/data/<symbol>/<symbol>_daily.stockstat
	 * 
	 * /<stock_home/>/data/<symbol>/<symbol>_weekly.stockstat
	 * 
	 * /<stock_home/>/data/<symbol>/<symbol>_monthly.stockstat
	 * 
	 * /<stock_home/>/data/<symbol>/<symbol>_yearly.stockstat
	 * 
	 * @param symbol
	 * @param type
	 * @return
	 */
	public Path getStockStatFile(String symbol, String type) {
		type = checkType(type);
		String suffix = getFileSuffix(type);

		Path stockHome = SetupUtil.getStockHome();
		String stockFolderName = symbol;

		String stockStatFileName = symbol + "_" + suffix + StockConstants.STOCK_STAT_DOT_FILE_EXTENSION;
		return SetupUtil.getDataFolder(stockHome).resolve(stockFolderName).resolve(stockStatFileName);
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	protected String checkType(String type) {
		if (!StockConstants.TYPE_DAILY.equalsIgnoreCase(type) //
				&& !StockConstants.TYPE_WEEKLY.equalsIgnoreCase(type) //
				&& !StockConstants.TYPE_MONTHLY.equalsIgnoreCase(type) //
				&& !StockConstants.TYPE_YEARLY.equalsIgnoreCase(type)) {
			return StockConstants.TYPE_DAILY;
		}
		return type;
	}

	protected String getFileSuffix(String type) {
		String suffix = null;
		if (StockConstants.TYPE_DAILY.equalsIgnoreCase(type)) {
			suffix = "daily";

		} else if (StockConstants.TYPE_WEEKLY.equalsIgnoreCase(type)) {
			suffix = "weekly";

		} else if (StockConstants.TYPE_MONTHLY.equalsIgnoreCase(type)) {
			suffix = "monthly";

		} else if (StockConstants.TYPE_YEARLY.equalsIgnoreCase(type)) {
			suffix = "yearly";

		} else {
			suffix = "daily";
		}
		return suffix;
	}

	/**
	 * Get stock stat file from stock stat list.
	 * 
	 * @param stockStatList
	 */
	public File getStockStatFile(List<StockStat> stockStatList) {
		File file = null;
		if (stockStatList != null) {
			for (StockStat stockStat : stockStatList) {
				File currFile = getStockStatFile(stockStat);
				if (currFile != null) {
					file = currFile;
					break;
				}
			}
		}
		return file;
	}

	/**
	 * Get stock stat file from stock stat.
	 * 
	 * @param stockStat
	 */
	public File getStockStatFile(StockStat stockStat) {
		File file = null;
		if (stockStat != null) {
			WorkingCopy workingCopy = WorkingCopyUtil.getWorkingCopy(stockStat);
			if (workingCopy != null) {
				URI uri = workingCopy.getURI();
				file = new File(uri);
			}
		}
		return file;
	}

	/**
	 * Get stock stat list from working copy.
	 * 
	 * @param symbol
	 * @return
	 * @throws IOException
	 */
	public List<StockStat> getStockStat(String symbol, String type, Comparator<StockStat> comparator) throws IOException {
		List<StockStat> stockStatList = null;
		Path path = getStockStatFile(symbol, type);
		if (path != null) {
			WorkingCopy workingCopy = WorkingCopyUtil.getWorkingCopy(path.toFile());
			if (workingCopy != null) {
				stockStatList = workingCopy.getContents(StockStat.class);
			}
		}
		if (stockStatList == null) {
			stockStatList = new ArrayList<StockStat>();
		}
		if (comparator != null) {
			Collections.sort(stockStatList, comparator);
		}
		return stockStatList;
	}

	/**
	 * Convert stock stat list to stock stat map.
	 * 
	 * @param stockStatList
	 * @return
	 */
	public Map<Date, StockStat> listToMap(List<StockStat> stockStatList) {
		Map<Date, StockStat> stockStatMap = new HashMap<Date, StockStat>();
		if (stockStatList != null) {
			for (StockStat stockStat : stockStatList) {
				stockStatMap.put(stockStat.getDate(), stockStat);
			}
		}
		return stockStatMap;
	}

	/**
	 * Save stock stat list to local file.
	 * 
	 * @param stockStatList
	 * @param file
	 * @throws IOException
	 */
	public void save(List<StockStat> stockStatList, File file) throws IOException {
		if (stockStatList == null || file == null) {
			return;
		}
		WorkingCopy workingCopy = WorkingCopyUtil.getWorkingCopy(file);
		if (workingCopy != null) {
			Resource resource = workingCopy.getResource();
			if (resource != null) {
				resource.getContents().clear();
				if (stockStatList != null) {
					resource.getContents().addAll(stockStatList);
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
