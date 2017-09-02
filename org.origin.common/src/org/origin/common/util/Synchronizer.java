package org.origin.common.util;

public interface Synchronizer<SOURCE, TARGET> {

	boolean isSyncTarget(SOURCE source, TARGET target);

	boolean isInSync(SOURCE source, TARGET target);

	boolean syncTarget(SOURCE source, TARGET target);

	TARGET buildTarget(SOURCE source);

	boolean disposeTarget(TARGET target);

}
