package org.orbit.platform.model.platform.dto;

import java.util.ArrayList;
import java.util.List;

public class PlatformModelConverter {

	public static PlatformModelConverter INSTANCE = new PlatformModelConverter();

	/**
	 * 
	 * @param serviceExtensionInfoDTOs
	 * @return
	 */
	public List<ServiceExtensionInfo> toServiceExtensions(List<ServiceExtensionInfoDTO> serviceExtensionInfoDTOs) {
		List<ServiceExtensionInfo> serviceExtensionInfos = new ArrayList<ServiceExtensionInfo>();
		if (serviceExtensionInfoDTOs != null) {
			for (ServiceExtensionInfoDTO serviceExtensionInfoDTO : serviceExtensionInfoDTOs) {
				ServiceExtensionInfo serviceExtensionInfo = toServiceExtensions(serviceExtensionInfoDTO);
				if (serviceExtensionInfo != null) {
					serviceExtensionInfos.add(serviceExtensionInfo);
				}
			}
		}
		return serviceExtensionInfos;
	}

	/**
	 * 
	 * @param serviceExtensionInfoDTO
	 * @return
	 */
	public ServiceExtensionInfo toServiceExtensions(ServiceExtensionInfoDTO serviceExtensionInfoDTO) {
		if (serviceExtensionInfoDTO == null) {
			return null;
		}
		ServiceExtensionInfoImpl serviceExtensionInfo = new ServiceExtensionInfoImpl();
		serviceExtensionInfo.setExtensionTypeId(serviceExtensionInfoDTO.getExtensionTypeId());
		serviceExtensionInfo.setExtensionId(serviceExtensionInfoDTO.getExtensionId());
		return serviceExtensionInfo;
	}

	/**
	 * 
	 * @param serviceInstanceInfoDTOs
	 * @return
	 */
	public List<ServiceInstanceInfo> toServiceInstances(List<ServiceInstanceInfoDTO> serviceInstanceInfoDTOs) {
		List<ServiceInstanceInfo> serviceInstanceInfos = new ArrayList<ServiceInstanceInfo>();
		if (serviceInstanceInfoDTOs != null) {
			for (ServiceInstanceInfoDTO serviceInstanceInfoDTO : serviceInstanceInfoDTOs) {
				ServiceInstanceInfo serviceInstanceInfo = toServiceInstance(serviceInstanceInfoDTO);
				if (serviceInstanceInfo != null) {
					serviceInstanceInfos.add(serviceInstanceInfo);
				}
			}
		}
		return serviceInstanceInfos;
	}

	/**
	 * 
	 * @param serviceInstanceInfoDTO
	 * @return
	 */
	public ServiceInstanceInfo toServiceInstance(ServiceInstanceInfoDTO serviceInstanceInfoDTO) {
		if (serviceInstanceInfoDTO == null) {
			return null;
		}
		ServiceInstanceInfoImpl serviceInstanceInfo = new ServiceInstanceInfoImpl();
		serviceInstanceInfo.setExtensionTypeId(serviceInstanceInfoDTO.getExtensionTypeId());
		serviceInstanceInfo.setExtensionId(serviceInstanceInfoDTO.getExtensionId());
		serviceInstanceInfo.setProperties(serviceInstanceInfoDTO.getProperties());
		return serviceInstanceInfo;
	}

}
