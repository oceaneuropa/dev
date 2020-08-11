package org.orbit.component.api.util;

import java.util.Map;

import org.orbit.platform.model.program.ManifestReader;
import org.orbit.platform.model.program.ProgramManifest;

public class ProgramManifestClientConverter {

	/**
	 * 
	 * @param responseMap
	 * @return
	 */
	public static ProgramManifest convert(Map<?, ?> responseMap) {
		if (responseMap == null) {
			return null;
		}
		ProgramManifest programManifest = null;
		return programManifest;
	}

	/**
	 * 
	 * @param responseMap
	 * @return
	 */
	public static ProgramManifest convert(String stringContent) {
		if (stringContent == null || stringContent.isEmpty()) {
			return null;
		}
		ManifestReader reader = new ManifestReader();
		ProgramManifest programManifest = reader.read(stringContent);
		return programManifest;
	}

}
