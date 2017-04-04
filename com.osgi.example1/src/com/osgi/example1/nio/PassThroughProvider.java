package com.osgi.example1.nio;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PassThroughProvider extends FileSystemProvider {

	private static final String SCHEME = "pass";
	private static volatile PassThroughFileSystem delegate;
	protected static boolean debug = true;

	/**
	 * Creates a new "pass through" file system. Useful for test environments where the provider might not be deployed.
	 */
	public static FileSystem create() throws IOException {
		FileSystemProvider provider = new PassThroughProvider();
		Map<String, ?> env = Collections.emptyMap();
		URI uri = URI.create("pass:///");
		FileSystem newFileSystem = provider.newFileSystem(uri, env);

		if (debug) {
			println(MessageFormat.format("PassThroughFileSystem.create() uri = {0}, provider = {1} returns FileSystem [{2}]", new Object[] { uri, provider, newFileSystem }));
		}
		return newFileSystem;
	}

	public static Path unwrap(Path wrapper) {
		return PassThroughPath.unwrap(wrapper);
	}

	public PassThroughProvider() {
	}

	@Override
	public String getScheme() {
		return SCHEME;
	}

	private void checkScheme(URI uri) {
		if (!uri.getScheme().equalsIgnoreCase(SCHEME)) {
			throw new IllegalArgumentException();
		}
	}

	private void checkUri(URI uri) {
		checkScheme(uri);
		if (!uri.getSchemeSpecificPart().equals("///")) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
		checkUri(uri);
		synchronized (PassThroughProvider.class) {
			if (delegate != null)
				throw new FileSystemAlreadyExistsException();
			PassThroughFileSystem result = new PassThroughFileSystem(this, FileSystems.getDefault());
			delegate = result;
			return result;
		}
	}

	@Override
	public FileSystem getFileSystem(URI uri) {
		checkUri(uri);
		FileSystem result = delegate;
		if (result == null)
			throw new FileSystemNotFoundException();
		return result;
	}

	@Override
	public Path getPath(URI uri) {
		checkScheme(uri);
		if (delegate == null)
			throw new FileSystemNotFoundException();
		uri = URI.create(delegate.provider().getScheme() + ":" + uri.getSchemeSpecificPart());
		return new PassThroughPath(delegate, delegate.provider().getPath(uri));
	}

	@Override
	public void setAttribute(Path file, String attribute, Object value, LinkOption... options) throws IOException {
		Files.setAttribute(unwrap(file), attribute, value, options);
	}

	@Override
	public Map<String, Object> readAttributes(Path file, String attributes, LinkOption... options) throws IOException {
		return Files.readAttributes(unwrap(file), attributes, options);
	}

	@Override
	public <V extends FileAttributeView> V getFileAttributeView(Path file, Class<V> type, LinkOption... options) {
		return Files.getFileAttributeView(unwrap(file), type, options);
	}

	@Override
	public <A extends BasicFileAttributes> A readAttributes(Path file, Class<A> type, LinkOption... options) throws IOException {
		return Files.readAttributes(unwrap(file), type, options);
	}

	@Override
	public void delete(Path file) throws IOException {
		Files.delete(unwrap(file));
	}

	@Override
	public void createSymbolicLink(Path link, Path target, FileAttribute<?>... attrs) throws IOException {
		Files.createSymbolicLink(unwrap(link), unwrap(target), attrs);
	}

	@Override
	public void createLink(Path link, Path existing) throws IOException {
		Files.createLink(unwrap(link), unwrap(existing));
	}

	@Override
	public Path readSymbolicLink(Path link) throws IOException {
		Path target = Files.readSymbolicLink(unwrap(link));
		return new PassThroughPath(delegate, target);
	}

	@Override
	public void copy(Path source, Path target, CopyOption... options) throws IOException {
		Files.copy(unwrap(source), unwrap(target), options);
	}

	@Override
	public void move(Path source, Path target, CopyOption... options) throws IOException {
		Files.move(unwrap(source), unwrap(target), options);
	}

	private DirectoryStream<Path> wrap(final DirectoryStream<Path> stream) {
		return new DirectoryStream<Path>() {
			@Override
			public Iterator<Path> iterator() {
				final Iterator<Path> itr = stream.iterator();
				return new Iterator<Path>() {
					@Override
					public boolean hasNext() {
						return itr.hasNext();
					}

					@Override
					public Path next() {
						return new PassThroughPath(delegate, itr.next());
					}

					@Override
					public void remove() {
						itr.remove();
					}
				};
			}

			@Override
			public void close() throws IOException {
				stream.close();
			}
		};
	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
		return wrap(Files.newDirectoryStream(dir, filter));
	}

	@Override
	public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
		Files.createDirectory(unwrap(dir), attrs);
	}

	@Override
	public SeekableByteChannel newByteChannel(Path file, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
		return Files.newByteChannel(unwrap(file), options, attrs);
	}

	@Override
	public boolean isHidden(Path file) throws IOException {
		return Files.isHidden(unwrap(file));
	}

	@Override
	public FileStore getFileStore(Path file) throws IOException {
		return Files.getFileStore(unwrap(file));
	}

	@Override
	public boolean isSameFile(Path file, Path other) throws IOException {
		return Files.isSameFile(unwrap(file), unwrap(other));
	}

	@Override
	public void checkAccess(Path file, AccessMode... modes) throws IOException {
		// hack
		if (modes.length == 0) {
			if (Files.exists(unwrap(file))) {
				return;
			} else {
				throw new NoSuchFileException(file.toString());
			}
		}
		throw new RuntimeException("not implemented yet");
	}

	public static void println() {
		System.out.println();
	}

	public static void println(String message) {
		System.out.println(message);
	}

}