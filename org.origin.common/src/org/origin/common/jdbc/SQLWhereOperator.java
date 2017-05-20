package org.origin.common.jdbc;

/**
 * @see http://www.w3schools.com/sql/sql_where.asp
 *
 */
public class SQLWhereOperator {

	// Equal
	public static final String EQUAL = "=";

	// Not equal. Note: In some versions of SQL this operator may be written as !=
	public static final String NOT_EQUAL = "<>";
	public static final String NOT_EQUAL2 = "!=";

	// Greater than
	public static final String GREATER_THAN = ">";

	// Greater than or equal
	public static final String GREATER_OR_EQUAL_THAN = ">=";

	// Less than
	public static final String LESS_THAN = "<";

	// Less than or equal
	public static final String LESS_OR_EQUAL_THAN = "<=";

	// Between an inclusive range
	public static final String BETWEEN = "BETWEEN";

	// Search for a pattern
	public static final String LIKE = "LIKE";

	// To specify multiple possible values for a column
	public static final String IN = "IN";

	/**
	 * 
	 * @param oper
	 * @return
	 */
	public static String check(String oper) {
		if (oper == null || oper.isEmpty() || EQUAL.equals(oper)) {
			return EQUAL;
		}
		if (!NOT_EQUAL.equals(oper) //
				&& !NOT_EQUAL2.equals(oper) //
				&& !GREATER_THAN.equals(oper) //
				&& !GREATER_OR_EQUAL_THAN.equals(oper) //
				&& !LESS_THAN.equals(oper) //
				&& !LESS_OR_EQUAL_THAN.equals(oper) //
				&& !BETWEEN.equalsIgnoreCase(oper) //
				&& !LIKE.equalsIgnoreCase(oper) //
				&& !IN.equalsIgnoreCase(oper) //
		) {
			return EQUAL;
		}
		return oper;
	}

	/**
	 * Whether operator is '='
	 * 
	 * @param oper
	 * @return
	 */
	public static boolean isEqual(String oper) {
		oper = check(oper);
		return (EQUAL.equals(oper)) ? true : false;
	}

	/**
	 * Whether operator is '<>' or '!='
	 * 
	 * @param oper
	 * @return
	 */
	public static boolean isNotEqual(String oper) {
		oper = check(oper);
		return (NOT_EQUAL.equals(oper) || NOT_EQUAL2.equals(oper)) ? true : false;
	}

	/**
	 * Whether operator is '>'
	 * 
	 * @param oper
	 * @return
	 */
	public static boolean isGreaterThan(String oper) {
		oper = check(oper);
		return (GREATER_THAN.equals(oper)) ? true : false;
	}

	/**
	 * Whether operator is '>='
	 * 
	 * @param oper
	 * @return
	 */
	public static boolean isGreaterOrEqualThan(String oper) {
		oper = check(oper);
		return (GREATER_OR_EQUAL_THAN.equals(oper)) ? true : false;
	}

	/**
	 * Whether operator is '<'
	 * 
	 * @param oper
	 * @return
	 */
	public static boolean isLessThan(String oper) {
		oper = check(oper);
		return (LESS_THAN.equals(oper)) ? true : false;
	}

	/**
	 * Whether operator is '<='
	 * 
	 * @param oper
	 * @return
	 */
	public static boolean isLessOrEqualThan(String oper) {
		oper = check(oper);
		return (LESS_OR_EQUAL_THAN.equals(oper)) ? true : false;
	}

	/**
	 * Whether operator is 'BETWEEN'
	 * 
	 * @param oper
	 * @return
	 */
	public static boolean isBetween(String oper) {
		oper = check(oper);
		return (BETWEEN.equalsIgnoreCase(oper)) ? true : false;
	}

	/**
	 * Whether operator is 'LIKE'
	 * 
	 * @param oper
	 * @return
	 */
	public static boolean isLike(String oper) {
		oper = check(oper);
		return (LIKE.equalsIgnoreCase(oper)) ? true : false;
	}

	/**
	 * Whether operator is 'IN'
	 * 
	 * @param oper
	 * @return
	 */
	public static boolean isIn(String oper) {
		oper = check(oper);
		return (IN.equalsIgnoreCase(oper)) ? true : false;
	}

}
