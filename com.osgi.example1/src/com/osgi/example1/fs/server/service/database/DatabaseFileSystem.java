package com.osgi.example1.fs.server.service.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.io.IOUtil;
import org.origin.common.jdbc.DatabaseUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.osgi.example1.fs.common.Configuration;
import com.osgi.example1.fs.common.FileMetadata;
import com.osgi.example1.fs.common.Path;
import com.osgi.example1.fs.common.vo.FileContentVO;
import com.osgi.example1.fs.common.vo.FileMetadataVO;
import com.osgi.example1.fs.server.service.FileSystem;

public class DatabaseFileSystem implements FileSystem {

	protected static final Path[] EMPTY_PATHS = new Path[0];

	protected DatabaseFileSystemConfiguration config;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();
	protected ServiceRegistration<?> serviceReg;

	/**
	 * 
	 * @param config
	 */
	public DatabaseFileSystem(DatabaseFileSystemConfiguration config) {
		this.config = config;
	}

	@Override
	public void start() {
		BundleContext bundleContext = getAdapter(BundleContext.class);
		if (bundleContext != null) {
			// Register as a service
			Hashtable<String, Object> props = new Hashtable<String, Object>();
			this.serviceReg = bundleContext.registerService(FileSystem.class, this, props);
		}
	}

	@Override
	public void stop() {
		// Unregister the service
		if (this.serviceReg != null) {
			this.serviceReg.unregister();
			this.serviceReg = null;
		}
	}

	protected Connection getConnection() {
		return this.config.getConnection();
	}

	public static FsFileMetadataTableHandler getFileMetadataHandler() {
		return FsTableUtil.getFileMetadataHandler();
	}

	public static FsFileContentTableHandler getFileContentHandler() {
		return FsTableUtil.getFileContentHandler();
	}

	@Override
	public Configuration getConfiguration() {
		return this.config;
	}

	@Override
	public FileMetadata getFileMetaData(Path path) {
		Connection conn = getConnection();
		try {
			String[] segments = path.getSegments();
			if (segments == null || segments.length == 0) {
				// empty path --- which belongs to relative path --- which cannot be used to resolve a file --- return non-exists FileMetadata.
				return FsTableUtil.createNonExistsFileMetadata(path);
			}

			int currParentFileId = -1;

			for (int i = 0; i < segments.length; i++) {
				String dirName = segments[i];
				boolean isLastSegment = (i == (segments.length - 1)) ? true : false;

				FileMetadataVO vo = getFileMetadataHandler().getByName(conn, currParentFileId, dirName);
				if (vo == null) {
					// file record cannot be found --- file doesn't exist --- return non-exists FileMetadata.
					return FsTableUtil.createNonExistsFileMetadata(path);
				}

				if (!isLastSegment) {
					// not the last segment yet --- continue to look for the next segment until the last segment --- which is the file name.
					currParentFileId = vo.getFileId();
				} else {
					// file record is found --- file exists --- create FileMetadata from vo and return the FileMetadata.
					return FsTableUtil.createFileMetadata(path, vo);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		// file is not found --- return non-exists FileMetadata.
		return FsTableUtil.createNonExistsFileMetadata(path);
	}

	@Override
	public Path[] listRoots() {
		Connection conn = getConnection();
		try {
			// get member files in the root folder (not including in-trash file records)
			List<FileMetadataVO> rootVOs = getFileMetadataHandler().get(conn, -1, false);
			if (rootVOs != null) {
				Path[] paths = new Path[rootVOs.size()];
				for (int i = 0; i < rootVOs.size(); i++) {
					FileMetadataVO rootVO = rootVOs.get(i);
					paths[i] = new Path(Path.SEPARATOR + rootVO.getName());
				}
				return paths;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return EMPTY_PATHS;
	}

	@Override
	public Path[] listFiles(Path parent) {
		if (parent.isRoot()) {
			// parent path is "/" --- which is root path --- return root files path
			return listRoots();
		}

		if (parent.isEmpty()) {
			// empty path --- which belongs to relative path --- which does not have member files --- return empty paths array
			return EMPTY_PATHS;
		}

		Connection conn = getConnection();
		try {
			int currParentFileId = -1;

			String[] segments = parent.getSegments();
			for (int i = 0; i < segments.length; i++) {
				String dirName = segments[i];
				boolean isLastSegment = (i == (segments.length - 1)) ? true : false;

				FileMetadataVO parentVO = getFileMetadataHandler().getByName(conn, currParentFileId, dirName);
				if (parentVO == null) {
					// parent file record cannot be found --- which doesn't exist --- which does not have member files
					return EMPTY_PATHS;
				}
				if (!parentVO.isDirectory()) {
					// parent file is not directory --- which does not have member files
					return EMPTY_PATHS;
				}

				if (!isLastSegment) {
					// not the last segment yet --- continue to look for the next segment until the last segment
					currParentFileId = parentVO.getFileId();

				} else {
					// parent file record is found --- parent file exists --- get member files (not including in-trash file records)
					List<FileMetadataVO> memberVOs = getFileMetadataHandler().get(conn, parentVO.getFileId(), false);
					if (memberVOs != null) {
						Path[] paths = new Path[memberVOs.size()];
						for (int j = 0; j < memberVOs.size(); j++) {
							FileMetadataVO memberVO = memberVOs.get(j);
							paths[j] = new Path(parent, memberVO.getName());
						}
						return paths;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		// file is not found --- return empty paths array
		return EMPTY_PATHS;
	}

	@Override
	public boolean exists(Path path) {
		if (path.isEmpty()) {
			// empty path --- which belongs to relative path --- which cannot be used to resolve a file --- equivalent to file doesn't exist.
			return false;
		}

		Connection conn = getConnection();
		try {
			int currParentFileId = -1;

			String[] segments = path.getSegments();
			for (int i = 0; i < segments.length; i++) {
				String segment = segments[i];
				boolean isLastSegment = (i == (segments.length - 1)) ? true : false;

				FileMetadataVO vo = getFileMetadataHandler().getByName(conn, currParentFileId, segment);
				if (vo == null) {
					// the file record for one of the parent file cannot be found --- target file doesn't exist.
					return false;
				}

				if (!isLastSegment) {
					// not the last segment yet --- continue to look for the next segment until the last segment
					currParentFileId = vo.getFileId();

				} else {
					// file record for the file is found --- target file exists
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		// still not found --- file doesn't exist.
		return false;
	}

	@Override
	public boolean isDirectory(Path path) {
		FileMetadata metadata = getFileMetaData(path);
		return (metadata != null && metadata.isDirectory()) ? true : false;
	}

	@Override
	public boolean mkdirs(Path path) throws IOException {
		if (path.isEmpty()) {
			// empty path --- which belongs to relative path --- which cannot be used to locate file --- cannot create directories.
			return false;
		}

		Connection conn = getConnection();
		try {
			int currParentFileId = -1;

			String[] segments = path.getSegments();
			for (int i = 0; i < segments.length; i++) {
				String segment = segments[i];
				boolean isLastSegment = (i == (segments.length - 1)) ? true : false;
				String currPathString = path.getPathString(0, i + 1);

				FileMetadataVO vo = getFileMetadataHandler().getByName(conn, currParentFileId, segment);

				if (vo == null) {
					// file record for the current directory doesn't exist --- create a file record for the directory.
					vo = getFileMetadataHandler().insert(conn, currParentFileId, segment, true, 0);
				}
				if (vo == null) {
					// file record is not created --- failed to create current directory
					throw new IOException("Directory '" + currPathString + "' cannot be created.");
				}
				if (!vo.isDirectory()) {
					// file exist, but is not a directory --- invalid path parameter.
					throw new IOException("Path '" + currPathString + "' already exists but is not a directory.");
				}

				if (!isLastSegment) {
					// not last segment --- expected to be a directory

					// not the last segment yet --- continue to look for the next segment until the last path segment
					currParentFileId = vo.getFileId();

				} else {
					// is last segment --- expected to be a directory

					// Note:
					// not like create new file, if target directory already exists, just return true.
					// so that any client code that expects the directory to exist can continue.
					// They just want to make sure the directory exists.

					// current directory is the target directory --- all directories are created.
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		return false;
	}

	@Override
	public boolean createNewFile(Path path) throws IOException {
		// get or create file record for any directories in the path. then create the file record for the file (file or directory) itself.
		// throw IOException if file (file or directory) already exists.

		if (path.isEmpty()) {
			// empty path --- which belongs to relative path --- which cannot be used to locate file --- cannot create new file for relative path.
			return false;
		}

		Connection conn = getConnection();
		try {
			int currParentFileId = -1;

			String[] segments = path.getSegments();
			for (int i = 0; i < segments.length; i++) {
				String segment = segments[i];
				boolean isLastSegment = (i == (segments.length - 1)) ? true : false;
				String currPathString = path.getPathString(0, i + 1);

				FileMetadataVO vo = getFileMetadataHandler().getByName(conn, currParentFileId, segment);

				if (!isLastSegment) {
					// not last segment --- expected to be a directory

					if (vo == null) {
						// file record not exists --- create file record for the current directory
						vo = getFileMetadataHandler().insert(conn, currParentFileId, segment, true, 0);
					}
					if (vo == null) {
						// file record is not created --- failed to create the new file
						throw new IOException("Directory '" + currPathString + "' cannot be created.");
					}
					if (!vo.isDirectory()) {
						// file record exists, but is not a directory --- invalid path parameter.
						throw new IOException("Path '" + currPathString + "' already exists but is not a directory.");
					}

					// not the last segment yet --- continue to look for the next segment until the last path segment
					currParentFileId = vo.getFileId();

				} else {
					// is last segment --- expected to be a file

					// if file record already exists --- cannot create new file
					if (vo != null) {
						if (vo.isDirectory()) {
							throw new IOException("Directory '" + currPathString + "' already exists.");
						} else {
							throw new IOException("File '" + currPathString + "' already exists.");
						}
					}

					// file record not exists --- create a new file record for the file
					vo = getFileMetadataHandler().insert(conn, currParentFileId, segment, false, 0);
					if (vo == null) {
						// file record is not created --- failed to create the new file
						throw new IOException("File '" + currPathString + "' cannot be created.");
					}

					// new file is created.
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean delete(Path path) throws IOException {
		if (path.isRoot()) {
			// root path --- there is no file record for root --- root path itself cannot be delete.
			System.err.println("Path is root - '/'. Cannot delete the root path itself.");
			return false;
		}
		if (path.isEmpty()) {
			// empty path --- which belongs to relative path --- which cannot be used to locate a file --- no file record is deleted.
			System.err.println("Path is empty.");
			return false;
		}
		if (!exists(path)) {
			// file record for the given path doesn't exist --- no file record is deleted.
			System.err.println("Path '" + path.getPathString() + "' does not exist.");
			return false;
		}

		Connection conn = getConnection();
		try {
			int currParentFileId = -1;

			String[] segments = path.getSegments();
			for (int i = 0; i < segments.length; i++) {
				String segment = segments[i];
				boolean isLastSegment = (i == (segments.length - 1)) ? true : false;

				FileMetadataVO vo = getFileMetadataHandler().getByName(conn, currParentFileId, segment);
				if (vo == null) {
					// the file record for one of the parent file or the target file itself cannot be found --- target file doesn't exist.
					System.err.println("Path '" + path.getPathString(0, i + 1) + "' does not exist.");
					return false;
				}

				if (!isLastSegment) {
					// not last segment --- continue to look for the next segment until the last segment
					currParentFileId = vo.getFileId();

				} else {
					// is last segment --- expected to be a directory or a file
					return FsTableUtil.delete(conn, vo.getFileId());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		// no file record is deleted.
		return false;
	}

	@Override
	public InputStream getInputStream(Path path) throws IOException {
		if (path.isRoot() || path.isEmpty()) {
			// root path --- cannot be used to locate a file --- cannot get input stream
			// empty path --- belongs to relative path --- cannot be used to locate a file --- cannot get input stream
			return null;
		}

		Connection conn = getConnection();
		try {
			int currParentFileId = -1;

			String[] segments = path.getSegments();
			for (int i = 0; i < segments.length; i++) {
				String segment = segments[i];
				boolean isLastSegment = (i == (segments.length - 1)) ? true : false;

				FileMetadataVO vo = getFileMetadataHandler().getByName(conn, currParentFileId, segment);

				if (!isLastSegment) {
					// not last segment --- expected to be a directory

					// Note:
					// this method only read input stream from the target file. it is not this method's job to make sure file records for all parent
					// directories exist. there is no need to check and create file records for parent directories.

					// not the last segment yet --- continue to look for the next segment until the last path segment
					currParentFileId = vo.getFileId();

				} else {
					// is last segment --- expected to be a file

					if (vo == null) {
						// file record not exists --- file doesn't exist --- cannot get input stream
						return null;
					}
					if (vo.isDirectory()) {
						// file record exists but is a directory --- cannot get input stream from a directory
						return null;
					}

					FileContentVO contentVO = getFileContentHandler().getByFileId(conn, vo.getFileId());
					if (contentVO == null) {
						// file content record is not found --- file does not have content yet --- cannot get input stream
						return null;
					}

					int fileContentId = contentVO.getFileContentId();

					// TODO: get database name from Connection and determine which method to call to get input stream of the file content.
					// if using other database
					// return getFileContentHandler().readFileContent(conn, fileContentId);

					// using Postgres database
					byte[] bytes = getFileContentHandler().readFileContentPostgres(conn, fileContentId);
					if (bytes != null) {
						return IOUtil.toInputStream(bytes);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		// cannot get input stream
		return null;
	}

	@Override
	public Path copyInputStreamToFsFile(InputStream inputStream, Path destFilePath) throws IOException {
		// Check inputStream
		if (inputStream == null) {
			throw new IOException("InputStream is null.");
		}

		// Check target file
		Connection conn = getConnection();
		try {
			if (destFilePath.isEmpty()) {
				throw new IOException("Path '" + destFilePath.getPathString() + "' is empty.");
			}

			int currParentFileId = -1;

			String[] segments = destFilePath.getSegments();
			for (int i = 0; i < segments.length; i++) {
				String segment = segments[i];
				boolean isLastSegment = (i == (segments.length - 1)) ? true : false;
				String currPathString = destFilePath.getPathString(0, i + 1);

				FileMetadataVO vo = getFileMetadataHandler().getByName(conn, currParentFileId, segment);

				if (!isLastSegment) {
					// not last segment --- expected to be a directory

					// Note:
					// Writing data to file. need to make sure parent directories exist.

					// make sure file record exists
					if (vo == null) {
						// file record not exists --- create file record for the current directory
						vo = getFileMetadataHandler().insert(conn, currParentFileId, segment, true, 0);
					}
					if (vo == null) {
						// file record is not created --- failed to create the new file
						throw new IOException("Path " + currPathString + " cannot be created.");
					}
					if (!vo.isDirectory()) {
						// file record exists, but is not a directory --- invalid path parameter.
						throw new IOException("Path '" + currPathString + "' exists but is not a directory.");
					}

					// not the last segment yet --- continue to look for the next segment until the last path segment
					currParentFileId = vo.getFileId();

				} else {
					// is last segment --- expected to be a file

					// make sure file record exists
					if (vo == null) {
						vo = getFileMetadataHandler().insert(conn, currParentFileId, segment, false, 0);
					}
					if (vo == null) {
						// file record is not created --- failed to create the new file
						throw new IOException("Path '" + currPathString + "' cannot be created.");
					}
					if (vo.isDirectory()) {
						// target file is a directory --- target is expected to be a file --- invalid path.
						throw new IOException("Path '" + currPathString + "' exists but is a directory.");
					}

					// TODO: get database name from Connection and determine which method to call to write file content into.

					// using Postgres database
					FsTableUtil.writeFileContentPostgres(conn, vo.getFileId(), inputStream);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		return destFilePath;
	}

	@Override
	public Path copyFileToFsFile(File localFile, Path destFilePath) throws IOException {
		// Check source file
		if (!localFile.exists()) {
			throw new IOException("Local file '" + localFile + "' does not exist.");
		}
		if (localFile.isDirectory()) {
			throw new IOException("Local file '" + localFile + "' exists but is a directory.");
		}

		// Check target file
		Connection conn = getConnection();
		try {
			if (destFilePath.isEmpty()) {
				throw new IOException("Path '" + destFilePath.getPathString() + "' is empty.");
			}

			int currParentFileId = -1;

			String[] segments = destFilePath.getSegments();
			for (int i = 0; i < segments.length; i++) {
				String segment = segments[i];
				boolean isLastSegment = (i == (segments.length - 1)) ? true : false;
				String currPathString = destFilePath.getPathString(0, i + 1);

				FileMetadataVO vo = getFileMetadataHandler().getByName(conn, currParentFileId, segment);

				if (!isLastSegment) {
					// not last segment --- expected to be a directory

					// Note:
					// Writing data to file. need to make sure parent directories exist.

					// make sure file record exists
					if (vo == null) {
						// file record not exists --- create file record for the current directory
						vo = getFileMetadataHandler().insert(conn, currParentFileId, segment, true, 0);
					}
					if (vo == null) {
						// file record is not created --- failed to create the new file
						throw new IOException("Path " + currPathString + " cannot be created.");
					}
					if (!vo.isDirectory()) {
						// file record exists, but is not a directory --- invalid path parameter.
						throw new IOException("Path '" + currPathString + "' exists but is not a directory.");
					}

					// not the last segment yet --- continue to look for the next segment until the last path segment
					currParentFileId = vo.getFileId();

				} else {
					// is last segment --- expected to be a file

					// make sure file record exists
					if (vo == null) {
						vo = getFileMetadataHandler().insert(conn, currParentFileId, segment, false, 0);
					}
					if (vo == null) {
						// file record is not created --- failed to create the new file
						throw new IOException("Path '" + currPathString + "' cannot be created.");
					}
					if (vo.isDirectory()) {
						// target file is a directory --- target is expected to be a file --- invalid path.
						throw new IOException("Path '" + currPathString + "' exists but is a directory.");
					}

					// TODO: get database name from Connection and determine which method to call to write file content into.

					// using Postgres database
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(localFile);
						FsTableUtil.writeFileContentPostgres(conn, vo.getFileId(), fis);
					} finally {
						IOUtil.closeQuietly(fis, true);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		return destFilePath;
	}

	@Override
	public Path copyFileToFsDirectory(File localFile, Path destDirPath) throws IOException {
		Path destFilePath = new Path(destDirPath, localFile.getName());
		return copyFileToFsFile(localFile, destFilePath);
	}

	@Override
	public boolean copyDirectoryToFsDirectory(File localDir, Path destDirPath, boolean includingSourceDir) throws IOException {
		// Check source directory
		if (!localDir.exists()) {
			throw new IOException("Local directory '" + localDir + "' does not exist.");
		}
		if (!localDir.isDirectory()) {
			throw new IOException("Local directory '" + localDir + "' exists but is not a directory.");
		}

		if (includingSourceDir) {
			destDirPath = new Path(destDirPath, localDir.getName());
		}

		// Recursively copy every file from the localDir to the destDirPath.
		List<File> encounteredFiles = new ArrayList<File>();
		File[] memberFiles = localDir.listFiles();
		for (File memberFile : memberFiles) {
			doCopyLocalFileOrDirectoryToFsDirectory(memberFile, destDirPath, encounteredFiles);
		}

		return true;
	}

	/**
	 * 
	 * @param localFile
	 * @param destDirPath
	 * @param encounteredFiles
	 * @throws IOException
	 */
	protected void doCopyLocalFileOrDirectoryToFsDirectory(File localFile, Path destDirPath, List<File> encounteredFiles) throws IOException {
		if (localFile == null || encounteredFiles.contains(localFile)) {
			return;
		} else {
			encounteredFiles.add(localFile);
		}

		if (localFile.isDirectory()) {
			Path newDestDirPath = new Path(destDirPath, localFile.getName());
			mkdirs(newDestDirPath);

			File[] memberFiles = localFile.listFiles();
			for (File memberFile : memberFiles) {
				doCopyLocalFileOrDirectoryToFsDirectory(memberFile, newDestDirPath, encounteredFiles);
			}
		} else {
			copyFileToFsDirectory(localFile, destDirPath);
		}
	}

	/** implement IAdaptable interface */
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		adaptorSupport.adapt(clazz, object);
	}

}
