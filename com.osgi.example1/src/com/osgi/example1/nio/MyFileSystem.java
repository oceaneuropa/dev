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
public class MyFileSystem extends FileSystem {

	protected static boolean debug = true;

	public static FileSystem wrap(FileSystemProvider provider, FileSystem fs) {
		FileSystem resultFs = (fs != null) ? ((fs instanceof MyFileSystem) ? fs : new MyFileSystem(provider, fs)) : null;
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystem.wrap(FileSystemProvider, FileSystem) provider = ''{0}'', provider = ''{1}'' returns FileSystem [{2}]", new Object[] { provider, fs, resultFs }));
		}
		return resultFs;
	}

	public static FileSystem unwrap(FileSystem fs) {
		FileSystem resultFs = (fs instanceof MyFileSystem) ? ((MyFileSystem) fs).delegate : fs;
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystem.unwrap(FileSystem) fs = ''{0}'' returns FileSystem [{1}]", new Object[] { fs, resultFs }));
		}
		return resultFs;
	}

	public static Path wrap(FileSystem fs, Path path) {
		return MyPath.wrap(fs, path);
	}

	public static Path unwrap(Path path) {
		return MyPath.unwrap(path);
	}

	protected final FileSystemProvider provider;
	protected final FileSystem delegate;

	/**
	 * 
	 * @param provider
	 * @param delegate
	 */
	public MyFileSystem(FileSystemProvider provider, FileSystem delegate) {
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystem() provider = {0}, delegate = {1}", new Object[] { provider, delegate }));
		}
		this.provider = provider;
		this.delegate = delegate;
	}

	@Override
	public FileSystemProvider provider() {
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystem.provider() returns {0}", new Object[] { provider }));
		}
		return this.provider;
	}

	@Override
	public void close() throws IOException {
		this.delegate.close();
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystem.close()", new Object[] {}));
		}
	}

	@Override
	public boolean isOpen() {
		boolean isOpen = this.delegate.isOpen();
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystem.isOpen() returns {0}", new Object[] { isOpen }));
		}
		return isOpen;
	}

	@Override
	public boolean isReadOnly() {
		boolean isReadOnly = this.delegate.isReadOnly();
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystem.isReadOnly() returns {0}", new Object[] { isReadOnly }));
		}
		return isReadOnly;
	}

	@Override
	public String getSeparator() {
		String separator = this.delegate.getSeparator();
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystem.getSeparator() returns {0}", new Object[] { separator }));
		}
		return separator;
	}

	@Override
	public Iterable<Path> getRootDirectories() {
		final Iterable<Path> roots = this.delegate.getRootDirectories();
		Iterable<Path> itorable = new Iterable<Path>() {
			@Override
			public Iterator<Path> iterator() {
				final Iterator<Path> itor = roots.iterator();
				return new Iterator<Path>() {
					@Override
					public boolean hasNext() {
						return itor.hasNext();
					}

					@Override
					public Path next() {
						// return new PassThroughPath(PassThroughFileSystem.this, itr.next());
						return wrap(MyFileSystem.this, itor.next());
					}

					@Override
					public void remove() {
						itor.remove();
					}
				};
			}
		};
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystem.getRootDirectories() returns [{0}]", new Object[] { itorable }));
		}
		return itorable;
	}

	@Override
	public Iterable<FileStore> getFileStores() {
		// assume that unwrapped objects aren't exposed
		Iterable<FileStore> iterable = this.delegate.getFileStores();
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystem.getFileStores() returns [{0}]", new Object[] { iterable }));
		}
		return iterable;
	}

	@Override
	public Set<String> supportedFileAttributeViews() {
		// assume that unwrapped objects aren't exposed
		Set<String> set = this.delegate.supportedFileAttributeViews();
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystem.supportedFileAttributeViews() returns {0}", new Object[] { (set != null ? set.toString() : "null") }));
		}
		return set;
	}

	@Override
	public Path getPath(String first, String... more) {
		// Path path = new PassThroughPath(this, delegate.getPath(first, more));
		Path path = wrap(this, this.delegate.getPath(first, more));
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystem.getPath() first = {0}, more = {1} returns Path [{2}]", new Object[] { first, (more != null ? Arrays.toString(more) : "null"), path }));
		}
		return path;
	}

	@Override
	public PathMatcher getPathMatcher(String syntaxAndPattern) {
		final PathMatcher matcher = this.delegate.getPathMatcher(syntaxAndPattern);
		PathMatcher newMatcher = new PathMatcher() {
			@Override
			public boolean matches(Path path) {
				return matcher.matches(unwrap(path));
			}
		};
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystem.getPathMatcher() syntaxAndPattern = {0} returns PathMatcher [{1}]", new Object[] { syntaxAndPattern, newMatcher }));
		}
		return newMatcher;
	}

	@Override
	public UserPrincipalLookupService getUserPrincipalLookupService() {
		// assume that unwrapped objects aren't exposed
		UserPrincipalLookupService service = this.delegate.getUserPrincipalLookupService();
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystem.getUserPrincipalLookupService() returns UserPrincipalLookupService [{0}]", new Object[] { service }));
		}
		return service;
	}

	@Override
	public WatchService newWatchService() throws IOException {
		// to keep it simple
		// throw new UnsupportedOperationException();
		WatchService service = this.delegate.newWatchService();
		if (debug) {
			Printer.println(MessageFormat.format("MyFileSystem.newWatchService() returns WatchService [{0}]", new Object[] { service }));
		}
		return service;
	}

}
