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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @see http://stackoverflow.com/questions/19425836/tweaking-the-behavior-of-the-default-file-system-in-java-7
 *
 * @usage -Djava.nio.file.spi.DefaultFileSystemProvider=com.osgi.example1.nio.MyFileSystemProvider
 */
public class MyFileSystemProvider extends FileSystemProvider {

	protected static boolean debug = true;

	public static Path wrap(FileSystem fs, Path path) {
		return MyPath.wrap(fs, path);
	}

	public static Path unwrap(Path path) {
		return MyPath.unwrap(path);
	}

	protected static final String SCHEME = "file";
	protected static URI fileSchemeURI = URI.create("file:///");

	protected FileSystemProvider delegate;
	protected Map<URI, FileSystem> fsMap = new HashMap<URI, FileSystem>();

	/**
	 * 
	 * @param defaultProvider
	 */
	public MyFileSystemProvider(FileSystemProvider defaultProvider) {
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystemProvider() defaultProvider = {0}", new Object[] { defaultProvider }));
		}
		this.delegate = defaultProvider;
	}

	@Override
	public String getScheme() {
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystemProvider.getScheme() returns ''{0}''", new Object[] { SCHEME }));
		}
		return SCHEME;
	}

	@Override
	public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
		FileSystem fileSystem = this.delegate.newFileSystem(uri, env);
		FileSystem myFileSystem = MyFileSystem.wrap(this, fileSystem);
		this.fsMap.put(uri, myFileSystem);
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystemProvider.newFileSystem(URI, Map) uri = ''{0}'', defaultFileSystem = {1} returns FileSystem [{2}]", new Object[] { uri, fileSystem, myFileSystem }));
		}
		return myFileSystem;
	}

	@Override
	public FileSystem getFileSystem(URI uri) {
		FileSystem fileSystem = this.delegate.getFileSystem(uri);
		FileSystem myFileSystem = this.fsMap.get(uri);
		if (myFileSystem == null) {
			myFileSystem = MyFileSystem.wrap(this, fileSystem);
			this.fsMap.put(uri, myFileSystem);
		}
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystemProvider.getFileSystem(URI) uri = ''{0}'' defaultFileSystem = {1} returns FileSystem [{2}]", new Object[] { uri, fileSystem, myFileSystem }));
		}
		return myFileSystem;
	}

	@Override
	public Path getPath(URI uri) {
		Path path = this.delegate.getPath(uri);
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystemProvider.getPath() uri = ''{0}'' returns Path [{1}]", uri, path));
		}
		return path;
	}

	@Override
	public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
		SeekableByteChannel channel = this.delegate.newByteChannel(unwrap(path), options, attrs);
		SeekableByteChannel newChannel = MySeekableByteChannel.wrap(channel);
		if (debug) {
			String optionsStr = (options != null) ? Arrays.toString(options.toArray(new Object[options.size()])) : "null";
			String attrsStr = (attrs != null) ? Arrays.toString(attrs) : "null";
			Printer.println(MessageFormat.format("MyFileSystemProvider.newByteChannel() path = ''{0}'', options = {1}, attrs = {2} returns SeekableByteChannel [{3}]", new Object[] { path, optionsStr, attrsStr, newChannel }));
		}
		return newChannel;
	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path dir, Filter<? super Path> filter) throws IOException {
		final DirectoryStream<Path> dirStream = this.delegate.newDirectoryStream(unwrap(dir), filter);

		DirectoryStream<Path> newDirStream = new DirectoryStream<Path>() {
			@Override
			public Iterator<Path> iterator() {
				final Iterator<Path> itor = dirStream.iterator();
				Iterator<Path> newItor = new Iterator<Path>() {
					@Override
					public boolean hasNext() {
						return itor.hasNext();
					}

					@Override
					public Path next() {
						Path path = itor.next();
						FileSystem fileSystem = getFileSystem(fileSchemeURI);
						return MyPath.wrap(fileSystem, path);
					}
				};
				return newItor;
			}

			@Override
			public void close() throws IOException {
				dirStream.close();
			}
		};

		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystemProvider.newDirectoryStream() path = '{0}' returns DirectoryStream [{1}]", dir, newDirStream));
		}
		return newDirStream;
	}

	@Override
	public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
		this.delegate.createDirectory(unwrap(dir), attrs);
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystemProvider.createDirectory() path = ''{0}''", new Object[] { dir }));
		}
	}

	@Override
	public void delete(Path path) throws IOException {
		this.delegate.delete(unwrap(path));
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystemProvider.delete() path = ''{0}''", new Object[] { path }));
		}
	}

	@Override
	public void copy(Path source, Path target, CopyOption... options) throws IOException {
		this.delegate.copy(unwrap(source), unwrap(target), options);
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystemProvider.delete() source = ''{0}'', target = ''{1}''", new Object[] { source, target }));
		}
	}

	@Override
	public void move(Path source, Path target, CopyOption... options) throws IOException {
		this.delegate.move(unwrap(source), unwrap(target), options);
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystemProvider.move() source = ''{0}'', target = ''{1}''", new Object[] { source, target }));
		}
	}

	@Override
	public boolean isSameFile(Path path, Path path2) throws IOException {
		boolean isSameFile = this.delegate.isSameFile(unwrap(path), unwrap(path2));
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystemProvider.isSameFile() path = ''{0}'', path2 = ''{1}'' returns [{2}]", new Object[] { path, path2, isSameFile }));
		}
		return isSameFile;
	}

	@Override
	public boolean isHidden(Path path) throws IOException {
		boolean isHidden = this.delegate.isHidden(unwrap(path));
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystemProvider.isHidden() path = ''{0}'' returns [{1}]", new Object[] { path, isHidden }));
		}
		return isHidden;
	}

	@Override
	public FileStore getFileStore(Path path) throws IOException {
		FileStore fileStore = this.delegate.getFileStore(unwrap(path));
		FileStore myFileStore = MyFileStore.wrap(fileStore);
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystemProvider.getFileStore() path = ''{0}'' returns FileStore [{1}]", new Object[] { path, myFileStore }));
		}
		return myFileStore;
	}

	@Override
	public void checkAccess(Path path, AccessMode... modes) throws IOException {
		this.delegate.checkAccess(unwrap(path), modes);
		if (debug) {
			String modesStr = (modes != null) ? Arrays.toString(modes) : "null";
			Printer.println(MessageFormat.format("MyFileSystemProvider.checkAccess() path = ''{0}'', accessModes = {1}", new Object[] { path, modesStr }));
		}
	}

	@Override
	public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
		V attrView = this.delegate.getFileAttributeView(unwrap(path), type, options);
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystemProvider.checkAccess() path = ''{0}'' returns FileAttributeView [{1}]", new Object[] { path, attrView }));
		}
		return attrView;
	}

	@Override
	public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
		A attr = this.delegate.readAttributes(unwrap(path), type, options);
		if (debug) {
			String optionsStr = (options != null) ? Arrays.toString(options) : "null";
			Printer.println(MessageFormat.format("MyFileSystemProvider.readAttributes() path = ''{0}'', options = {1} returns BasicFileAttributes [{2}]", new Object[] { path, optionsStr, attr }));
		}
		return attr;
	}

	@Override
	public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
		Map<String, Object> attrs = this.delegate.readAttributes(unwrap(path), attributes, options);
		if (debug) {
			String optionsStr = (options != null) ? Arrays.toString(options) : "null";
			Printer.println(MessageFormat.format("MyFileSystemProvider.readAttributes() path = ''{0}'', attributes = ''{1}'', options = {2} returns Map [{3}]", new Object[] { path, attributes, optionsStr, attrs }));
		}
		return attrs;
	}

	@Override
	public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
		this.delegate.setAttribute(unwrap(path), attribute, value, options);
		if (debug) {
			String optionsStr = (options != null) ? Arrays.toString(options) : "null";
			Printer.println(MessageFormat.format("MyFileSystemProvider.setAttribute() path = ''{0}'', attributes = ''{1}'', value = {2}, options = {3}", new Object[] { path, attribute, value, optionsStr }));
		}
	}

}
