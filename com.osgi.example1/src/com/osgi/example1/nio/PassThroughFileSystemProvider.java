package com.osgi.example1.nio;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @see http://stackoverflow.com/questions/19425836/tweaking-the-behavior-of-the-default-file-system-in-java-7
 *
 * @usage -Djava.nio.file.spi.DefaultFileSystemProvider=com.osgi.example1.nio.PassThroughFileSystemProvider
 */
public class PassThroughFileSystemProvider extends FileSystemProvider {

	protected static boolean debug = true;
	protected static final String SCHEME = "file";

	protected FileSystemProvider defaultProvider;
	protected Map<URI, PassThroughFileSystem> uriToPassThroughMap = new HashMap<URI, PassThroughFileSystem>();

	public PassThroughFileSystemProvider(FileSystemProvider defaultProvider) {
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider() defaultProvider = {0}", new Object[] { defaultProvider }));
		}
		this.defaultProvider = defaultProvider;
	}

	@Override
	public String getScheme() {
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.getScheme() returns ''{0}''", new Object[] { SCHEME }));
		}
		return SCHEME;
	}

	@Override
	public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
		FileSystem defaultFileSystem = defaultProvider.newFileSystem(uri, env);
		PassThroughFileSystem passThroughFileSystem = new PassThroughFileSystem(this, defaultFileSystem);
		uriToPassThroughMap.put(uri, passThroughFileSystem);
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.newFileSystem(URI, Map) uri = '{0}', defaultFileSystem = {1} returns FileSystem [{2}]", new Object[] { uri, defaultFileSystem, passThroughFileSystem }));
		}
		return passThroughFileSystem;
	}

	@Override
	public FileSystem getFileSystem(URI uri) {
		FileSystem defaultFileSystem = defaultProvider.getFileSystem(uri);
		PassThroughFileSystem passThroughFileSystem = uriToPassThroughMap.get(uri);
		if (passThroughFileSystem == null) {
			passThroughFileSystem = new PassThroughFileSystem(this, defaultFileSystem);
			uriToPassThroughMap.put(uri, passThroughFileSystem);
		}
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.getFileSystem(URI) uri = ''{0}'' defaultFileSystem = {1} returns FileSystem [{2}]", new Object[] { uri, defaultFileSystem, passThroughFileSystem }));
		}
		return passThroughFileSystem;
	}

	@Override
	public Path getPath(URI uri) {
		Path path = defaultProvider.getPath(uri);
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.getPath() uri = '{0}' returns Path [{1}]", uri, path));
		}
		return path;
	}

	@Override
	public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
		SeekableByteChannel channel = defaultProvider.newByteChannel(path, options, attrs);
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.newByteChannel() path = '{0}' returns SeekableByteChannel [{1}]", path, channel));
		}
		return channel;
	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path dir, Filter<? super Path> filter) throws IOException {
		DirectoryStream<Path> dirStream = defaultProvider.newDirectoryStream(dir, filter);
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.newDirectoryStream() path = '{0}' returns DirectoryStream [{1}]", dir, dirStream));
		}
		return dirStream;
	}

	@Override
	public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.createDirectory() path = '{0}'", new Object[] { dir }));
		}
		defaultProvider.createDirectory(dir, attrs);
	}

	@Override
	public void delete(Path path) throws IOException {
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.delete() path = '{0}'", new Object[] { path }));
		}
		defaultProvider.delete(path);
	}

	@Override
	public void copy(Path source, Path target, CopyOption... options) throws IOException {
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.delete() source = '{0}', target = '{1}'", new Object[] { source, target }));
		}
		defaultProvider.copy(source, target, options);
	}

	@Override
	public void move(Path source, Path target, CopyOption... options) throws IOException {
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.move() source = '{0}', target = '{1}'", new Object[] { source, target }));
		}
		defaultProvider.move(source, target, options);
	}

	@Override
	public boolean isSameFile(Path path, Path path2) throws IOException {
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.isSameFile() path = '{0}', path2 = '{1}'", new Object[] { path, path2 }));
		}
		return defaultProvider.isSameFile(path, path2);
	}

	@Override
	public boolean isHidden(Path path) throws IOException {
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.isHidden() path = '{0}'", new Object[] { path }));
		}
		return defaultProvider.isHidden(path);
	}

	@Override
	public FileStore getFileStore(Path path) throws IOException {
		FileStore fileStore = defaultProvider.getFileStore(path);
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.getFileStore() path = '{0}' returns FileStore [{1}]", new Object[] { path, fileStore }));
		}
		return fileStore;
	}

	@Override
	public void checkAccess(Path path, AccessMode... modes) throws IOException {
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.checkAccess() path = '{0}', accessModes = '{1}'", new Object[] { path, modes }));
		}
		defaultProvider.checkAccess(path, modes);
	}

	@Override
	public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
		V v = defaultProvider.getFileAttributeView(path, type, options);
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.checkAccess() path = '{0}' returns FileAttributeView [{1}]", new Object[] { path, v }));
		}
		return v;
	}

	@Override
	public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
		A a = defaultProvider.readAttributes(path, type, options);
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.readAttributes() path = '{0}' returns BasicFileAttributes [{1}]", new Object[] { path, a }));
		}
		return a;
	}

	@Override
	public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
		Map<String, Object> attrs = defaultProvider.readAttributes(path, attributes, options);
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.readAttributes() path = '{0}', attributes = '{1}' returns Map [{2}]", new Object[] { path, attributes, (attrs != null ? attrs.toString() : "null") }));
		}
		return attrs;
	}

	@Override
	public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystemProvider.setAttribute() path = '{0}', attributes = '{1}' value = {2}", new Object[] { path, attribute, (value != null ? value : "null") }));
		}
		defaultProvider.setAttribute(path, attribute, value, options);
	}

	public static void println() {
		System.out.println();
	}

	public static void println(String message) {
		System.out.println(message);
	}

}
