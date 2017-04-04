package com.osgi.example1.nio;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;

public class PassThroughPath implements Path {
	public final FileSystem fs;
	public final Path delegate;

	public static Path unwrap(Path wrapper) {
		if (wrapper == null) {
			throw new NullPointerException();
		}
		if (!(wrapper instanceof PassThroughPath)) {
			throw new ProviderMismatchException();
		}
		return ((PassThroughPath) wrapper).delegate;
	}

	PassThroughPath(FileSystem fs, Path delegate) {
		this.fs = fs;
		this.delegate = delegate;
	}

	private Path wrap(Path path) {
		return (path != null) ? new PassThroughPath(fs, path) : null;
	}

	@Override
	public FileSystem getFileSystem() {
		return fs;
	}

	@Override
	public boolean isAbsolute() {
		return delegate.isAbsolute();
	}

	@Override
	public Path getRoot() {
		return wrap(delegate.getRoot());
	}

	@Override
	public Path getParent() {
		return wrap(delegate.getParent());
	}

	@Override
	public int getNameCount() {
		return delegate.getNameCount();
	}

	@Override
	public Path getFileName() {
		return wrap(delegate.getFileName());
	}

	@Override
	public Path getName(int index) {
		return wrap(delegate.getName(index));
	}

	@Override
	public Path subpath(int beginIndex, int endIndex) {
		return wrap(delegate.subpath(beginIndex, endIndex));
	}

	@Override
	public boolean startsWith(Path other) {
		return delegate.startsWith(unwrap(other));
	}

	@Override
	public boolean startsWith(String other) {
		return delegate.startsWith(other);
	}

	@Override
	public boolean endsWith(Path other) {
		return delegate.endsWith(unwrap(other));
	}

	@Override
	public boolean endsWith(String other) {
		return delegate.endsWith(other);
	}

	@Override
	public Path normalize() {
		return wrap(delegate.normalize());
	}

	@Override
	public Path resolve(Path other) {
		return wrap(delegate.resolve(unwrap(other)));
	}

	@Override
	public Path resolve(String other) {
		return wrap(delegate.resolve(other));
	}

	@Override
	public Path resolveSibling(Path other) {
		return wrap(delegate.resolveSibling(unwrap(other)));
	}

	@Override
	public Path resolveSibling(String other) {
		return wrap(delegate.resolveSibling(other));
	}

	@Override
	public Path relativize(Path other) {
		return wrap(delegate.relativize(unwrap(other)));
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof PassThroughPath))
			return false;
		return delegate.equals(unwrap((PassThroughPath) other));
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

	@Override
	public URI toUri() {
		String ssp = delegate.toUri().getSchemeSpecificPart();
		return URI.create(fs.provider().getScheme() + ":" + ssp);
	}

	@Override
	public Path toAbsolutePath() {
		return wrap(delegate.toAbsolutePath());
	}

	@Override
	public Path toRealPath(LinkOption... options) throws IOException {
		return wrap(delegate.toRealPath(options));
	}

	@Override
	public File toFile() {
		return delegate.toFile();
	}

	@Override
	public Iterator<Path> iterator() {
		final Iterator<Path> itr = delegate.iterator();
		return new Iterator<Path>() {
			@Override
			public boolean hasNext() {
				return itr.hasNext();
			}

			@Override
			public Path next() {
				return wrap(itr.next());
			}

			@Override
			public void remove() {
				itr.remove();
			}
		};
	}

	@Override
	public int compareTo(Path other) {
		return delegate.compareTo(unwrap(other));
	}

	@Override
	public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) {
		throw new UnsupportedOperationException();
	}

	@Override
	public WatchKey register(WatchService watcher, WatchEvent.Kind<?>... events) {
		throw new UnsupportedOperationException();
	}

}