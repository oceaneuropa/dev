package apache.hadoop.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @see http://www.tutorialspoint.com/junit/junit_ignore_test.htm
 * @see http://stackoverflow.com/questions/11342400/how-to-list-all-files-in-a-directory-and-its-subdirectories-in-hadoop-hdfs
 * 
 */
public class HdfsClientMacTest /* extends TestCase */ {

	protected FileSystem fs;

	public HdfsClientMacTest() {
		this.fs = getFileSystem();
	}

	protected void setUp() {
		this.fs = getFileSystem();
	}

	protected FileSystem getFileSystem() {
		Configuration config = HdfsUtil.getConfiguration("/Users/yayang/apache/hadoop/hadoop-2.7.1/etc/hadoop/core-site.xml");
		return HdfsUtil.getFileSystem(config);
	}

	@Ignore
	@Test
	public void testFileSystem() throws IOException {
		System.out.println("--- --- --- testFileSystem() --- --- ---");

		// e.g.
		// homeDir = hdfs://localhost:9000/user/yayang
		// workingDir = hdfs://localhost:9000/user/yayang
		// fsStatus = {Capacity:499418034176, Remaining:325051322368, Used:73728}
		// fsUri = hdfs://localhost:9000
		// fsScheme = hdfs
		Path homeDir = fs.getHomeDirectory();
		Path workingDir = fs.getWorkingDirectory();
		FsStatus fsStatus = fs.getStatus();
		URI fsUri = fs.getUri();
		String fsScheme = fs.getScheme();

		System.out.println("homeDir = " + homeDir);
		System.out.println("workingDir = " + workingDir);
		System.out.println("fsStatus = {Capacity:" + fsStatus.getCapacity() + ", Remaining:" + fsStatus.getRemaining() + ", Used:" + fsStatus.getUsed() + "}");
		System.out.println("fsUri = " + fsUri);
		System.out.println("fsScheme = " + fsScheme);

		System.out.println();
	}

	@Test
	public void testListAllFiles() throws IOException {
		System.out.println("--- --- --- testListAllFiles() --- --- ---");

		FileStatus[] files = HdfsUtil.listAllFiles(fs);
		for (FileStatus file : files) {
			Path path = file.getPath();
			System.out.println(path.toUri());
		}

		System.out.println();
	}

	@Test
	public void testCreateDir() throws IOException {
		System.out.println("--- --- --- testCreateDir() --- --- ---");

		FileStatus dir1 = HdfsUtil.mkdirs(fs, new Path(HdfsUtil.ROOT, "TestDirectory1"));
		FileStatus dir2 = HdfsUtil.mkdirs(fs, new Path(HdfsUtil.ROOT, "TestDirectory2"));
		FileStatus dir3 = HdfsUtil.mkdirs(fs, new Path(HdfsUtil.ROOT, "TestDirectory3"));

		boolean exist1 = HdfsUtil.exist(fs, dir1);
		boolean exist2 = HdfsUtil.exist(fs, dir2);
		boolean exist3 = HdfsUtil.exist(fs, dir3);

		System.out.println();
	}

	/**
	 * @see http://stackoverflow.com/questions/11342400/how-to-list-all-files-in-a-directory-and-its-subdirectories-in-hadoop-hdfs
	 * @throws IOException
	 */
	@Test
	public void testListDir() throws IOException {
		System.out.println("--- --- --- testListDir() --- --- ---");

		FileStatus[] files = HdfsUtil.listRoots(fs);
		for (FileStatus file : files) {
			walkFolders(file);
		}

		System.out.println();
	}

	/**
	 * Recursively walk through folders and print out folder path.
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected void walkFolders(FileStatus file) throws FileNotFoundException, IOException {
		if (file.isDirectory()) {
			System.out.println(file.getPath().toString());

			FileStatus[] subFiles = fs.listStatus(file.getPath());
			for (FileStatus subFile : subFiles) {
				walkFolders(subFile);
			}
		}
	}

	@Test
	public void testUploadFiles() throws IOException {

	}

	public static void main(String[] args) {

	}

}
