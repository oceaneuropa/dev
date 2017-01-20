package org.plutus.stock.common.util;

import java.util.Comparator;
import java.util.Date;

import org.plutus.stock.model.dto.StockCategory;
import org.plutus.stock.model.dto.StockCompany;
import org.plutus.stock.model.dto.StockSector;
import org.plutus.stock.model.dto.YahooFinanceStockData;
import org.plutus.stock.model.runtime.StockData;
import org.plutus.stock.model.runtime.StockStat;

public class Comparators {

	/**
	 * asc/desc aware Comparator
	 *
	 * @param <T>
	 */
	public static abstract class BaseComparator<T> implements Comparator<T> {
		public static final String SORT_ASC = "asc"; //$NON-NLS-1$
		public static final String SORT_DESC = "desc"; //$NON-NLS-1$

		protected String sort;

		protected String check(String sort) {
			if (!SORT_ASC.equalsIgnoreCase(sort) && !SORT_DESC.equalsIgnoreCase(sort)) {
				return SORT_ASC;
			}
			return sort;
		}

		protected boolean asc() {
			return SORT_ASC.equalsIgnoreCase(sort);
		}

		protected boolean desc() {
			return SORT_DESC.equalsIgnoreCase(sort);
		}
	}

	/**
	 * Compare StockCategoryData objects
	 *
	 */
	public static class StockCategoryComparator extends BaseComparator<StockCategory> {
		public static StockCategoryComparator ASC = new StockCategoryComparator(SORT_ASC);
		public static StockCategoryComparator DESC = new StockCategoryComparator(SORT_DESC);

		public StockCategoryComparator() {
			this(SORT_ASC);
		}

		public StockCategoryComparator(String sort) {
			this.sort = check(sort);
		}

		@Override
		public int compare(StockCategory o1, StockCategory o2) {
			if (desc()) {
				StockCategory tmp = o1;
				o1 = o2;
				o2 = tmp;
			}

			String sector1 = o1.getSector();
			if (sector1 == null) {
				sector1 = "";
			}
			String sector2 = o2.getSector();
			if (sector2 == null) {
				sector2 = "";
			}

			String industry1 = o1.getIndustry();
			if (industry1 == null) {
				industry1 = "";
			}
			String industry2 = o2.getIndustry();
			if (industry2 == null) {
				industry2 = "";
			}

			if (sector1.equals(sector2)) {
				// same sector, compare industries
				if ("".equals(industry2)) {
					return -1;

				} else if ("n/a".equalsIgnoreCase(industry2)) {
					if ("".equals(industry1)) {
						return 1;
					}
					return -1;

				} else {
					return industry1.compareTo(industry2);
				}
			} else {
				// different sectors, compare sectors
				if ("".equals(sector2)) {
					return -1;

				} else if ("n/a".equalsIgnoreCase(sector2)) {
					if ("".equals(sector1)) {
						return 1;
					}
					return -1;

				} else {
					return sector1.compareTo(sector2);
				}
			}
		}
	}

	/**
	 * Compare StockSector objects.
	 *
	 */
	public static class StockSectorComparator extends BaseComparator<StockSector> {
		public static StockSectorComparator ASC = new StockSectorComparator(SORT_ASC);
		public static StockSectorComparator DESC = new StockSectorComparator(SORT_DESC);

		public StockSectorComparator() {
			this(SORT_ASC);
		}

		public StockSectorComparator(String sort) {
			this.sort = check(sort);
		}

		@Override
		public int compare(StockSector o1, StockSector o2) {
			if (desc()) {
				StockSector tmp = o1;
				o1 = o2;
				o2 = tmp;
			}

			String sector1 = o1.getSector();
			String sector2 = o2.getSector();

			if ("".equals(sector2)) {
				return -1;

			} else if ("n/a".equalsIgnoreCase(sector2)) {
				if ("".equals(sector1)) {
					return 1;
				}
				return -1;

			} else {
				return sector1.compareTo(sector2);
			}
		}
	}

	/**
	 * Compare sector strings.
	 *
	 */
	public static class SectorComparator extends BaseComparator<String> {
		public static SectorComparator ASC = new SectorComparator(SORT_ASC);
		public static SectorComparator DESC = new SectorComparator(SORT_DESC);

		public SectorComparator() {
			this(SORT_ASC);
		}

		public SectorComparator(String sort) {
			this.sort = check(sort);
		}

		@Override
		public int compare(String sector1, String sector2) {
			if (desc()) {
				String tmp = sector1;
				sector1 = sector2;
				sector2 = tmp;
			}

			if ("".equals(sector2)) {
				return -1;

			} else if ("n/a".equalsIgnoreCase(sector2)) {
				if ("".equals(sector1)) {
					return 1;
				}
				return -1;

			} else {
				return sector1.compareTo(sector2);
			}
		}
	}

	/**
	 * Compare industry strings.
	 * 
	 */
	public static class IndustryComparator extends BaseComparator<String> {
		public static IndustryComparator ASC = new IndustryComparator(SORT_ASC);
		public static IndustryComparator DESC = new IndustryComparator(SORT_DESC);

		public IndustryComparator() {
			this(SORT_ASC);
		}

		public IndustryComparator(String sort) {
			this.sort = check(sort);
		}

		@Override
		public int compare(String sector1, String sector2) {
			if (desc()) {
				String tmp = sector1;
				sector1 = sector2;
				sector2 = tmp;
			}

			if ("".equals(sector2)) {
				return -1;

			} else if ("n/a".equalsIgnoreCase(sector2)) {
				if ("".equals(sector1)) {
					return 1;
				}
				return -1;

			} else {
				return sector1.compareTo(sector2);
			}
		}
	}

	/**
	 * Compare StockCompanyData objects
	 *
	 */
	public static class StockCompanyComparator extends BaseComparator<StockCompany> {
		public static StockCompanyComparator ASC = new StockCompanyComparator(SORT_ASC);
		public static StockCompanyComparator DESC = new StockCompanyComparator(SORT_DESC);

		public StockCompanyComparator() {
			this(SORT_ASC);
		}

		public StockCompanyComparator(String sort) {
			this.sort = check(sort);
		}

		@Override
		public int compare(StockCompany o1, StockCompany o2) {
			if (desc()) {
				StockCompany tmp = o1;
				o1 = o2;
				o2 = tmp;
			}

			String symbol1 = o1.getSymbol();
			if (symbol1 == null) {
				symbol1 = "";
			}
			String symbol2 = o2.getSymbol();
			if (symbol2 == null) {
				symbol2 = "";
			}

			return symbol1.compareTo(symbol2);
		}
	}

	/**
	 * Compare YahooFinanceStockData objects
	 *
	 */
	public static class YahooStockDataComparator extends BaseComparator<YahooFinanceStockData> {
		public static YahooStockDataComparator ASC = new YahooStockDataComparator(SORT_ASC);
		public static YahooStockDataComparator DESC = new YahooStockDataComparator(SORT_DESC);

		public YahooStockDataComparator() {
			this(SORT_ASC);
		}

		public YahooStockDataComparator(String sort) {
			this.sort = check(sort);
		}

		@Override
		public int compare(YahooFinanceStockData o1, YahooFinanceStockData o2) {
			if (desc()) {
				YahooFinanceStockData tmp = o1;
				o1 = o2;
				o2 = tmp;
			}
			Date date1 = o1.getDate();
			Date date2 = o2.getDate();
			return date1.compareTo(date2);
		}
	}

	/**
	 * Compare StockData objects
	 *
	 */
	public static class StockDataComparator extends BaseComparator<StockData> {
		public static StockDataComparator ASC = new StockDataComparator(SORT_ASC);
		public static StockDataComparator DESC = new StockDataComparator(SORT_DESC);

		public StockDataComparator() {
			this(SORT_ASC);
		}

		public StockDataComparator(String sort) {
			this.sort = check(sort);
		}

		@Override
		public int compare(StockData o1, StockData o2) {
			if (desc()) {
				StockData tmp = o1;
				o1 = o2;
				o2 = tmp;
			}
			Date date1 = o1.getDate();
			Date date2 = o2.getDate();
			return date1.compareTo(date2);
		}
	}

	/**
	 * Compare StockStat objects
	 *
	 */
	public static class StockStatComparator extends BaseComparator<StockStat> {
		public static StockStatComparator ASC = new StockStatComparator(SORT_ASC);
		public static StockStatComparator DESC = new StockStatComparator(SORT_DESC);

		public StockStatComparator() {
			this(SORT_ASC);
		}

		public StockStatComparator(String sort) {
			this.sort = check(sort);
		}

		@Override
		public int compare(StockStat o1, StockStat o2) {
			if (desc()) {
				StockStat tmp = o1;
				o1 = o2;
				o2 = tmp;
			}
			Date date1 = o1.getDate();
			Date date2 = o2.getDate();
			return date1.compareTo(date2);
		}
	}

}
