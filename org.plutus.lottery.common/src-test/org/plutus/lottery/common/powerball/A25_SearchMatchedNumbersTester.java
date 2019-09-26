package org.plutus.lottery.common.powerball;

import java.io.File;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.origin.common.util.SystemUtils;
import org.plutus.lottery.util.DrawReaderV2;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class A25_SearchMatchedNumbersTester extends AbstractAnalysisTester {

	public A25_SearchMatchedNumbersTester() {
		super(DrawReaderV2.INSTANCE, new File(SystemUtils.getUserDir(), "/doc/data/DownloadAllNumbers.txt"));
	}

	public static void main(String[] args) {

	}

}
