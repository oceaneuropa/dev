package org.origin.common.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class PrettyPrinter {

	public static interface Printer {

		public void print(Object object);

		public void println(Object object);
	}

	public static class DefaultPrinter implements Printer {
		@Override
		public void print(Object object) {
			System.out.print(object);
		}

		@Override
		public void println(Object object) {
			System.out.println(object);
		}
	}

	protected static DefaultPrinter DEFAULT_PRINTER = new DefaultPrinter();

	/**
	 * 
	 * @param titles
	 * @param rows
	 */
	public static void prettyPrint(String[] titles, String[]... rows) {
		prettyPrint(DEFAULT_PRINTER, "(no records)", true, titles, rows);
	}

	/**
	 * 
	 * @param printer
	 * @param defaultContent
	 * @param printRowCount
	 * @param titles
	 * @param rows
	 */
	public static void prettyPrint(Printer printer, String defaultContent, boolean printRowCount, String[] titles, String[]... rows) {
		if (printer == null) {
			printer = DEFAULT_PRINTER;
		}
		if (titles == null) {
			titles = new String[] {};
		}
		if (rows == null) {
			rows = new String[][] {};
		}

		// get max number of columns
		int maxColumnNum = titles.length;
		for (String[] row : rows) {
			if (row.length > maxColumnNum) {
				maxColumnNum = row.length;
			}
		}

		if (maxColumnNum == 0) {
			printer.println(defaultContent);
			return;
		}

		// make sure titles has the same max number of columns
		if (titles.length < maxColumnNum) {
			for (int i = 0; i < titles.length; i++) {
				String[] tmpTitles = new String[maxColumnNum];
				System.arraycopy(titles, 0, tmpTitles, 0, titles.length);
				titles = tmpTitles;
			}
		}

		// make sure each row has the same max number of columns
		for (int i = 0; i < rows.length; i++) {
			String[] row = rows[i];
			if (row != null && row.length < maxColumnNum) {
				String[] tmpRow = new String[maxColumnNum];
				System.arraycopy(row, 0, tmpRow, 0, row.length);
				rows[i] = tmpRow;
			}
		}

		// calculate max length (of characters) in each column
		int defaultMaxLength = 0;
		Map<Integer, Integer> columnIndexToMaxLength = new Hashtable<Integer, Integer>();
		for (int columnIndex = 0; columnIndex < maxColumnNum; columnIndex++) {
			int columnMaxLength = 0;
			if (titles[columnIndex] != null && titles[columnIndex].length() > columnMaxLength) {
				columnMaxLength = titles[columnIndex].length();
			}

			for (int i = 0; i < rows.length; i++) {
				String[] row = rows[i];
				if (row != null && row[columnIndex] != null && row[columnIndex].length() > columnMaxLength) {
					columnMaxLength = row[columnIndex].length();
				}
			}

			columnIndexToMaxLength.put(columnIndex, columnMaxLength);
			defaultMaxLength += columnMaxLength;

			if (columnIndex > 0) {
				defaultMaxLength += 3;
			}
		}

		if (rows.length == 0 && defaultContent.length() > defaultMaxLength) {
			int lastColumnIndex = maxColumnNum - 1;
			int maxLength = columnIndexToMaxLength.get(lastColumnIndex);
			maxLength += defaultContent.length() - defaultMaxLength;
			columnIndexToMaxLength.put(lastColumnIndex, maxLength);
		}

		List<String> lines = new ArrayList<String>();

		// first line
		String line0 = getRowSeparater(maxColumnNum, columnIndexToMaxLength, true);
		lines.add(line0);

		// titles
		String line_title = getRowText(titles, maxColumnNum, columnIndexToMaxLength, true);
		lines.add(line_title);

		boolean withColumnSeparater = (rows.length == 0) ? false : true;
		String line_title_separater = getRowSeparater(maxColumnNum, columnIndexToMaxLength, withColumnSeparater);
		lines.add(line_title_separater);

		// rows
		int rowCount = rows.length;
		if (rowCount == 0) {
			// no rows. use default content
			String line_row = getRowText(defaultContent, defaultMaxLength);
			lines.add(line_row);

			String line_separater = getRowSeparater(maxColumnNum, columnIndexToMaxLength, false);
			lines.add(line_separater);

		} else {
			// there are rows.
			for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
				String[] row = rows[rowIndex];

				String line_row = getRowText(row, maxColumnNum, columnIndexToMaxLength, true);
				lines.add(line_row);

				if (rowIndex == (rows.length - 1)) {
					String line_separater = getRowSeparater(maxColumnNum, columnIndexToMaxLength, true);
					lines.add(line_separater);
				}
			}
		}

		// row count
		if (printRowCount) {
			StringBuilder rowCountLine = new StringBuilder();
			rowCountLine.append("(").append(rowCount);
			if (rowCount <= 1) {
				rowCountLine.append(" row)");
			} else {
				rowCountLine.append(" rows)");
			}
			lines.add(rowCountLine.toString());
		}

		// print out result
		for (String line : lines) {
			printer.println(line);
		}
	}

	/**
	 * 
	 * @param columnNum
	 * @param columnIndexToMaxLength
	 * @param withColumnSeparaterb
	 * @return
	 */
	protected static String getRowSeparater(int columnNum, Map<Integer, Integer> columnIndexToMaxLength, boolean withColumnSeparaterb) {
		StringBuilder line = new StringBuilder();
		line.append("+-");

		for (int columnIndex = 0; columnIndex < columnNum; columnIndex++) {
			int maxLength = columnIndexToMaxLength.get(columnIndex);
			if (maxLength <= 0) {
				maxLength = 4;
			}
			for (int i = 0; i < maxLength; i++) {
				line.append("-");
			}

			if (columnIndex < columnNum - 1) {
				// not last column
				if (withColumnSeparaterb) {
					line.append("-+-");
				} else {
					line.append("---");
				}
			} else {
				// is last column
				line.append("-+");
			}
		}

		return line.toString();
	}

	/**
	 * 
	 * @param columnNum
	 * @param columnIndexToMaxLength
	 * @param withColumnSeparaterb
	 * @return
	 */
	protected static String getRowText(String[] texts, int columnNum, Map<Integer, Integer> columnIndexToMaxLength, boolean withColumnSeparaterb) {
		StringBuilder line = new StringBuilder();
		line.append("| ");

		for (int columnIndex = 0; columnIndex < columnNum; columnIndex++) {
			int maxLength = columnIndexToMaxLength.get(columnIndex);

			String text = texts[columnIndex];
			int textLength = 0;
			if (text != null) {
				textLength = text.length();
				line.append(text);
			}

			int remainLength = maxLength - textLength;
			for (int i = 0; i < remainLength; i++) {
				line.append(" ");
			}

			if (columnIndex < columnNum - 1) {
				// not last column
				if (withColumnSeparaterb) {
					line.append(" | ");
				} else {
					line.append("   ");
				}
			} else {
				// is last column
				line.append(" |");
			}
		}

		return line.toString();
	}

	/**
	 * 
	 * @param maxLength
	 * @return
	 */
	protected static String getRowSeparater(int maxLength) {
		StringBuilder line = new StringBuilder();
		line.append("+-");
		for (int i = 0; i < maxLength; i++) {
			line.append("-");
		}
		line.append("-+");
		return line.toString();
	}

	/**
	 * 
	 * @param text
	 * @param maxLength
	 * @return
	 */
	protected static String getRowText(String text, int maxLength) {
		StringBuilder line = new StringBuilder();
		line.append("| ");

		int textLength = 0;
		if (text != null) {
			textLength = text.length();
			line.append(text);
		}

		int leftLength = maxLength - textLength;
		for (int i = 0; i < leftLength; i++) {
			line.append(" ");
		}

		line.append(" |");
		return line.toString();
	}

}
