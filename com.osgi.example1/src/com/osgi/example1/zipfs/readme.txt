Online posts
------------------------------------------------------------------------------------------------------------
Override java.io.File to write to memory?
https://coderanch.com/t/277767/java/Override-java-io-File-write


IMPLEMENTING A FILESYSTEMPROVIDER IN AN OSGI COMPLIANT MANNER
https://shomeier.wordpress.com/2015/03/01/implementing-a-filesystemprovider-in-an-osgi-compliant-manner/


Tweaking the behavior of the default file system in Java 7
http://stackoverflow.com/questions/19425836/tweaking-the-behavior-of-the-default-file-system-in-java-7
------------------------------------------------------------------------------------------------------------


Keyword to search
------------------------------------------------------------------------------------------------------------
extends FileSystemProvider
------------------------------------------------------------------------------------------------------------


Java doc
------------------------------------------------------------------------------------------------------------
https://docs.oracle.com/javase/7/docs/api/java/nio/file/spi/FileSystemProvider.html
http://docs.oracle.com/javase/8/docs/technotes/guides/io/fsp/filesystemprovider.html
------------------------------------------------------------------------------------------------------------

system property example
------------------------------------------------------------------------------------------------------------
-Djava.nio.file.spi.DefaultFileSystemProvider=TestProvider
-Djava.nio.file.spi.DefaultFileSystemProvider=com.osgi.example1.zipfs.ZipFileSystemProvider
-Djava.nio.file.spi.DefaultFileSystemProvider=com.osgi.example1.zipfs.PassThroughFileSystem
------------------------------------------------------------------------------------------------------------

File system provider implementations
------------------------------------------------------------------------------------------------------------
ZipFileSystemProvider.java
http://cr.openjdk.java.net/~sherman/zipfs_src/ZipFileSystemProvider.java
http://cr.openjdk.java.net/~sherman/zipfs_src/Demo.java
http://cr.openjdk.java.net/~sherman/zipfs_src/


PassThroughFileSystem.java
https://github.com/openjdk-mirror/jdk7u-jdk/blob/master/test/java/nio/file/Files/PassThroughFileSystem.java


Class CloudStorageFileSystemProvider
http://googlecloudplatform.github.io/google-cloud-java/0.5.0/apidocs/com/google/cloud/storage/contrib/nio/CloudStorageFileSystemProvider.html


Class JimfsFileSystemProvider
http://google.github.io/jimfs/releases/1.0/api/docs/com/google/common/jimfs/JimfsFileSystemProvider.html


S3FileSystemProvider.java
https://github.com/Upplication/Amazon-S3-FileSystem-NIO2/blob/master/src/main/java/com/upplication/s3fs/S3FileSystemProvider.java


TestProvider.java
https://github.com/openjdk-mirror/jdk7u-jdk/blob/master/test/java/nio/file/spi/TestProvider.java
https://github.com/openjdk-mirror/jdk7u-jdk/blob/master/test/java/nio/file/spi/SetDefaultProvider.java
@run main/othervm -Djava.nio.file.spi.DefaultFileSystemProvider=TestProvider SetDefaultProvider


ResourceFileSystemProvider.java
https://github.com/JakeWharton/resourcefs/blob/master/src/main/java/com/jakewharton/resourcefs/ResourceFileSystemProvider.java


GlusterFileSystemProvider.java
https://github.com/gluster/glusterfs-java-filesystem/blob/master/glusterfs-java-filesystem/src/main/java/com/peircean/glusterfs/GlusterFileSystemProvider.java


RootedFileSystemProvider
https://mina.apache.org/sshd-project/apidocs/org/apache/sshd/common/file/root/RootedFileSystemProvider.html


MemoryFileSystemProvider.java
https://github.com/marschall/memoryfilesystem/blob/master/src/main/java/com/github/marschall/memoryfilesystem/MemoryFileSystemProvider.java


DxFileSystemProvider.java
https://github.com/nextflow-io/jdk7-dxfs/blob/master/src/main/java/nextflow/fs/dx/DxFileSystemProvider.java
------------------------------------------------------------------------------------------------------------


FileSystemProvider examples
------------------------------------------------------------------------------------------------------------
http://www.programcreek.com/java-api-examples/index.php?api=java.nio.file.spi.FileSystemProvider
------------------------------------------------------------------------------------------------------------


About Azure
------------------------------------------------------------------------------------------------------------
https://github.com/Azure/azure-sdk-for-java
https://github.com/Azure/azure-storage-java/tree/master/microsoft-azure-storage/src/com/microsoft/azure/storage/blob
https://azure.microsoft.com/en-us/develop/java/
http://storageexplorer.com/
------------------------------------------------------------------------------------------------------------

My Design
------------------------------------------------------------------------------------------------------------
Eclipse -> java.io.File (/root/workspace1) -> DB (/root/workspace1) -> GoogleDrive(/root/workspace1)
------------------------------------------------------------------------------------------------------------

