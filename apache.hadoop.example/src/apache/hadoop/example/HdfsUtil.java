package apache.hadoop.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

/**
 * HDFS utilities class.
 *
 */
public class HdfsUtil {

	public static final Path ROOT = new Path("/");

	/**
	 * Create HDFS Configuration.
	 * 
	 * e.g.
	 * 
	 * conf.addResource(new Path("/Users/yayang/apache/hadoop/hadoop-2.7.1/etc/hadoop/core-site.xml"));
	 * 
	 * conf.addResource(new Path("/Users/yayang/apache/hadoop/hadoop-2.7.1/etc/hadoop/hdfs-site.xml"));
	 * 
	 * conf.addResource(new Path("/Users/yayang/apache/hadoop/hadoop-2.7.1/etc/hadoop/mapred-site.xml"));
	 * 
	 * conf.set("fs.default.name", "hdfs://localhost:9000");
	 * 
	 * @param core_site_xml_path
	 * @return
	 */
	public static Configuration getConfiguration(String... core_site_xml_path) {
		Configuration conf = new Configuration();
		for (String path : core_site_xml_path) {
			conf.addResource(new Path(path));
		}
		return conf;
	}

	/**
	 * Create HDFS FileSystem.
	 * 
	 * @param config
	 * @return
	 */
	public static FileSystem getFileSystem(Configuration config) {
		FileSystem fs = null;
		try {
			fs = FileSystem.get(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fs;
	}

	/**
	 * List root files (including directories).
	 * 
	 * @param fs
	 * @return
	 * @throws IOException
	 */
	public static FileStatus[] listRoots(FileSystem fs) throws IOException {
		return fs.listStatus(ROOT);
	}

	/**
	 * List all files (not including directories).
	 * 
	 * @param fs
	 * @return
	 * @throws IOException
	 */
	public static FileStatus[] listAllFiles(FileSystem fs) throws IOException {
		List<FileStatus> allFiles = new ArrayList<FileStatus>();
		RemoteIterator<LocatedFileStatus> filesItor = fs.listFiles(HdfsUtil.ROOT, true);
		while (filesItor.hasNext()) {
			FileStatus file = filesItor.next();
			allFiles.add(file);
		}
		return allFiles.toArray(new FileStatus[allFiles.size()]);
	}

	/**
	 * List files (including directories) in a directory.
	 * 
	 * @param fs
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public static FileStatus[] listFiles(FileSystem fs, FileStatus dir) throws IOException {
		if (dir == null) {
			return new FileStatus[0];
		}
		return fs.listStatus(dir.getPath());
	}

	/**
	 * Check whether a file exists.
	 * 
	 * @param fs
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static boolean exist(FileSystem fs, FileStatus file) throws IOException {
		return (file != null && fs.exists(file.getPath())) ? true : false;
	}

	/**
	 * Check whether a path exists.
	 * 
	 * @param fs
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static boolean exist(FileSystem fs, Path path) throws IOException {
		return fs.exists(path);
	}

	/**
	 * Create directories for a given path. Return the FileStatus for the path.
	 * 
	 * @param fs
	 * @param path
	 * @throws IOException
	 */
	public static FileStatus mkdirs(FileSystem fs, Path path) throws IOException {
		FileStatus file = null;
		if (!fs.exists(path)) {
			boolean succeed = fs.mkdirs(path);
			if (succeed) {
				file = fs.getFileStatus(path);
			}
		}
		return file;
	}

	/**
	 * Upload a java.io.File to a HDFS file.
	 * 
	 * @param fs
	 * @param srcFile
	 * @param destPath
	 * @throws IOException
	 */
	public static void uploadFileToFile(FileSystem fs, File srcFile, Path destPath) throws IOException {
		String fileLocation = srcFile.getAbsolutePath();
		Path srcPath = new Path(fileLocation);

		fs.copyFromLocalFile(srcPath, destPath);
	}

	/**
	 * Upload a java.io.File to a HDFS directory.
	 * 
	 * @param fs
	 * @param srcFile
	 * @param destDir
	 * @throws IOException
	 */
	public static void uploadFileToDir(FileSystem fs, File srcFile, Path destDir) throws IOException {
		// Create source path
		String fileLocation = srcFile.getAbsolutePath();
		Path srcPath = new Path(fileLocation);

		// Create dest path
		String fileName = srcFile.getName();
		Path destPath = new Path(destDir, fileName);

		fs.copyFromLocalFile(srcPath, destPath);
	}

	/**
	 * Upload a folder to a HDFS directory.
	 * 
	 * @param fs
	 * @param srcDir
	 * @param destPath
	 * @param includingSourceDir
	 * @throws IOException
	 */
	public static void uploadDirToDir(FileSystem fs, File srcDir, Path destPath, boolean includingSourceDir) throws IOException {
		if (!srcDir.isDirectory()) {
			throw new IllegalArgumentException(srcDir.getAbsolutePath() + " is not a directory.");
		}

		if (!exist(fs, destPath)) {

		}

		if (includingSourceDir) {
			// make sure a directory (with name srcDir.getName()) exists destDir.
			Path newDestPath = new Path(destPath, srcDir.getName());
			if (!exist(fs, newDestPath)) {
				// create folder if not exist
				mkdirs(fs, newDestPath);
			}
			uploadDirToDir(fs, srcDir, newDestPath, false);

		} else {
			File[] files = srcDir.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					uploadFileToDir(fs, file, destPath);

				} else if (file.isDirectory()) {
					uploadDirToDir(fs, file, destPath, true);
				}
			}
		}
	}

}
