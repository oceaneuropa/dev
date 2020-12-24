package org.orbit.infra.connector.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.orbit.infra.api.configregistry.ConfigElement;
import org.orbit.infra.api.configregistry.ConfigRegistry;
import org.orbit.infra.api.configregistry.ConfigRegistryClient;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProviderItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.subscription.ISubsMapping;
import org.orbit.infra.api.subscription.SubsServerAPI;
import org.orbit.infra.api.subscription.ISubsSource;
import org.orbit.infra.api.subscription.ISubsTarget;
import org.orbit.infra.api.subscription.ISubsType;
import org.orbit.infra.connector.configregistry.ConfigElementImpl;
import org.orbit.infra.connector.configregistry.ConfigRegistryImpl;
import org.orbit.infra.connector.indexes.IndexItemImpl;
import org.orbit.infra.connector.indexes.IndexProviderItemImpl;
import org.orbit.infra.connector.subscription.SubsMappingImpl;
import org.orbit.infra.connector.subscription.SubsSourceImpl;
import org.orbit.infra.connector.subscription.SubsTargetImpl;
import org.orbit.infra.connector.subscription.SubsTypeImpl;
import org.orbit.infra.model.configregistry.ConfigElementDTO;
import org.orbit.infra.model.configregistry.ConfigRegistryDTO;
import org.orbit.infra.model.indexes.IndexItemDTO;
import org.orbit.infra.model.indexes.IndexProviderItemDTO;
import org.orbit.infra.model.subs.dto.SubsMappingDTO;
import org.orbit.infra.model.subs.dto.SubsSourceDTO;
import org.orbit.infra.model.subs.dto.SubsTargetDTO;
import org.orbit.infra.model.subs.dto.SubsTypeDTO;
import org.origin.common.json.JSONUtil;
import org.origin.common.resource.Path;
import org.origin.common.resource.PathDTO;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class ClientModelConverter {

	public static class INDEX_SERVICE {

		/**
		 * 
		 * @param DTO
		 * @return
		 */
		public static IndexProviderItem toIndexProvider(IndexProviderItemDTO DTO) {
			if (DTO == null) {
				return null;
			}
			IndexProviderItemImpl item = new IndexProviderItemImpl();

			String id = DTO.getId();
			String name = DTO.getName();
			String description = DTO.getDescription();
			long dateCreated = DTO.getDateCreated();
			long dateModified = DTO.getDateModified();
			Date dateCreatedObj = new Date(dateCreated);
			Date dateModifiedObj = new Date(dateModified);

			item.setId(id);
			item.setName(name);
			item.setDescription(description);
			item.setDateCreated(dateCreatedObj);
			item.setDateModified(dateModifiedObj);

			return item;
		}

		/**
		 * 
		 * @param client
		 * @param indexItemDTO
		 * @return
		 */
		public static IndexItem toIndexItem(IndexServiceClient client, IndexItemDTO indexItemDTO) {
			if (indexItemDTO == null) {
				return null;
			}
			Integer indexItemId = indexItemDTO.getIndexItemId();
			String indexProviderId = indexItemDTO.getIndexProviderId();
			String type = indexItemDTO.getType();
			String name = indexItemDTO.getName();
			// Map<String, Object> currProperties = indexItemDTO.getProperties();
			String propertiesString = indexItemDTO.getPropertiesString();
			Map<String, Object> properties = JSONUtil.toProperties(propertiesString);
			Date createTime = indexItemDTO.getCreateTime();
			Date updateTime = indexItemDTO.getUpdateTime();

			IndexItem indexItem = new IndexItemImpl(indexItemId, indexProviderId, type, name, properties, createTime, updateTime);
			return indexItem;
		}
	}

	public static class CONFIG_REGISTRY {
		/**
		 * 
		 * @param configRegistryClient
		 * @param configRegistryDTO
		 * @return
		 */
		public static ConfigRegistry toConfigRegistry(ConfigRegistryClient configRegistryClient, ConfigRegistryDTO configRegistryDTO) {
			if (configRegistryDTO == null) {
				return null;
			}

			String id = configRegistryDTO.getId();
			String type = configRegistryDTO.getType();
			String name = configRegistryDTO.getName();
			Map<String, Object> properties = configRegistryDTO.getProperties();
			long dateCreated = configRegistryDTO.getDateCreated();
			long dateModified = configRegistryDTO.getDateModified();

			Map<String, Object> properties2 = new TreeMap<String, Object>();
			if (properties != null && !properties.isEmpty()) {
				properties2.putAll(properties);
			}

			ConfigRegistryImpl configRegistry = new ConfigRegistryImpl(configRegistryClient);
			configRegistry.setId(id);
			configRegistry.setType(type);
			configRegistry.setName(name);
			configRegistry.setProperties(properties2);
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
		public static ConfigRegistry[] getConfigRegistries(ConfigRegistryClient configRegistryClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			List<ConfigRegistry> configRegistries = new ArrayList<ConfigRegistry>();
			List<ConfigRegistryDTO> configRegistryDTOs = response.readEntity(new GenericType<List<ConfigRegistryDTO>>() {
			});
			for (ConfigRegistryDTO configRegistryDTO : configRegistryDTOs) {
				ConfigRegistry configRegistry = toConfigRegistry(configRegistryClient, configRegistryDTO);
				if (configRegistry != null) {
					configRegistries.add(configRegistry);
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
		public static ConfigRegistry getConfigRegistry(ConfigRegistryClient configRegistryClient, Response response) throws ClientException {
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
		public static ConfigElement toConfigElement(ConfigRegistryClient configRegistryClient, ConfigElementDTO configElementDTO) {
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

			Map<String, Object> attributes2 = new TreeMap<String, Object>();
			if (attributes != null && !attributes.isEmpty()) {
				attributes2.putAll(attributes);
			}

			ConfigElementImpl configElement = new ConfigElementImpl(configRegistryClient);
			configElement.setConfigRegistryId(configRegistryId);
			configElement.setParentElementId(parentElementId);
			configElement.setElementId(elementId);
			configElement.setPath(path);
			configElement.setAttributes(attributes2);
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
		public static ConfigElement[] getConfigElements(ConfigRegistryClient configRegistryClient, Response response) throws ClientException {
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
		public static ConfigElement getConfigElement(ConfigRegistryClient configRegistryClient, Response response) throws ClientException {
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

	public static class SUBS_SERVER {
		/**
		 * 
		 * @param api
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public static List<ISubsType> toTypes(SubsServerAPI api, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			List<ISubsType> types = new ArrayList<ISubsType>();
			List<SubsTypeDTO> typeDTOs = response.readEntity(new GenericType<List<SubsTypeDTO>>() {
			});
			for (SubsTypeDTO typeDTO : typeDTOs) {
				ISubsType type = toType(api, typeDTO);
				if (type != null) {
					types.add(type);
				}
			}
			return types;
		}

		/**
		 * 
		 * @param api
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public static ISubsType toType(SubsServerAPI api, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			ISubsType type = null;
			SubsTypeDTO typeDTO = response.readEntity(SubsTypeDTO.class);
			if (typeDTO != null) {
				type = toType(api, typeDTO);
			}
			return type;
		}

		/**
		 * 
		 * @param api
		 * @param typeDTO
		 * @return
		 */
		public static ISubsType toType(SubsServerAPI api, SubsTypeDTO typeDTO) {
			if (typeDTO == null) {
				return null;
			}

			Integer id = typeDTO.getId();
			String type = typeDTO.getType();
			String name = typeDTO.getName();
			long dateCreated = typeDTO.getDateCreated();
			long dateModified = typeDTO.getDateModified();

			SubsTypeImpl typeObj = new SubsTypeImpl();
			typeObj.setAPI(api);
			typeObj.setId(id);
			typeObj.setType(type);
			typeObj.setName(name);
			typeObj.setDateCreated(dateCreated);
			typeObj.setDateModified(dateModified);

			return typeObj;
		}

		/**
		 * 
		 * @param api
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public static List<ISubsSource> toSources(SubsServerAPI api, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			List<ISubsSource> sources = new ArrayList<ISubsSource>();
			List<SubsSourceDTO> sourceDTOs = response.readEntity(new GenericType<List<SubsSourceDTO>>() {
			});
			for (SubsSourceDTO sourceDTO : sourceDTOs) {
				ISubsSource source = toSource(api, sourceDTO);
				if (source != null) {
					sources.add(source);
				}
			}
			return sources;
		}

		/**
		 * 
		 * @param api
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public static ISubsSource toSource(SubsServerAPI api, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			ISubsSource source = null;
			SubsSourceDTO sourceDTO = response.readEntity(SubsSourceDTO.class);
			if (sourceDTO != null) {
				source = toSource(api, sourceDTO);
			}
			return source;
		}

		/**
		 * 
		 * @param api
		 * @param sourceDTO
		 * @return
		 */
		public static ISubsSource toSource(SubsServerAPI api, SubsSourceDTO sourceDTO) {
			if (sourceDTO == null) {
				return null;
			}

			Integer id = sourceDTO.getId();
			String type = sourceDTO.getType();
			String instanceId = sourceDTO.getInstanceId();
			String name = sourceDTO.getName();
			Map<String, Object> properties = sourceDTO.getProperties();
			long dateCreated = sourceDTO.getDateCreated();
			long dateModified = sourceDTO.getDateModified();

			Map<String, Object> properties2 = new TreeMap<String, Object>();
			if (properties != null && !properties.isEmpty()) {
				properties2.putAll(properties);
			}

			SubsSourceImpl source = new SubsSourceImpl();
			source.setAPI(api);
			source.setId(id);
			source.setType(type);
			source.setInstanceId(instanceId);
			source.setName(name);
			source.setProperties(properties2);
			source.setDateCreated(dateCreated);
			source.setDateModified(dateModified);

			return source;
		}

		/**
		 * 
		 * @param api
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public static List<ISubsTarget> toTargets(SubsServerAPI api, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			List<ISubsTarget> targets = new ArrayList<ISubsTarget>();
			List<SubsTargetDTO> targetDTOs = response.readEntity(new GenericType<List<SubsTargetDTO>>() {
			});
			for (SubsTargetDTO targetDTO : targetDTOs) {
				ISubsTarget target = toTarget(api, targetDTO);
				if (target != null) {
					targets.add(target);
				}
			}
			return targets;
		}

		/**
		 * 
		 * @param api
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public static ISubsTarget toTarget(SubsServerAPI api, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			ISubsTarget target = null;
			SubsTargetDTO targetDTO = response.readEntity(SubsTargetDTO.class);
			if (targetDTO != null) {
				target = toTarget(api, targetDTO);
			}
			return target;
		}

		/**
		 * 
		 * @param api
		 * @param targetDTO
		 * @return
		 */
		public static ISubsTarget toTarget(SubsServerAPI api, SubsTargetDTO targetDTO) {
			if (targetDTO == null) {
				return null;
			}

			Integer id = targetDTO.getId();
			String type = targetDTO.getType();
			String instanceId = targetDTO.getInstanceId();
			String name = targetDTO.getName();
			String serverId = targetDTO.getServerId();
			String serverURL = targetDTO.getServerURL();
			long serverHeartbeatTime = targetDTO.getServerHeartbeatTime();
			Map<String, Object> properties = targetDTO.getProperties();
			long dateCreated = targetDTO.getDateCreated();
			long dateModified = targetDTO.getDateModified();

			Map<String, Object> properties2 = new TreeMap<String, Object>();
			if (properties != null && !properties.isEmpty()) {
				properties2.putAll(properties);
			}

			SubsTargetImpl target = new SubsTargetImpl();
			target.setAPI(api);
			target.setId(id);
			target.setType(type);
			target.setInstanceId(instanceId);
			target.setName(name);
			target.setServerId(serverId);
			target.setServerURL(serverURL);
			target.setServerHeartbeatTime(serverHeartbeatTime);
			target.setProperties(properties2);
			target.setDateCreated(dateCreated);
			target.setDateModified(dateModified);

			return target;
		}

		/**
		 * 
		 * @param api
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public static List<ISubsMapping> toMappings(SubsServerAPI api, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			List<ISubsMapping> mappings = new ArrayList<ISubsMapping>();
			List<SubsMappingDTO> mappingDTOs = response.readEntity(new GenericType<List<SubsMappingDTO>>() {
			});
			for (SubsMappingDTO mappingDTO : mappingDTOs) {
				ISubsMapping mapping = toMapping(api, mappingDTO);
				if (mapping != null) {
					mappings.add(mapping);
				}
			}
			return mappings;
		}

		/**
		 * 
		 * @param api
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public static ISubsMapping toMapping(SubsServerAPI api, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			ISubsMapping mapping = null;
			SubsMappingDTO mappingDTO = response.readEntity(SubsMappingDTO.class);
			if (mappingDTO != null) {
				mapping = toMapping(api, mappingDTO);
			}
			return mapping;
		}

		/**
		 * 
		 * @param api
		 * @param mappingDTO
		 * @return
		 */
		public static ISubsMapping toMapping(SubsServerAPI api, SubsMappingDTO mappingDTO) {
			if (mappingDTO == null) {
				return null;
			}

			Integer id = mappingDTO.getId();
			Integer sourceId = mappingDTO.getSourceId();
			Integer targetId = mappingDTO.getTargetId();

			String clientId = mappingDTO.getClientId();
			String clientURL = mappingDTO.getClientURL();
			long clientHeartbeatTime = mappingDTO.getClientHeartbeatTime();

			Map<String, Object> properties = mappingDTO.getProperties();
			long dateCreated = mappingDTO.getDateCreated();
			long dateModified = mappingDTO.getDateModified();

			Map<String, Object> properties2 = new TreeMap<String, Object>();
			if (properties != null && !properties.isEmpty()) {
				properties2.putAll(properties);
			}

			SubsMappingImpl mapping = new SubsMappingImpl();
			mapping.setAPI(api);
			mapping.setId(id);
			mapping.setSourceId(sourceId);
			mapping.setTargetId(targetId);
			mapping.setClientId(clientId);
			mapping.setClientURL(clientURL);
			mapping.setClientHeartbeatTime(clientHeartbeatTime);
			mapping.setProperties(properties2);
			mapping.setDateCreated(dateCreated);
			mapping.setDateModified(dateModified);

			return mapping;
		}
	}

	public static class COMMON {
		/**
		 * 
		 * @param pathDTO
		 * @return
		 */
		public static Path toPath(PathDTO pathDTO) {
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
		public static Path getPath(Response response) throws ClientException {
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
		public static boolean isUpdated(Response response) throws ClientException {
			return ResponseUtil.isSucceed(response);
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public static boolean isDeleted(Response response) throws ClientException {
			return ResponseUtil.isSucceed(response);
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public static boolean isUploaded(Response response) throws ClientException {
			return ResponseUtil.isSucceed(response);
		}

		/**
		 * 
		 * @param properties
		 * @return
		 */
		public static String toPropertiesString(Map<String, Object> properties) {
			String propertiesString = JSONUtil.toJsonString(properties);
			return propertiesString;
		}

		/**
		 * 
		 * @param propertiesString
		 * @return
		 */
		public static Map<String, Object> toProperties(String propertiesString) {
			Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);
			return properties;
		}

		/**
		 * 
		 * @param propertiesString
		 * @return
		 */
		public static Map<String, Object> toMap(String mapString) {
			Map<String, Object> map = JSONUtil.toProperties(mapString, true);
			return map;
		}

		/**
		 * 
		 * @param map
		 * @return
		 */
		public static String toMapString(Map<String, Object> map) {
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
