package org.origin.common.rest.editpolicy;

import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.util.ResponseUtil;

public abstract class AbstractWSCommand implements WSCommand {

	/**
	 * Handle Exception and create ErrorDTO from it.
	 * 
	 * @param e
	 * @param errorCode
	 * @param printStackTrace
	 * @return
	 */
	protected ErrorDTO handleError(Exception e, String errorCode, boolean printStackTrace) {
		if (printStackTrace) {
			e.printStackTrace();
		}
		return ResponseUtil.toDTO(e, errorCode);
	}

}
