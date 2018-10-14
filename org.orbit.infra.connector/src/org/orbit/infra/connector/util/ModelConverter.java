package org.orbit.infra.connector.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.orbit.infra.api.datacast.DataCastClient;
import org.orbit.infra.api.datacast.DataTubeConfig;
import org.orbit.infra.connector.datacast.DataTubeConfigImpl;
import org.orbit.infra.model.datacast.DataTubeConfigDTO;
import org.origin.common.json.JSONUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;

public class ModelConverter {

	public static DataCast DataCast = new DataCast();
	public static DataTube DataTube = new DataTube();

	public static class DataCast {
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
	}

	public static class DataTube {
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
	}

}

// long size = dto.getSize();
// int startIndex = dto.getStartIndex();
// int endIndex = dto.getEndIndex();
// metadata.setSize(size);
// metadata.setStartIndex(startIndex);
// metadata.setEndIndex(endIndex);
