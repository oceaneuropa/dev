package org.orbit.infra.runtime.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;
import org.orbit.infra.io.util.DataCastNodeConfigHelper;
import org.orbit.infra.runtime.datacast.service.ChannelMetadata;
import org.orbit.platform.sdk.ranking.RankingProvider;
import org.origin.common.model.Weightable;
import org.origin.common.util.WeightableComparator;

public class RankingProviderForDataTubeNodeChannel implements RankingProvider {

	public static final String ID = "org.orbit.infra.datacast.RankingProviderForDataTubeNodeChannel";

	@Override
	public Map<String, Object> getRanking(Map<String, Object> args) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();

		String accessToken = (String) args.get("accessToken");
		String dataCastId = (String) args.get("dataCastId");
		List<ChannelMetadata> channels = (List<ChannelMetadata>) args.get("channels");

		if (accessToken == null) {
			throw new IllegalArgumentException("'accessToken' parameter is not set.");
		}
		if (dataCastId == null) {
			throw new IllegalArgumentException("'dataCastId' parameter is not set.");
		}
		if (channels == null) {
			throw new IllegalArgumentException("'channels' parameter is not set.");
		}

		List<Weightable<IConfigElement, Integer>> weightables = new ArrayList<Weightable<IConfigElement, Integer>>();

		IConfigRegistry cfgReg = DataCastNodeConfigHelper.INSTANCE.getDataCastNodesConfigRegistry(accessToken, false);
		if (cfgReg != null) {
			IConfigElement dataCastConfigElement = DataCastNodeConfigHelper.INSTANCE.getDataCastConfigElement(cfgReg, dataCastId);

			if (dataCastConfigElement != null) {
				IConfigElement[] dataTubeConfigElements = dataCastConfigElement.memberConfigElements();

				for (IConfigElement dataTubeConfigElement : dataTubeConfigElements) {
					String dataTubeId = dataTubeConfigElement.getAttribute(InfraConstants.IDX_PROP__DATATUBE__ID, String.class);

					if (dataTubeId != null && dataTubeConfigElement.isEnabled()) {
						int weight = 0;
						for (ChannelMetadata channel : channels) {
							String currDataTubeId = channel.getDataTubeId();
							if (dataTubeId.equals(currDataTubeId)) {
								weight += 1;
							}
						}
						weightables.add(new Weightable<IConfigElement, Integer>(dataTubeConfigElement, weight));
					}
				}
			}
		}

		// Sort the dataTube elements by weight. Put dataTube with lighter weight first.
		Collections.sort(weightables, WeightableComparator.ASC);

		List<IConfigElement> dataTubeConfigElements = new ArrayList<IConfigElement>();
		Map<String, Integer> dataTubeIdToWeight = new LinkedHashMap<String, Integer>();

		for (Weightable<IConfigElement, Integer> weightable : weightables) {
			IConfigElement dataTubeConfigElement = weightable.getElement();
			String dataTubeId = dataTubeConfigElement.getAttribute(InfraConstants.IDX_PROP__DATATUBE__ID, String.class);
			Integer weight = weightable.getWeight();

			dataTubeConfigElements.add(dataTubeConfigElement);
			dataTubeIdToWeight.put(dataTubeId, weight);
		}

		result.put("dataTubeConfigElements", dataTubeConfigElements);
		result.put("dataTubeIdToWeight", dataTubeIdToWeight);

		return result;
	}

}
