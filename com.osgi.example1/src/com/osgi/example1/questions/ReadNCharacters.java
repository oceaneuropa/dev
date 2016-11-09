package com.osgi.example1.questions;

public class ReadNCharacters {

	public int read4(char[] buf) {
		return 4;
	}

	public int read(char[] buf, int n) {
		int totalNum = 0;

		char[] buf4 = new char[4];
		while (totalNum < n) {
			int len = 0;
			try {
				len = read4(buf4);
			} catch (Exception e) {
				// error reading file
				e.printStackTrace();
				break;
			}
			if (len <= 0) {
				// end of file
				break;
			}

			if (totalNum + len > n) {
				len = n - totalNum;
			}

			System.arraycopy(buf4, 0, buf, totalNum, len);
			totalNum += len;

			if (totalNum == n) {
				break;
			}
		}

		return totalNum;
	}

}
