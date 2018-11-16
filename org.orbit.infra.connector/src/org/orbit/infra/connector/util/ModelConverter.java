package org.orbit.infra.connector.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.orbit.infra.api.configregistry.ConfigElement;
import org.orbit.infra.api.configregistry.ConfigRegistry;
import org.orbit.infra.api.configregistry.ConfigRegistryClient;
import org.orbit.infra.api.datacast.ChannelMetadata;
import org.orbit.infra.api.datacast.DataCastClient;
import org.orbit.infra.api.datacast.DataTubeConfig;
import org.orbit.infra.api.datatube.DataTubeClient;
import org.orbit.infra.api.datatube.RuntimeChannel;
import org.orbit.infra.connector.configregistry.ConfigElementImpl;
import org.orbit.infra.connector.configregistry.ConfigRegistryImpl;
import org.orbit.infra.connector.datacast.ChannelMetadataImpl;
import org.orbit.infra.connector.datacast.DataTubeConfigImpl;
import org.orbit.infra.connector.datatube.RuntimeChannelImpl;
import org.orbit.infra.model.configregistry.ConfigElementDTO;
import org.orbit.infra.model.configregistry.ConfigRegistryDTO;
import org.orbit.infra.model.datacast.ChannelMetadataDTO;
import org.orbit.infra.model.datacast.DataTubeConfigDTO;
import org.orbit.infra.model.datatube.RuntimeChannelDTO;
import org.origin.common.json.JSONUtil;
import org.origin.common.model.AccountConfig;
import org.origin.common.resource.Path;
import org.origin.common.resource.PathDTO;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;
import org.origin.common.util.AccountConfigUtil;

public class ModelConverter {

	public static CONFIG_REGISTRY CONFIG_REGISTRY = new CONFIG_REGISTRY();
	public static DATA_CAST DATA_CAST = new DATA_CAST();
	public static DATA_TUBE DATA_TUBE = new DATA_TUBE();
	public static COMMON COMMON = new COMMON();

	public static class CONFIG_REGISTRY {
		/**
		 * 
		 * @param configRegistryClient
		 * @param configRegistryDTO
		 * @return
		 */
		public ConfigRegistry toConfigRegistry(ConfigRegistryClient configRegistryClient, ConfigRegistryDTO configRegistryDTO) {
			if (configRegistryDTO == null) {
				return null;
			}

			String id = configRegistryDTO.getId();
			String type = configRegistryDTO.getType();
			String name = configRegistryDTO.getName();
			Map<String, Object> properties = configRegistryDTO.getProperties();
			long dateCreated = configRegistryDTO.getDateCreated();
			long dateModified = configRegistryDTO.getDateModified();

			ConfigRegistryImpl configRegistry = new ConfigRegistryImpl(configRegistryClient);
			configRegistry.setId(id);
			configRegistry.setType(type);
			configRegistry.setName(name);
			configRegistry.setProperties(properties);
			configRegistry.setDateCreated(dateCreated);
			configRegistry.setDateModified(dateModified);

			return configRegistry;
		}

		/**
		 * 
		 * @param configRegistryClient
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public ConfigRegistry[] getConfigRegistries(ConfigRegistryClient configRegistryClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			List<ConfigRegistry> configRegistries = new ArrayList<ConfigRegistry>();
			List<ConfigRegistryDTO> configRegistryDTOs = response.readEntity(new GenericType<List<ConfigRegistryDTO>>() {
			});
			for (ConfigRegistryDTO dataTubeConfigDTO : configRegistryDTOs) {
				ConfigRegistry dataTubeConfig = toConfigRegistry(configRegistryClient, dataTubeConfigDTO);
				if (dataTubeConfig != null) {
					configRegistries.add(dataTubeConfig);
				}
			}
			return configRegistries.toArray(new ConfigRegistry[configRegistries.size()]);
		}

		/**
		 * 
		 * @param configRegistryClient
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public ConfigRegistry getConfigRegistry(ConfigRegistryClient configRegistryClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			ConfigRegistry configRegistry = null;
			ConfigRegistryDTO configRegistryDTO = response.readEntity(ConfigRegistryDTO.class);
			if (configRegistryDTO != null) {
				configRegistry = toConfigRegistry(configRegistryClient, configRegistryDTO);
			}
			return configRegistry;
		}

		/**
		 * 
		 * @param configRegistryClient
		 * @param configElementDTO
		 * @return
		 */
		public ConfigElement toConfigElement(ConfigRegistryClient configRegistryClient, ConfigElementDTO configElementDTO) {
			if (configElementDTO == null) {
				return null;
			}

			String configRegistryId = configElementDTO.getConfigRegistryId();
			String parentElementId = configElementDTO.getParentElementId();
			String elementId = configElementDTO.getElementId();
			String pathString = configElementDTO.getPath();
			Map<String, Object> attributes = configElementDTO.getAttributes();
			long dateCreated = configElementDTO.getDateCreated();
			long dateModified = configElementDTO.getDateModified();

			Path path = null;
			if (pathString != null && !pathString.isEmpty()) {
				path = new Path(pathString);
			}

			ConfigElementImpl configElement = new ConfigElementImpl(configRegistryClient);
			configElement.setConfigRegistryId(configRegistryId);
			configElement.setParentElementId(parentElementId);
			configElement.setElementId(elementId);
			configElement.setPath(path);
			configElement.setAttributes(attributes);
			configElement.setDateCreated(dateCreated);
			configElement.setDateModified(dateModified);

			return configElement;
		}

		/**
		 * 
		 * @param configRegistryClient
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement[] getConfigElements(ConfigRegistryClient configRegistryClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			List<ConfigElement> configElements = new ArrayList<ConfigElement>();
			List<ConfigElementDTO> configElementDTOs = response.readEntity(new GenericType<List<ConfigElementDTO>>() {
			});
			for (ConfigElementDTO configElementDTO : configElementDTOs) {
				ConfigElement configElement = toConfigElement(configRegistryClient, configElementDTO);
				if (configElement != null) {
					configElements.add(configElement);
				}
			}
			return configElements.toArray(new ConfigElement[configElements.size()]);
		}

		/**
		 * 
		 * @param configRegistryClient
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public ConfigElement getConfigElement(ConfigRegistryClient configRegistryClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			ConfigElement configElement = null;
			ConfigElementDTO configElementDTO = response.readEntity(ConfigElementDTO.class);
			if (configElementDTO != null) {
				configElement = toConfigElement(configRegistryClient, configElementDTO);
			}
			return configElement;
		}

	}

	public static class DATA_CAST {
		/**
		 * 
		 * @param dataCastClient
		 * @param dataTubeConfigDTO
		 * @return
		 */
		public DataTubeConfig toDataTubeConfig(DataCastClient dataCastClient, DataTubeConfigDTO dataTubeConfigDTO) {
			if (dataTubeConfigDTO == null) {
				return null;
			}

			String configId = dataTubeConfigDTO.getId();
			String dataCastId = dataTubeConfigDTO.getDataCastId();
			String dataTubeId = dataTubeConfigDTO.getDataTubeId();
			String name = dataTubeConfigDTO.getName();
			Map<String, Object> properties = dataTubeConfigDTO.getProperties();
			long dateCreated = dataTubeConfigDTO.getDateCreated();
			long dateModified = dataTubeConfigDTO.getDateModified();

			DataTubeConfigImpl dataTubeConfig = new DataTubeConfigImpl(dataCastClient);
			dataTubeConfig.setId(configId);
			dataTubeConfig.setDataCastId(dataCastId);
			dataTubeConfig.setDataTubeId(dataTubeId);
			dataTubeConfig.setName(name);
			dataTubeConfig.setProperties(properties);
			dataTubeConfig.setDateCreated(dateCreated);
			dataTubeConfig.setDateModified(dateModified);

			return dataTubeConfig;
		}

		/**
		 * 
		 * @param dataCastClient
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public DataTubeConfig[] getDataTubeConfigs(DataCastClient dataCastClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			List<DataTubeConfig> dataTubeConfigs = new ArrayList<DataTubeConfig>();
			List<DataTubeConfigDTO> dataTubeConfigDTOs = response.readEntity(new GenericType<List<DataTubeConfigDTO>>() {
			});
			for (DataTubeConfigDTO dataTubeConfigDTO : dataTubeConfigDTOs) {
				DataTubeConfig dataTubeConfig = toDataTubeConfig(dataCastClient, dataTubeConfigDTO);
				if (dataTubeConfig != null) {
					dataTubeConfigs.add(dataTubeConfig);
				}
			}
			return dataTubeConfigs.toArray(new DataTubeConfig[dataTubeConfigs.size()]);
		}

		/**
		 * 
		 * @param dataCastClient
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public DataTubeConfig getDataTubeConfig(DataCastClient dataCastClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			DataTubeConfig dataTubeConfig = null;
			DataTubeConfigDTO dataTubeConfigDTO = response.readEntity(DataTubeConfigDTO.class);
			if (dataTubeConfigDTO != null) {
				dataTubeConfig = toDataTubeConfig(dataCastClient, dataTubeConfigDTO);
			}
			return dataTubeConfig;
		}

		/**
		 * 
		 * @param dataCastClient
		 * @param channelMetadataDTO
		 * @return
		 */
		public ChannelMetadata toChannelMetadata(DataCastClient dataCastClient, ChannelMetadataDTO channelMetadataDTO) {
			if (channelMetadataDTO == null) {
				return null;
			}

			String dataCastId = channelMetadataDTO.getDataCastId();
			String dataTubeId = channelMetadataDTO.getDataTubeId();
			String channelId = channelMetadataDTO.getChannelId();
			String name = channelMetadataDTO.getName();
			String accessType = channelMetadataDTO.getAccessType();
			String accessCode = channelMetadataDTO.getAccessCode();
			String ownerAccountId = channelMetadataDTO.getOwnerAccountId();
			String accountConfigsString = channelMetadataDTO.getAccountConfigsString();
			Map<String, Object> properties = channelMetadataDTO.getProperties();
			long dateCreated = channelMetadataDTO.getDateCreated();
			long dateModified = channelMetadataDTO.getDateModified();

			List<AccountConfig> accountConfigs = AccountConfigUtil.toAccountConfigs(accountConfigsString);

			ChannelMetadataImpl channelMetadata = new ChannelMetadataImpl(dataCastClient);
			channelMetadata.setDataCastId(dataCastId);
			channelMetadata.setDataTubeId(dataTubeId);
			channelMetadata.setChannelId(channelId);
			channelMetadata.setName(name);
			channelMetadata.setAccessType(accessType);
			channelMetadata.setAccessCode(accessCode);
			channelMetadata.setOwnerAccountId(ownerAccountId);
			channelMetadata.setAccountConfigs(accountConfigs);
			channelMetadata.setProperties(properties);
			channelMetadata.setDateCreated(dateCreated);
			channelMetadata.setDateModified(dateModified);

			return channelMetadata;
		}

		/**
		 * 
		 * @param dataCastClient
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public ChannelMetadata[] getChannelMetadatas(DataCastClient dataCastClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			List<ChannelMetadata> channelMetadatas = new ArrayList<ChannelMetadata>();
			List<ChannelMetadataDTO> channelMetadataDTOs = response.readEntity(new GenericType<List<ChannelMetadataDTO>>() {
			});
			for (ChannelMetadataDTO channelMetadataDTO : channelMetadataDTOs) {
				ChannelMetadata channelMetadata = toChannelMetadata(dataCastClient, channelMetadataDTO);
				if (channelMetadata != null) {
					channelMetadatas.add(channelMetadata);
				}
			}
			return channelMetadatas.toArray(new ChannelMetadata[channelMetadatas.size()]);
		}

		/**
		 * 
		 * @param dataCastClient
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public ChannelMetadata getChannelMetadata(DataCastClient dataCastClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			ChannelMetadata channelMetadata = null;
			ChannelMetadataDTO channelMetadataDTO = response.readEntity(ChannelMetadataDTO.class);
			if (channelMetadataDTO != null) {
				channelMetadata = toChannelMetadata(dataCastClient, channelMetadataDTO);
			}
			return channelMetadata;
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public String getDataTubeId(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			String dataTubeId = null;
			try {
				dataTubeId = ResponseUtil.getSimpleValue(response, "data_tube_id", String.class);

			} catch (Exception e) {
				throw new ClientException(500, e.getMessage(), e);
			}
			return dataTubeId;
		}
	}

	public static class DATA_TUBE {
		/**
		 * 
		 * @param dataTubeClient
		 * @param runtimeChannelDTO
		 * @return
		 */
		public RuntimeChannel toRuntimeChannel(DataTubeClient dataTubeClient, RuntimeChannelDTO runtimeChannelDTO) {
			if (runtimeChannelDTO == null) {
				return null;
			}

			String dataCastId = runtimeChannelDTO.getDataCastId();
			String dataTubeId = runtimeChannelDTO.getDataTubeId();
			String channelId = runtimeChannelDTO.getChannelId();
			String name = runtimeChannelDTO.getName();
			long dateCreated = runtimeChannelDTO.getDateCreated();
			long dateModified = runtimeChannelDTO.getDateModified();

			RuntimeChannelImpl runtimeChannel = new RuntimeChannelImpl(dataTubeClient);
			runtimeChannel.setDataCastId(dataCastId);
			runtimeChannel.setDataTubeId(dataTubeId);
			runtimeChannel.setChannelId(channelId);
			runtimeChannel.setName(name);
			runtimeChannel.setDateCreated(dateCreated);
			runtimeChannel.setDateModified(dateModified);

			return runtimeChannel;
		}

		/**
		 * 
		 * @param dataTubeClient
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public RuntimeChannel[] getRuntimeChannels(DataTubeClient dataTubeClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			List<RuntimeChannel> runtimeChannels = new ArrayList<RuntimeChannel>();
			List<RuntimeChannelDTO> runtimeChannelDTOs = response.readEntity(new GenericType<List<RuntimeChannelDTO>>() {
			});
			for (RuntimeChannelDTO runtimeChannelDTO : runtimeChannelDTOs) {
				RuntimeChannel runtimeChannel = toRuntimeChannel(dataTubeClient, runtimeChannelDTO);
				if (runtimeChannel != null) {
					runtimeChannels.add(runtimeChannel);
				}
			}
			return runtimeChannels.toArray(new RuntimeChannel[runtimeChannels.size()]);
		}

		/**
		 * 
		 * @param dataTubeClient
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public RuntimeChannel getRuntimeChannel(DataTubeClient dataTubeClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			RuntimeChannel runtimeChannel = null;
			RuntimeChannelDTO runtimeChannelDTO = response.readEntity(RuntimeChannelDTO.class);
			if (runtimeChannelDTO != null) {
				runtimeChannel = toRuntimeChannel(dataTubeClient, runtimeChannelDTO);
			}
			return runtimeChannel;
		}
	}

	public static class COMMON {
		/**
		 * 
		 * @param pathDTO
		 * @return
		 */
		public Path toPath(PathDTO pathDTO) {
			if (pathDTO == null) {
				return null;
			}

			String pathString = pathDTO.getPathString();
			if (pathString == null) {
				return null;
			}

			return new Path(pathString);
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public Path getPath(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			Path path = null;
			PathDTO pathDTO = response.readEntity(PathDTO.class);
			if (pathDTO != null) {
				path = toPath(pathDTO);
			}
			return path;
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public boolean exists(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			boolean exists = false;
			try {
				exists = ResponseUtil.getSimpleValue(response, "exists", Boolean.class);

			} catch (Exception e) {
				throw new ClientException(500, e.getMessage(), e);
			}
			return exists;
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public boolean isUpdated(Response response) throws ClientException {
			return isSucceed(response);
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public boolean isDeleted(Response response) throws ClientException {
			return isSucceed(response);
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public boolean isUploaded(Response response) throws ClientException {
			return isSucceed(response);
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public boolean isSucceed(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			boolean succeed = false;
			try {
				succeed = ResponseUtil.getSimpleValue(response, "succeed", Boolean.class);

			} catch (Exception e) {
				throw new ClientException(500, e.getMessage(), e);
			}
			return succeed;
		}

		/**
		 * 
		 * @param properties
		 * @return
		 */
		public String toPropertiesString(Map<String, Object> properties) {
			String propertiesString = JSONUtil.toJsonString(properties);
			return propertiesString;
		}

		/**
		 * 
		 * @param propertiesString
		 * @return
		 */
		public Map<String, Object> toProperties(String propertiesString) {
			Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);
			return properties;
		}

		/**
		 * 
		 * @param propertiesString
		 * @return
		 */
		public Map<String, Object> toMap(String mapString) {
			Map<String, Object> map = JSONUtil.toProperties(mapString, true);
			return map;
		}

		/**
		 * 
		 * @param map
		 * @return
		 */
		public String toMapString(Map<String, Object> map) {
			String mapString = JSONUtil.toJsonString(map);
			return mapString;
		}
	}

}

// long size = dto.getSize();
// int startIndex = dto.getStartIndex();
// int endIndex = dto.getEndIndex();
// metadata.setSize(size);
// metadata.setStartIndex(startIndex);
// metadata.setEndIndex(endIndex);

// /**
// *
// * @param dfsVolumeClient
// * @param dataBlockDTO
// * @return
// */
// public DataBlockMetadata toDataBlock(DfsVolumeClient dfsVolumeClient, DataBlockMetadataDTO dataBlockDTO) {
// if (dataBlockDTO == null) {
// return null;
// }
//
// DataBlockMetadataImpl dataBlock = new DataBlockMetadataImpl(dfsVolumeClient);
// if (dataBlockDTO != null) {
// String dfsId = dataBlockDTO.getDfsId();
// String dfsVolumeId = dataBlockDTO.getDfsVolumeId();
// String blockId = dataBlockDTO.getBlockId();
// String accountId = dataBlockDTO.getAccountId();
// long capacity = dataBlockDTO.getCapacity();
// long size = dataBlockDTO.getSize();
// String pendingFilesString = dataBlockDTO.getPendingFilesString();
// String propertiesString = dataBlockDTO.getPropertiesString();
// long dateCreated = dataBlockDTO.getDateCreated();
// long dateModified = dataBlockDTO.getDateModified();
//
// List<PendingFile> pendingFiles = toPendingFiles(pendingFilesString);
// Map<String, Object> properties = toProperties(propertiesString);
//
// dataBlock.setDfsId(dfsId);
// dataBlock.setDfsVolumeId(dfsVolumeId);
// dataBlock.setBlockId(blockId);
// dataBlock.setAccountId(accountId);
// dataBlock.setCapacity(capacity);
// dataBlock.setSize(size);
// dataBlock.setPendingFiles(pendingFiles);
// dataBlock.setProperties(properties);
// dataBlock.setDateCreated(dateCreated);
// dataBlock.setDateModified(dateModified);
// }
// return dataBlock;
// }
//
// /**
// *
// * @param dfsVolumeClient
// * @param fileContentDTO
// * @return
// */
// public FileContentMetadata toFileContent(DfsVolumeClient dfsVolumeClient, FileContentMetadataDTO fileContentDTO) {
// if (fileContentDTO == null) {
// return null;
// }
//
// String dfsId = fileContentDTO.getDfsId();
// String dfsVolumeId = fileContentDTO.getDfsVolumeId();
// String blockId = fileContentDTO.getBlockId();
// String fileId = fileContentDTO.getFileId();
// int partId = fileContentDTO.getPartId();
// long size = fileContentDTO.getSize();
// long checksum = fileContentDTO.getChecksum();
// long dateCreated = fileContentDTO.getDateCreated();
// long dateModified = fileContentDTO.getDateModified();
//
// FileContentMetadataImpl fileContent = new FileContentMetadataImpl(dfsVolumeClient);
// fileContent.setDfsId(dfsId);
// fileContent.setDfsVolumeId(dfsVolumeId);
// fileContent.setBlockId(blockId);
// fileContent.setFileId(fileId);
// fileContent.setPartId(partId);
// fileContent.setSize(size);
// fileContent.setChecksum(checksum);
// fileContent.setDateCreated(dateCreated);
// fileContent.setDateModified(dateModified);
//
// return fileContent;
// }
//
// /**
// *
// * @param dfsVolumeClient
// * @param response
// * @return
// * @throws ClientException
// */
// public DataBlockMetadata[] getDataBlocks(DfsVolumeClient dfsVolumeClient, Response response) throws ClientException {
// if (!ResponseUtil.isSuccessful(response)) {
// throw new ClientException(response);
// }
// List<DataBlockMetadata> datablocks = new ArrayList<DataBlockMetadata>();
// List<DataBlockMetadataDTO> datablockDTOs = response.readEntity(new GenericType<List<DataBlockMetadataDTO>>() {
// });
// for (DataBlockMetadataDTO datablockDTO : datablockDTOs) {
// DataBlockMetadata datablock = toDataBlock(dfsVolumeClient, datablockDTO);
// if (datablock != null) {
// datablocks.add(datablock);
// }
// }
// return datablocks.toArray(new DataBlockMetadata[datablocks.size()]);
// }
//
// /**
// *
// * @param dfsVolumeClient
// * @param response
// * @return
// * @throws ClientException
// */
// public DataBlockMetadata getDataBlock(DfsVolumeClient dfsVolumeClient, Response response) throws ClientException {
// if (!ResponseUtil.isSuccessful(response)) {
// throw new ClientException(response);
// }
// DataBlockMetadata datablock = null;
// DataBlockMetadataDTO datablockDTO = response.readEntity(DataBlockMetadataDTO.class);
// if (datablockDTO != null) {
// datablock = toDataBlock(dfsVolumeClient, datablockDTO);
// }
// return datablock;
// }

// /**
// *
// * @param dfsVolumeClient
// * @param response
// * @return
// * @throws ClientException
// */
// public FileContentMetadata[] getFileContents(DfsVolumeClient dfsVolumeClient, Response response) throws ClientException {
// if (!ResponseUtil.isSuccessful(response)) {
// throw new ClientException(response);
// }
// List<FileContentMetadata> fileContents = new ArrayList<FileContentMetadata>();
// List<FileContentMetadataDTO> fileContentDTOs = response.readEntity(new GenericType<List<FileContentMetadataDTO>>() {
// });
// for (FileContentMetadataDTO fileContentDTO : fileContentDTOs) {
// FileContentMetadata fileContent = toFileContent(dfsVolumeClient, fileContentDTO);
// if (fileContent != null) {
// fileContents.add(fileContent);
// }
// }
// return fileContents.toArray(new FileContentMetadata[fileContents.size()]);
// }
//
// /**
// *
// * @param dfsVolumeClient
// * @param response
// * @return
// * @throws ClientException
// */
// public FileContentMetadata getFileContent(DfsVolumeClient dfsVolumeClient, Response response) throws ClientException {
// if (!ResponseUtil.isSuccessful(response)) {
// throw new ClientException(response);
// }
// FileContentMetadata fileContent = null;
// FileContentMetadataDTO fileContentDTO = response.readEntity(FileContentMetadataDTO.class);
// if (fileContentDTO != null) {
// fileContent = toFileContent(dfsVolumeClient, fileContentDTO);
// }
// return fileContent;
// }
//
// /**
// * Convert PendingFile objects to json string
// *
// * @param pendingFiles
// * @return
// */
// public String toPendingFilesString(List<PendingFile> pendingFiles) {
// PendingFilesWriter writer = new PendingFilesWriter();
// String pendingFilesString = writer.write(pendingFiles);
// if (pendingFilesString == null) {
// pendingFilesString = "";
// }
// return pendingFilesString;
// }
//
// /**
// * Convert json string to PendingFile objects
// *
// * @param pendingFilesString
// * @return
// */
// public List<PendingFile> toPendingFiles(String pendingFilesString) {
// PendingFilesReader reader = new PendingFilesReader();
// List<PendingFile> pendingFiles = reader.read(pendingFilesString);
// if (pendingFiles == null) {
// pendingFiles = new ArrayList<PendingFile>();
// }
// return pendingFiles;
// }

// /**
// *
// * @param response
// * @return
// * @throws ClientException
// */
// public FileContentMetadata getUpdatedFileContent(DfsVolumeClient dfsVolumeClient, Response response) throws ClientException {
// if (!ResponseUtil.isSuccessful(response)) {
// throw new ClientException(response);
// }
//
// FileContentMetadata fileContent = null;
// FileContentMetadataDTO fileContentDTO = response.readEntity(FileContentMetadataDTO.class);
// if (fileContentDTO != null) {
// fileContent = toFileContent(dfsVolumeClient, fileContentDTO);
// }
// return fileContent;
// }
