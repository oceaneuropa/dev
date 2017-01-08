package org.plutus.stock.common.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.rest.client.ClientException;
import org.plutus.stock.common.client.NasdaqComClient;
import org.plutus.stock.common.client.NasdaqComHelper;
import org.plutus.stock.common.model.StockCategory;
import org.plutus.stock.common.model.StockCompany;
import org.plutus.stock.common.model.StockSector;
import org.plutus.stock.common.util.Comparators;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NasdaqComClientTest {

	protected NasdaqComClient nasdaqComClient;

	public NasdaqComClientTest() {
		this.nasdaqComClient = getClient();
	}

	protected void setUp() {
		this.nasdaqComClient = getClient();
	}

	protected NasdaqComClient getClient() {
		return new NasdaqComClient();
	}

	@Ignore
	@Test
	public void test001_listStockCompanies() throws IOException {
		System.out.println("--- --- --- test001_listStockCompanies() --- --- ---");

		try {
			List<StockCompany> stockCompaniesNASDAQ = this.nasdaqComClient.getStockCompanies(NasdaqComClient.EXCHANGE_NASDAQ, Comparators.StockCompanyComparator.ASC);
			System.out.println("stockCompaniesNASDAQ.size() = " + stockCompaniesNASDAQ.size());
			for (StockCompany stockCompany : stockCompaniesNASDAQ) {
				System.out.println(stockCompany.toString());
			}
			System.out.println();

			List<StockCompany> stockCompaniesNYSE = this.nasdaqComClient.getStockCompanies(NasdaqComClient.EXCHANGE_NYSE, Comparators.StockCompanyComparator.ASC);
			System.out.println("stockCompaniesNYSE.size() = " + stockCompaniesNYSE.size());
			for (StockCompany stockCompany : stockCompaniesNYSE) {
				System.out.println(stockCompany.toString());
			}
			System.out.println();

			List<StockCompany> stockCompaniesAMEX = this.nasdaqComClient.getStockCompanies(NasdaqComClient.EXCHANGE_AMEX, Comparators.StockCompanyComparator.ASC);
			System.out.println("stockCompaniesAMEX.size() = " + stockCompaniesAMEX.size());
			for (StockCompany stockCompany : stockCompaniesAMEX) {
				System.out.println(stockCompany.toString());
			}

		} catch (ClientException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	@Ignore
	@SuppressWarnings("unchecked")
	@Test
	public void test002_listStockCategories_part1() throws IOException {
		System.out.println("--- --- --- test002_listStockCategories_part1() --- --- ---");

		try {
			List<StockCompany> stockCompaniesNASDAQ = this.nasdaqComClient.getStockCompanies(NasdaqComClient.EXCHANGE_NASDAQ, Comparators.StockCompanyComparator.ASC);
			List<StockCompany> stockCompaniesNYSE = this.nasdaqComClient.getStockCompanies(NasdaqComClient.EXCHANGE_NYSE, Comparators.StockCompanyComparator.ASC);
			List<StockCompany> stockCompaniesAMEX = this.nasdaqComClient.getStockCompanies(NasdaqComClient.EXCHANGE_AMEX, Comparators.StockCompanyComparator.ASC);

			List<StockCategory> stockCategories = NasdaqComHelper.INSTANCE.extractStockCategories(Comparators.StockCategoryComparator.ASC, stockCompaniesNASDAQ, stockCompaniesNYSE, stockCompaniesAMEX);
			System.out.println("stockCategories.size() = " + stockCategories.size());
			System.out.println();

			for (StockCategory stockCategory : stockCategories) {
				System.out.println(stockCategory.toString());
			}
			System.out.println();

			List<StockSector> stockSectors = NasdaqComHelper.INSTANCE.convertToStockSectors(stockCategories, Comparators.StockSectorComparator.ASC);
			System.out.println("stockSectors.size() = " + stockSectors.size());
			System.out.println();

			for (StockSector stockSector : stockSectors) {
				// System.out.println(stockSector.toString());

				String sector = stockSector.getSector();
				List<String> industries = stockSector.getIndustries();

				System.out.println("Sector [" + sector + "] (" + industries.size() + " industries)");
				for (String industry : industries) {
					List<StockCompany> filteredStockCompaniesNASDAQ = NasdaqComHelper.INSTANCE.filterStockCompanies(stockCompaniesNASDAQ, sector, industry);
					List<StockCompany> filteredStockCompaniesNYSE = NasdaqComHelper.INSTANCE.filterStockCompanies(stockCompaniesNYSE, sector, industry);
					List<StockCompany> filteredStockCompaniesAMEX = NasdaqComHelper.INSTANCE.filterStockCompanies(stockCompaniesAMEX, sector, industry);

					int filteredCompaniesNum = filteredStockCompaniesNASDAQ.size() + filteredStockCompaniesNYSE.size() + filteredStockCompaniesAMEX.size();

					String filteredSymbolesNASDAQ = NasdaqComHelper.INSTANCE.getStockSymbols(filteredStockCompaniesNASDAQ);
					String filteredSymbolesNYSE = NasdaqComHelper.INSTANCE.getStockSymbols(filteredStockCompaniesNYSE);
					String filteredSymbolesAMEX = NasdaqComHelper.INSTANCE.getStockSymbols(filteredStockCompaniesAMEX);

					System.out.println("    Industry [" + industry + "] (" + filteredCompaniesNum + " companies)");
					System.out.println("        NASDAQ: " + filteredStockCompaniesNASDAQ.size());
					System.out.println("            " + filteredSymbolesNASDAQ);
					System.out.println("        NYSE: " + filteredStockCompaniesNYSE.size());
					System.out.println("            " + filteredSymbolesNYSE);
					System.out.println("        AMEX: " + filteredStockCompaniesAMEX.size());
					System.out.println("            " + filteredSymbolesAMEX);

					if (!filteredSymbolesAMEX.isEmpty()) {
						System.out.println();
					}
				}
			}
			System.out.println();

		} catch (ClientException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	// @Ignore
	@SuppressWarnings("unchecked")
	@Test
	public void test003_listStockCategories_part2() throws IOException {
		System.out.println("--- --- --- test003_listStockCategories_part2() --- --- ---");

		try {
			List<StockCompany> stockCompaniesNASDAQ = this.nasdaqComClient.getStockCompanies(NasdaqComClient.EXCHANGE_NASDAQ, Comparators.StockCompanyComparator.ASC);
			List<StockCompany> stockCompaniesNYSE = this.nasdaqComClient.getStockCompanies(NasdaqComClient.EXCHANGE_NYSE, Comparators.StockCompanyComparator.ASC);
			List<StockCompany> stockCompaniesAMEX = this.nasdaqComClient.getStockCompanies(NasdaqComClient.EXCHANGE_AMEX, Comparators.StockCompanyComparator.ASC);

			List<StockCategory> stockCategories = NasdaqComHelper.INSTANCE.extractStockCategories(Comparators.StockCategoryComparator.ASC, stockCompaniesNASDAQ, stockCompaniesNYSE, stockCompaniesAMEX);
			System.out.println("stockCategories.size() = " + stockCategories.size());
			System.out.println();

			List<StockCategoryWrapper> wrappers = new ArrayList<StockCategoryWrapper>();
			for (StockCategory stockCategory : stockCategories) {
				// System.out.println(stockCategory.toString());

				String sector = stockCategory.getSector();
				String industry = stockCategory.getIndustry();

				List<StockCompany> filteredStockCompaniesNASDAQ = NasdaqComHelper.INSTANCE.filterStockCompanies(stockCompaniesNASDAQ, sector, industry);
				List<StockCompany> filteredStockCompaniesNYSE = NasdaqComHelper.INSTANCE.filterStockCompanies(stockCompaniesNYSE, sector, industry);
				List<StockCompany> filteredStockCompaniesAMEX = NasdaqComHelper.INSTANCE.filterStockCompanies(stockCompaniesAMEX, sector, industry);

				int filteredCompaniesNum = filteredStockCompaniesNASDAQ.size() + filteredStockCompaniesNYSE.size() + filteredStockCompaniesAMEX.size();

				String filteredSymbolesNASDAQ = NasdaqComHelper.INSTANCE.getStockSymbols(filteredStockCompaniesNASDAQ);
				String filteredSymbolesNYSE = NasdaqComHelper.INSTANCE.getStockSymbols(filteredStockCompaniesNYSE);
				String filteredSymbolesAMEX = NasdaqComHelper.INSTANCE.getStockSymbols(filteredStockCompaniesAMEX);

				StockCategoryWrapper wrapper = new StockCategoryWrapper();
				wrapper.stockCategory = stockCategory;
				wrapper.filteredStockCompaniesNASDAQ = filteredStockCompaniesNASDAQ;
				wrapper.filteredStockCompaniesNYSE = filteredStockCompaniesNYSE;
				wrapper.filteredStockCompaniesAMEX = filteredStockCompaniesAMEX;
				wrapper.filteredCompaniesNum = filteredCompaniesNum;
				wrapper.filteredSymbolesNASDAQ = filteredSymbolesNASDAQ;
				wrapper.filteredSymbolesNYSE = filteredSymbolesNYSE;
				wrapper.filteredSymbolesAMEX = filteredSymbolesAMEX;
				wrappers.add(wrapper);
			}

			Collections.sort(wrappers, new StockCategoryWrapperComparator());
			for (StockCategoryWrapper wrapper : wrappers) {
				StockCategory stockCategory = wrapper.stockCategory;
				String sector = stockCategory.getSector();
				String industry = stockCategory.getIndustry();

				List<StockCompany> filteredStockCompaniesNASDAQ = wrapper.filteredStockCompaniesNASDAQ;
				List<StockCompany> filteredStockCompaniesNYSE = wrapper.filteredStockCompaniesNYSE;
				List<StockCompany> filteredStockCompaniesAMEX = wrapper.filteredStockCompaniesAMEX;

				int filteredCompaniesNum = wrapper.filteredCompaniesNum;

				String filteredSymbolesNASDAQ = wrapper.filteredSymbolesNASDAQ;
				String filteredSymbolesNYSE = wrapper.filteredSymbolesNYSE;
				String filteredSymbolesAMEX = wrapper.filteredSymbolesAMEX;

				System.out.println("Sector [" + sector + "] -> Industry [" + industry + "] (" + filteredCompaniesNum + " companies)");
				System.out.println("    NASDAQ: " + filteredStockCompaniesNASDAQ.size());
				System.out.println("        " + filteredSymbolesNASDAQ);
				System.out.println("    NYSE: " + filteredStockCompaniesNYSE.size());
				System.out.println("        " + filteredSymbolesNYSE);
				System.out.println("    AMEX: " + filteredStockCompaniesAMEX.size());
				System.out.println("        " + filteredSymbolesAMEX);

				if (!filteredSymbolesAMEX.isEmpty()) {
					System.out.println();
				}
			}

			System.out.println();

		} catch (ClientException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	public static class StockCategoryWrapper {
		protected StockCategory stockCategory;
		protected List<StockCompany> filteredStockCompaniesNASDAQ;
		protected List<StockCompany> filteredStockCompaniesNYSE;
		protected List<StockCompany> filteredStockCompaniesAMEX;
		protected int filteredCompaniesNum;
		protected String filteredSymbolesNASDAQ;
		protected String filteredSymbolesNYSE;
		protected String filteredSymbolesAMEX;
	}

	public static class StockCategoryWrapperComparator implements Comparator<StockCategoryWrapper> {
		@Override
		public int compare(StockCategoryWrapper o1, StockCategoryWrapper o2) {
			int v1 = o1.filteredCompaniesNum;
			int v2 = o2.filteredCompaniesNum;
			return v2 - v1;
		}
	}

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(NasdaqComClientTest.class);

		System.out.println("--- --- --- NasdaqComClientTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
