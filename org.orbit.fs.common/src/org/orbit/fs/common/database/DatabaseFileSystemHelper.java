package org.orbit.fs.common.database;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.orbit.fs.common.Constants;
import org.orbit.fs.model.FileMetadata;
import org.orbit.fs.model.vo.FileContentVO;
import org.orbit.fs.model.vo.FileMetadataVO;
import org.origin.common.io.IOUtil;
import org.origin.common.resource.IPath;

public class DatabaseFileSystemHelper {

	protected FileMetadataTableHandler fileMetadataHandler;
	protected FileContentTableHandler fileContentHandler;

	public DatabaseFileSystemHelper() {
		this(null, null);
	}

	public DatabaseFileSystemHelper(String metadataTableName, String contentTableName) {
		if (metadataTableName == null || metadataTableName.isEmpty()) {
			metadataTableName = Constants.METADATA_TABLE_NAME_DEFAULT_VALUE;
		}
		if (contentTableName == null || contentTableName.isEmpty()) {
			contentTableName = Constants.CONTENT_TABLE_NAME_DEFAULT_VALUE;
		}

		this.fileMetadataHandler = new FileMetadataTableHandler(metadataTableName);
		this.fileContentHandler = new FileContentTableHandler(contentTableName);
	}

	public FileMetadataTableHandler getFileMetadataHandler() {
		return this.fileMetadataHandler;
	}

	public FileContentTableHandler getFileContentHandler() {
		return this.fileContentHandler;
	}

	/**
	 * Force delete a file or a directory by file id.
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, int fileId) throws SQLException {
		if (!fileMetadataHandler.hasRecord(conn, fileId)) {
			return false;
		}
		List<Integer> encounteredFileIds = new ArrayList<Integer>();
		doDelete(conn, fileId, encounteredFileIds);
		return true;
	}

	private void doDelete(Connection conn, int fileId, List<Integer> encounteredFileIds) throws SQLException {
		if (encounteredFileIds.contains(fileId)) {
			return;
		} else {
			encounteredFileIds.add(fileId);
		}

		// if the file is a directory, delete all its member files first.
		if (fileMetadataHandler.isDirectory(conn, fileId)) {
			// current directory is already in trash, any file in it will be deleted.
			List<FileMetadataVO> vos = fileMetadataHandler.get(conn, fileId);
			if (vos != null) {
				for (FileMetadataVO vo : vos) {
					int memberFileId = vo.getFileId();
					doDelete(conn, memberFileId, encounteredFileIds);
				}
			}
		}

		// deletes the file content record from the FsFileContent table by file id.
		fileContentHandler.deleteByFileId(conn, fileId);

		// deletes the file metadata record from the FsFileMetadata table by file id.
		fileMetadataHandler.delete(conn, fileId);
	}

	/**
	 * Move a file or a directory to trash.
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 */
	public boolean moveToTrash(Connection conn, int fileId) throws SQLException {
		if (!fileMetadataHandler.hasRecord(conn, fileId)) {
			return false;
		}
		List<Integer> encounteredFileIds = new ArrayList<Integer>();
		doMoveToOrRestoreFromTrash(conn, fileId, true, encounteredFileIds);
		return true;
	}

	/**
	 * Restore a file or a directory from trash.
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 */
	public boolean restoreFromTrash(Connection conn, int fileId) throws SQLException {
		if (!fileMetadataHandler.hasRecord(conn, fileId)) {
			return false;
		}
		List<Integer> encounteredFileIds = new ArrayList<Integer>();
		doMoveToOrRestoreFromTrash(conn, fileId, false, encounteredFileIds);
		return true;
	}

	/**
	 * Internal method to recursively update file record's inTrash value.
	 * 
	 * @param conn
	 * @param fileId
	 * @param inTrash
	 *            value to be set. if true, the file will be moved to trash. if false, the file will be restored from trash.
	 * @param encounteredFileIds
	 * @throws SQLException
	 */
	private void doMoveToOrRestoreFromTrash(Connection conn, int fileId, boolean inTrash, List<Integer> encounteredFileIds) throws SQLException {
		if (encounteredFileIds.contains(fileId)) {
			return;
		} else {
			encounteredFileIds.add(fileId);
		}

		if (inTrash && fileMetadataHandler.isInTrash(conn, fileId)) {
			// already in trash. no need to move to trash.
			return;
		}
		if (!inTrash && !fileMetadataHandler.isInTrash(conn, fileId)) {
			// already not in trash. no need to restore from trash.
			return;
		}

		// update file record either move to trash (inTrash is true) or restore from trash (inTrash is false).
		fileMetadataHandler.updateInTrash(conn, fileId, inTrash);

		// recursively find member files and update each of them.
		if (fileMetadataHandler.isDirectory(conn, fileId)) {
			List<FileMetadataVO> vos = fileMetadataHandler.get(conn, fileId);
			if (vos != null) {
				for (FileMetadataVO vo : vos) {
					int memberFileId = vo.getFileId();
					doMoveToOrRestoreFromTrash(conn, memberFileId, inTrash, encounteredFileIds);
				}
			}
		}
	}

	/**
	 * Write a input stream to file content table by file id.
	 * 
	 * @param conn
	 * @param fileId
	 * @param inputStream
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public boolean writeFileContentPostgres(Connection conn, int fileId, InputStream inputStream) throws SQLException, IOException {
		FileContentVO vo = null;
		if (fileContentHandler.hasRecordByFileId(conn, fileId)) {
			vo = fileContentHandler.getByFileId(conn, fileId);
		} else {
			vo = fileContentHandler.insert(conn, fileId);
		}

		if (vo != null) {
			int fileContentId = vo.getFileContentId();

			byte[] bytes = IOUtil.toByteArray(inputStream);
			int bytesLength = bytes.length;

			ByteArrayInputStream bais = null;
			try {
				bais = new ByteArrayInputStream(bytes);
				boolean succeed = fileContentHandler.writeFileContentPostgres(conn, fileContentId, bais, bytesLength);
				if (succeed) {
					fileMetadataHandler.updateLength(conn, fileId, bytesLength);
					return true;
				}
			} finally {
				IOUtil.closeQuietly(bais, true);
			}
		}
		return false;
	}

	/**
	 * Write a input stream to file content table by file id.
	 * 
	 * @param conn
	 * @param fileId
	 * @param inputStream
	 * @param fileLength
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public boolean writeFileContentMySQL(Connection conn, int fileId, InputStream inputStream, long fileLength) throws SQLException, IOException {
		FileContentVO vo = null;
		if (fileContentHandler.hasRecordByFileId(conn, fileId)) {
			vo = fileContentHandler.getByFileId(conn, fileId);
		} else {
			vo = fileContentHandler.insert(conn, fileId);
		}

		if (vo != null) {
			int fileContentId = vo.getFileContentId();

			// byte[] bytes = IOUtil.toByteArray(inputStream);
			// int bytesLength = bytes.length;
			// System.out.println("bytesLength=" + bytesLength + ", fileLength=" + fileLength);

			// ByteArrayInputStream bais = null;
			try {
				// bais = new ByteArrayInputStream(bytes);
				boolean succeed = fileContentHandler.writeFileContent(conn, fileContentId, inputStream);
				// boolean succeed = fileContentHandler.writeFileContent(conn, fileContentId, bytes);
				if (succeed) {
					// fileMetadataHandler.updateLength(conn, fileId, bytesLength);
					fileMetadataHandler.updateLength(conn, fileId, fileLength);
					return true;
				}
			} finally {
				// IOUtil.closeQuietly(bais, true);
			}
		}
		return false;
	}

	/**
	 * Read bytes array from file content table by file id.
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public byte[] readFileContentPostgres(Connection conn, int fileId) throws SQLException, IOException {
		byte[] bytes = null;
		FileContentVO vo = null;
		if (fileContentHandler.hasRecordByFileId(conn, fileId)) {
			vo = fileContentHandler.getByFileId(conn, fileId);
		} else {
			vo = fileContentHandler.insert(conn, fileId);
		}
		if (vo != null) {
			int fileContentId = vo.getFileContentId();
			bytes = fileContentHandler.readFileContentPostgres(conn, fileContentId);
		}
		return bytes;
	}

	/**
	 * Read bytes array from file content table by file id.
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public InputStream readFileContentMySQL(Connection conn, int fileId) throws SQLException, IOException {
		// byte[] bytes = null;
		FileContentVO vo = null;
		if (fileContentHandler.hasRecordByFileId(conn, fileId)) {
			vo = fileContentHandler.getByFileId(conn, fileId);
		} else {
			vo = fileContentHandler.insert(conn, fileId);
		}
		if (vo != null) {
			int fileContentId = vo.getFileContentId();
			// bytes = fileContentHandler.readFileContent(conn, fileContentId);
			return fileContentHandler.readFileContent(conn, fileContentId);
		}
		// return bytes;
		return null;
	}

	/**
	 * Get all files and sub-directories within a directory.
	 * 
	 * @param conn
	 * @param parentFileId
	 * @param includeSubDirectories
	 * @return
	 * @throws SQLException
	 */
	public List<FileMetadataVO> getAllFilesAndDirectories(Connection conn, int parentFileId, boolean includeSubDirectories) throws SQLException {
		List<FileMetadataVO> collectedVOs = new ArrayList<FileMetadataVO>();
		List<Integer> encounteredParentFileIds = new ArrayList<Integer>();
		doGetAllFilesAndDirectories(conn, parentFileId, includeSubDirectories, collectedVOs, encounteredParentFileIds);
		return collectedVOs;
	}

	private void doGetAllFilesAndDirectories(Connection conn, int parentFileId, boolean includeSubDirectories, List<FileMetadataVO> collectedVOs, List<Integer> encounteredParentFileIds) throws SQLException {
		if (encounteredParentFileIds.contains(parentFileId)) {
			return;
		}
		encounteredParentFileIds.add(parentFileId);

		List<FileMetadataVO> vos = fileMetadataHandler.get(conn, parentFileId);
		if (vos != null) {
			for (FileMetadataVO vo : vos) {
				if (!collectedVOs.contains(vo)) {
					collectedVOs.add(vo);
				}
				if (includeSubDirectories) {
					if (vo.isDirectory()) {
						doGetAllFilesAndDirectories(conn, vo.getFileId(), includeSubDirectories, collectedVOs, encounteredParentFileIds);
					}
				}
			}
		}
	}

	/**
	 * Get all files within a directory.
	 * 
	 * @param conn
	 * @param parentFileId
	 * @param includeSubDirectories
	 * @return
	 * @throws SQLException
	 */
	public List<FileMetadataVO> getAllFiles(Connection conn, int parentFileId, boolean includeSubDirectories) throws SQLException {
		List<FileMetadataVO> collectedVOs = new ArrayList<FileMetadataVO>();
		List<Integer> encounteredParentFileIds = new ArrayList<Integer>();
		doGetAllFiles(conn, parentFileId, includeSubDirectories, collectedVOs, encounteredParentFileIds);
		return collectedVOs;
	}

	private void doGetAllFiles(Connection conn, int parentFileId, boolean includeSubDirectories, List<FileMetadataVO> collectedVOs, List<Integer> encounteredParentFileIds) throws SQLException {
		if (encounteredParentFileIds.contains(parentFileId)) {
			return;
		}
		encounteredParentFileIds.add(parentFileId);

		List<FileMetadataVO> vos = fileMetadataHandler.get(conn, parentFileId);
		if (vos != null) {
			for (FileMetadataVO vo : vos) {
				if (!vo.isDirectory()) {
					if (!collectedVOs.contains(vo)) {
						collectedVOs.add(vo);
					}
				}
				if (includeSubDirectories) {
					if (vo.isDirectory()) {
						doGetAllFiles(conn, vo.getFileId(), includeSubDirectories, collectedVOs, encounteredParentFileIds);
					}
				}
			}
		}
	}

	/**
	 * Get all sub-directories within a directory.
	 * 
	 * @param conn
	 * @param parentFileId
	 * @param includeSubDirectories
	 * @return
	 * @throws SQLException
	 */
	public List<FileMetadataVO> getAllDirectories(Connection conn, int parentFileId, boolean includeSubDirectories) throws SQLException {
		List<FileMetadataVO> collectedVOs = new ArrayList<FileMetadataVO>();
		List<Integer> encounteredParentFileIds = new ArrayList<Integer>();
		doGetAllDirectories(conn, parentFileId, includeSubDirectories, collectedVOs, encounteredParentFileIds);
		return collectedVOs;
	}

	private void doGetAllDirectories(Connection conn, int parentFileId, boolean includeSubDirectories, List<FileMetadataVO> collectedVOs, List<Integer> encounteredParentFileIds) throws SQLException {
		if (encounteredParentFileIds.contains(parentFileId)) {
			return;
		}
		encounteredParentFileIds.add(parentFileId);

		List<FileMetadataVO> vos = fileMetadataHandler.get(conn, parentFileId);
		if (vos != null) {
			for (FileMetadataVO vo : vos) {
				if (vo.isDirectory()) {
					if (!collectedVOs.contains(vo)) {
						collectedVOs.add(vo);
					}
				}
				if (includeSubDirectories) {
					if (vo.isDirectory()) {
						doGetAllDirectories(conn, vo.getFileId(), includeSubDirectories, collectedVOs, encounteredParentFileIds);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param path
	 * @param vo
	 * @return
	 */
	public FileMetadata createFileMetadata(IPath path, FileMetadataVO vo) {
		FileMetadata metadata = new FileMetadata();
		metadata.setExists(true);
		metadata.setFileId(vo.getFileId());
		metadata.setParentFileId(vo.getParentFileId());
		metadata.setName(vo.getName());
		metadata.setIsDirectory(vo.isDirectory());
		metadata.setHidden(vo.isHidden());
		metadata.setPath(path.getPathString());
		if (path.getParent() != null) {
			metadata.setParentPath(path.getParent().getPathString());
		}
		metadata.setCanExecute(vo.canExecute());
		metadata.setCanRead(vo.canRead());
		metadata.setCanWrite(vo.canWrite());
		metadata.setLength(vo.getLength());
		metadata.setLastModified(vo.getLastModified());
		return metadata;
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public FileMetadata createNonExistsFileMetadata(IPath path) {
		FileMetadata metadata = new FileMetadata();
		metadata.setExists(false);
		metadata.setName(path.getLastSegment());
		metadata.setIsDirectory(false);
		metadata.setHidden(false);
		metadata.setPath(path.getPathString());
		if (path.getParent() != null) {
			metadata.setParentPath(path.getParent().getPathString());
		}
		metadata.setCanExecute(false);
		metadata.setCanRead(false);
		metadata.setCanWrite(false);
		metadata.setLength(-1);
		metadata.setLastModified(-1);
		return metadata;
	}

}
