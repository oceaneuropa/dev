package apache.hadoop.example;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

public class HdfsClientMac {

	protected Configuration getConfiguration() {
		Configuration conf = new Configuration();
		conf.addResource(new Path("/Users/yayang/apache/hadoop/hadoop-2.7.1/etc/hadoop/core-site.xml"));
		// conf.addResource(new Path("/Users/yayang/apache/hadoop/hadoop-2.7.1/etc/hadoop/hdfs-site.xml"));
		// conf.addResource(new Path("/Users/yayang/apache/hadoop/hadoop-2.7.1/etc/hadoop/mapred-site.xml"));
		// conf.set("fs.default.name", "hdfs://localhost:9000");
		return conf;
	}

	protected FileSystem getFileSystem() {
		Configuration conf = getConfiguration();
		FileSystem fs = null;
		try {
			fs = FileSystem.get(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fs;
	}

	public void listFiles() {

	}

	public void test1() {
		try {
			FileSystem fs = getFileSystem();
			// fs.create(new Path("/readme.txt"));

			RemoteIterator<LocatedFileStatus> itor = fs.listFiles(new Path("/"), true);
			while (itor.hasNext()) {
				LocatedFileStatus fileStatus = itor.next();
				Path path = fileStatus.getPath();
				System.out.println("path = " + path.getName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void test2() {
		FileSystem fs = getFileSystem();
		try {
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

			Path root = new Path("/");

			System.out.println("\r\nlistFiles()");
			System.out.println("--------------------------------------------------------------------------------");
			RemoteIterator<LocatedFileStatus> filesItor = fs.listFiles(root, true);
			while (filesItor.hasNext()) {
				LocatedFileStatus fileStatus = filesItor.next();
				System.out.println(fileStatus.toString());
			}
			System.out.println("--------------------------------------------------------------------------------");

			System.out.println("\r\nlistLocatedStatus()");
			System.out.println("--------------------------------------------------------------------------------");
			filesItor = fs.listLocatedStatus(root);
			while (filesItor.hasNext()) {
				LocatedFileStatus fileStatus = filesItor.next();
				System.out.println(fileStatus.toString());
			}
			System.out.println("--------------------------------------------------------------------------------");

			System.out.println("\r\nlistStatus()");
			System.out.println("--------------------------------------------------------------------------------");
			FileStatus[] fileStatuses = fs.listStatus(root);
			for (FileStatus fileStatus : fileStatuses) {
				System.out.println(fileStatus.toString());
			}
			System.out.println("--------------------------------------------------------------------------------");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	public void copyFromLocal(String source, String dest) throws IOException {
		FileSystem fs = getFileSystem();

		Path srcPath = new Path(source);
		Path destPath = new Path(dest);

		if (fs.exists(destPath)) {
			// System.out.println(destPath + " exits.");
		} else {
			// System.out.println(destPath + " doesn't exit.");
		}

		try {
			fs.copyFromLocalFile(srcPath, destPath);
			System.out.println("File " + source + " is copied to " + dest);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fs.close();
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		HdfsClientMac client = new HdfsClientMac();

		// test 1
		client.test1();

		// test 2
		// client.test2();

		// test copy file
		try {
			client.copyFromLocal("/Users/yayang/apache/hadoop/readme.txt", "/yayang/apache/hadoop/readme1.txt");
			client.copyFromLocal("/Users/yayang/apache/hadoop/readme.txt", "/yayang/apache/hadoop/readme2.txt");
			client.copyFromLocal("/Users/yayang/apache/hadoop/readme.txt", "/yayang/apache/hadoop/readme3.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
