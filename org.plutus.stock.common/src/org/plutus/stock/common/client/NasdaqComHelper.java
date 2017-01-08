package org.plutus.stock.common.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.origin.common.rest.client.ClientException;
import org.plutus.stock.common.model.StockCategory;
import org.plutus.stock.common.model.StockCompany;
import org.plutus.stock.common.model.StockSector;
import org.plutus.stock.common.util.Comparators;

public class NasdaqComHelper {

	public static NasdaqComHelper INSTANCE = new NasdaqComHelper();

	/**
	 * Extract stock category data from stock companies data.
	 * 
	 * @param comparator
	 * @return
	 * @throws ClientException
	 */
	@SuppressWarnings("unchecked")
	public List<StockCategory> extractStockCategories(NasdaqComClient client, Comparator<StockCategory> comparator) throws ClientException {
		List<StockCompany> stockCompaniesNASDAQ = client.getStockCompanies(NasdaqComClient.EXCHANGE_NASDAQ, Comparators.StockCompanyComparator.ASC);
		List<StockCompany> stockCompaniesNYSE = client.getStockCompanies(NasdaqComClient.EXCHANGE_NYSE, Comparators.StockCompanyComparator.ASC);
		List<StockCompany> stockCompaniesAMEX = client.getStockCompanies(NasdaqComClient.EXCHANGE_AMEX, Comparators.StockCompanyComparator.ASC);
		return extractStockCategories(comparator, stockCompaniesNASDAQ, stockCompaniesNYSE, stockCompaniesAMEX);
	}

	/**
	 * Extract stock category data from stock companies data.
	 * 
	 * @param comparator
	 * @param stockCompaniesArray
	 * @return
	 * @throws ClientException
	 */
	@SuppressWarnings("unchecked")
	public List<StockCategory> extractStockCategories(Comparator<StockCategory> comparator, List<StockCompany>... stockCompaniesArray) throws ClientException {
		List<StockCategory> stockCategories = new ArrayList<StockCategory>();
		for (List<StockCompany> stockCompanies : stockCompaniesArray) {
			appendToStockCategories(stockCompanies, stockCategories);
		}
		if (comparator != null) {
			Collections.sort(stockCategories, comparator);
		}
		return stockCategories;
	}

	/**
	 * 
	 * @param stockCompanies
	 * @param stockCategories
	 */
	protected void appendToStockCategories(List<StockCompany> stockCompanies, List<StockCategory> stockCategories) {
		if (stockCompanies != null && stockCategories != null) {
			for (StockCompany company : stockCompanies) {
				String sector = company.getSector();
				String industry = company.getIndustry();

				StockCategory stockCategory = new StockCategory(sector, industry);
				if (!stockCategories.contains(stockCategory)) {
					stockCategories.add(stockCategory);
				}
			}
		}
	}

	/**
	 * Convert a list of StockCategoryData objects to a list of StockSector objects.
	 * 
	 * @param categoryDataList
	 * @return
	 */
	public List<StockSector> convertToStockSectors(List<StockCategory> categoryDataList, Comparator<StockSector> comparator) {
		List<StockSector> stockSectors = new ArrayList<StockSector>();

		if (categoryDataList == null || categoryDataList.isEmpty()) {
			return stockSectors;
		}

		List<String> sectors = new ArrayList<String>();
		Map<String, StockSector> sectorsMap = new HashMap<String, StockSector>();

		for (StockCategory categoryData : categoryDataList) {
			String sector = categoryData.getSector();
			String industry = categoryData.getIndustry();

			if (!sectors.contains(sector)) {
				sectors.add(sector);
			}

			StockSector sectorObj = sectorsMap.get(sector);
			if (sectorObj == null) {
				sectorObj = new StockSector(sector);
				sectorsMap.put(sector, sectorObj);
			}

			if (!sectorObj.containsIndustry(industry)) {
				sectorObj.getIndustries().add(industry);
			}
		}

		for (String sector : sectors) {
			StockSector sectorObj = sectorsMap.get(sector);
			sectorObj.sortIndustries();
			stockSectors.add(sectorObj);
		}

		if (comparator != null) {
			Collections.sort(stockSectors, comparator);
		}

		return stockSectors;
	}

	/**
	 * Filter stock companies by sector.
	 * 
	 * @param stockCompanies
	 * @param sector
	 * @return
	 */
	public List<StockCompany> filterStockCompanies(List<StockCompany> stockCompanies, String sector) {
		List<StockCompany> resultStockCompanies = new ArrayList<StockCompany>();
		for (StockCompany stockCompany : stockCompanies) {
			String currSector = stockCompany.getSector();
			if (sector.equals(currSector)) {
				resultStockCompanies.add(stockCompany);
			}
		}
		return resultStockCompanies;
	}

	/**
	 * Filter stock companies by sector and industry.
	 * 
	 * @param stockCompanies
	 * @param sector
	 * @param industry
	 * @return
	 */
	public List<StockCompany> filterStockCompanies(List<StockCompany> stockCompanies, String sector, String industry) {
		List<StockCompany> resultStockCompanies = new ArrayList<StockCompany>();
		for (StockCompany stockCompany : stockCompanies) {
			String currSector = stockCompany.getSector();
			String currIndustry = stockCompany.getIndustry();
			if (sector.equals(currSector) && industry.equals(currIndustry)) {
				resultStockCompanies.add(stockCompany);
			}
		}
		return resultStockCompanies;
	}

	/**
	 * 
	 * @param stockCompanies
	 * @return
	 */
	public String getStockSymbols(List<StockCompany> stockCompanies) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < stockCompanies.size(); i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(stockCompanies.get(i).getSymbol());
		}
		return sb.toString();
	}

}
