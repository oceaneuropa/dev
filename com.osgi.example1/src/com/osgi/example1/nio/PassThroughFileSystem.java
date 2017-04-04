package com.osgi.example1.nio;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * A "pass through" file system implementation that passes through, or delegates, everything to the default file system.
 * 
 * @see https://github.com/openjdk-mirror/jdk7u-jdk/blob/master/test/java/nio/file/Files/PassThroughFileSystem.java
 * 
 */
public class PassThroughFileSystem extends FileSystem {

	protected static boolean debug = true;

	public static Path unwrap(Path wrapper) {
		return PassThroughPath.unwrap(wrapper);
	}

	private FileSystemProvider provider;
	private FileSystem delegate;

	/**
	 * 
	 * @param provider
	 * @param delegate
	 */
	public PassThroughFileSystem(FileSystemProvider provider, FileSystem delegate) {
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystem() provider = {0}, delegate = {1}", new Object[] { provider, delegate }));
		}
		this.provider = provider;
		this.delegate = delegate;
	}

	@Override
	public FileSystemProvider provider() {
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystem.provider() returns {0}", new Object[] { provider }));
		}
		return provider;
	}

	@Override
	public void close() throws IOException {
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystem.close()", new Object[] {}));
		}
		delegate.close();
	}

	@Override
	public boolean isOpen() {
		boolean isOpen = delegate.isOpen();
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystem.isOpen() returns {0}", new Object[] { isOpen }));
		}
		return isOpen;
	}

	@Override
	public boolean isReadOnly() {
		boolean isReadOnly = delegate.isReadOnly();
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystem.isReadOnly() returns {0}", new Object[] { isReadOnly }));
		}
		return isReadOnly;
	}

	@Override
	public String getSeparator() {
		String separator = delegate.getSeparator();
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystem.getSeparator() returns {0}", new Object[] { separator }));
		}
		return separator;
	}

	@Override
	public Iterable<Path> getRootDirectories() {
		final Iterable<Path> roots = delegate.getRootDirectories();
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystem.getRootDirectories()", new Object[] {}));
		}

		return new Iterable<Path>() {
			@Override
			public Iterator<Path> iterator() {
				final Iterator<Path> itr = roots.iterator();
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
		};
	}

	@Override
	public Iterable<FileStore> getFileStores() {
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystem.getFileStores()", new Object[] {}));
		}
		// assume that unwrapped objects aren't exposed
		return delegate.getFileStores();
	}

	@Override
	public Set<String> supportedFileAttributeViews() {
		// assume that unwrapped objects aren't exposed
		Set<String> set = delegate.supportedFileAttributeViews();
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystem.supportedFileAttributeViews() returns {0}", new Object[] { (set != null ? set.toString() : "null") }));
		}
		return set;
	}

	@Override
	public Path getPath(String first, String... more) {
		Path path = new PassThroughPath(this, delegate.getPath(first, more));
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystem.getPath() first = {0}, more = {1} returns Path [{2}]", new Object[] { first, (more != null ? Arrays.toString(more) : "null"), path }));
		}
		return path;
	}

	@Override
	public PathMatcher getPathMatcher(String syntaxAndPattern) {
		final PathMatcher matcher = delegate.getPathMatcher(syntaxAndPattern);
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystem.getPathMatcher() syntaxAndPattern = {0} returns PathMatcher [{1}]", new Object[] { syntaxAndPattern, matcher }));
		}
		return new PathMatcher() {
			@Override
			public boolean matches(Path path) {
				return matcher.matches(unwrap(path));
			}
		};
	}

	@Override
	public UserPrincipalLookupService getUserPrincipalLookupService() {
		// assume that unwrapped objects aren't exposed
		UserPrincipalLookupService service = delegate.getUserPrincipalLookupService();
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystem.getUserPrincipalLookupService() returns UserPrincipalLookupService [{0}]", new Object[] { service }));
		}
		return service;
	}

	@Override
	public WatchService newWatchService() throws IOException {
		// to keep it simple
		// throw new UnsupportedOperationException();
		WatchService service = delegate.newWatchService();
		if (debug) {
			println(MessageFormat.format("PassThroughFileSystem.newWatchService() returns WatchService [{0}]", new Object[] { service }));
		}
		return service;
	}

	public static void println() {
		System.out.println();
	}

	public static void println(String message) {
		System.out.println(message);
	}

}
