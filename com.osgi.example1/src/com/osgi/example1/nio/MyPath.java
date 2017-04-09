package com.osgi.example1.nio;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Iterator;

public class MyPath implements Path {

	protected static boolean debug = true;

	public static Path wrap(FileSystem fs, Path path) {
		Path resultPath = (path != null) ? ((path instanceof MyPath) ? path : new MyPath(fs, path)) : path;
		// if (debug) {
		// Printer.println(MessageFormat.format("MyPath.wrap(FileSystem, Path) path = ''{0}'' returns Path ''{1}''", new Object[] { path, resultPath }));
		// }
		return resultPath;
	}

	public static Path unwrap(Path path) {
		Path resultPath = (path instanceof MyPath) ? ((MyPath) path).delegatePath : path;
		// if (debug) {
		// Printer.println(MessageFormat.format("MyPath.unwrap(Path) path = ''{0}'' returns Path ''{1}''", new Object[] { path, resultPath }));
		// }
		return resultPath;
	}

	public final FileSystem fs;
	public final Path delegatePath;

	/**
	 * 
	 * @param fs
	 * @param path
	 */
	public MyPath(FileSystem fs, Path path) {
		if (debug) {
			Printer.println(MessageFormat.format("new MyPath(FileSystem, Path) fs = {0}, path = ''{1}''", new Object[] { fs, path }));
		}
		this.fs = fs;
		this.delegatePath = path;
	}

	@Override
	public FileSystem getFileSystem() {
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.getFileSystem() returns [{0}]", new Object[] { this.fs }));
		}
		return this.fs;
	}

	@Override
	public boolean isAbsolute() {
		boolean isAbsolute = this.delegatePath.isAbsolute();
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.isAbsolute() returns [{0}]", new Object[] { isAbsolute }));
		}
		return isAbsolute;
	}

	@Override
	public Path getRoot() {
		Path root = this.delegatePath.getRoot();
		Path newRoot = wrap(this.fs, root);
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.getRoot() returns [{0}]", new Object[] { newRoot }));
		}
		return newRoot;
	}

	@Override
	public Path getParent() {
		Path parent = this.delegatePath.getParent();
		Path newParent = wrap(this.fs, parent);
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.getParent() returns [{0}]", new Object[] { newParent }));
		}
		return newParent;
	}

	@Override
	public int getNameCount() {
		int nameCount = this.delegatePath.getNameCount();
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.getNameCount() returns [{0}]", new Object[] { nameCount }));
		}
		return nameCount;
	}

	@Override
	public Path getFileName() {
		Path fileName = this.delegatePath.getFileName();
		Path newFileName = wrap(this.fs, fileName);
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.getFileName() returns [{0}]", new Object[] { newFileName }));
		}
		return newFileName;
	}

	@Override
	public Path getName(int index) {
		Path name = this.delegatePath.getName(index);
		Path newName = wrap(this.fs, name);
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.getName(int) index = {0} returns [{1}]", new Object[] { index, newName }));
		}
		return newName;
	}

	@Override
	public boolean startsWith(Path other) {
		boolean startsWith = this.delegatePath.startsWith(unwrap(other));
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.startsWith(Path) other = {0} returns [{1}]", new Object[] { other, startsWith }));
		}
		return startsWith;
	}

	@Override
	public boolean startsWith(String other) {
		boolean startsWith = this.delegatePath.startsWith(other);
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.startsWith(String) other = {0} returns [{1}]", new Object[] { other, startsWith }));
		}
		return startsWith;
	}

	@Override
	public boolean endsWith(Path other) {
		boolean endsWith = this.delegatePath.endsWith(unwrap(other));
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.endsWith(Path) other = {0} returns [{1}]", new Object[] { other, endsWith }));
		}
		return endsWith;
	}

	@Override
	public boolean endsWith(String other) {
		boolean endsWith = this.delegatePath.endsWith(other);
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.endsWith(String) other = {0} returns [{1}]", new Object[] { other, endsWith }));
		}
		return endsWith;
	}

	@Override
	public Path normalize() {
		Path normalizedPath = this.delegatePath.normalize();
		Path newNormalizedPath = wrap(this.fs, normalizedPath);
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.normalize() returns [{0}]", new Object[] { newNormalizedPath }));
		}
		return newNormalizedPath;
	}

	@Override
	public Path resolve(Path other) {
		Path resolvedPath = this.delegatePath.resolve(unwrap(other));
		Path newResolvedPath = wrap(this.fs, resolvedPath);
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.resolve(Path) other = {0} returns [{1}]", new Object[] { other, newResolvedPath }));
		}
		return newResolvedPath;
	}

	@Override
	public Path resolve(String other) {
		Path resolvedPath = this.delegatePath.resolve(other);
		Path newResolvedPath = wrap(this.fs, resolvedPath);
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.resolve(String) other = {0} returns [{1}]", new Object[] { other, newResolvedPath }));
		}
		return newResolvedPath;
	}

	@Override
	public Path resolveSibling(Path other) {
		Path siblingPath = this.delegatePath.resolveSibling(unwrap(other));
		Path newSiblingPath = wrap(this.fs, siblingPath);
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.resolveSibling(Path) other = {0} returns [{1}]", new Object[] { other, newSiblingPath }));
		}
		return newSiblingPath;
	}

	@Override
	public Path resolveSibling(String other) {
		Path siblingPath = delegatePath.resolveSibling(other);
		Path newSiblingPath = wrap(this.fs, siblingPath);
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.resolveSibling(String) other = {0} returns [{1}]", new Object[] { other, newSiblingPath }));
		}
		return newSiblingPath;
	}

	@Override
	public Path relativize(Path other) {
		Path relativizedPath = this.delegatePath.relativize(unwrap(other));
		Path newRelativizedPath = wrap(this.fs, relativizedPath);
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.relativize(Path) other = {0} returns [{1}]", new Object[] { other, newRelativizedPath }));
		}
		return newRelativizedPath;
	}

	@Override
	public Path toAbsolutePath() {
		Path absolutePath = this.delegatePath.toAbsolutePath();
		Path newAbsolutePath = wrap(this.fs, absolutePath);
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.toAbsolutePath() returns [{0}]", new Object[] { newAbsolutePath }));
		}
		return newAbsolutePath;
	}

	@Override
	public Path toRealPath(LinkOption... options) throws IOException {
		Path realPath = this.delegatePath.toRealPath(options);
		Path newRealPath = wrap(this.fs, realPath);
		if (debug) {
			String optionsStr = (options != null) ? Arrays.toString(options) : "null";
			Printer.println(MessageFormat.format("MyPath.toRealPath(LinkOption...) options = {0} returns [{1}]", new Object[] { optionsStr, newRealPath }));
		}
		return newRealPath;
	}

	@Override
	public Path subpath(int beginIndex, int endIndex) {
		Path subpath = this.delegatePath.subpath(beginIndex, endIndex);
		Path newSubpath = wrap(this.fs, subpath);
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.subpath(int, int) beginIndex = {0}, endIndex = {1} returns [{2}]", new Object[] { beginIndex, endIndex, newSubpath }));
		}
		return newSubpath;
	}

	@Override
	public URI toUri() {
		URI uri = this.delegatePath.toUri();
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.toUri() returns [{0}]", new Object[] { uri }));
		}
		return uri;
	}

	@Override
	public File toFile() {
		File ioFile = this.delegatePath.toFile();
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.toFile() returns [{0}]", new Object[] { ioFile }));
		}
		return ioFile;
	}

	@Override
	public Iterator<Path> iterator() {
		final Iterator<Path> itor = this.delegatePath.iterator();
		Iterator<Path> newItor = new Iterator<Path>() {
			@Override
			public boolean hasNext() {
				return itor.hasNext();
			}

			@Override
			public Path next() {
				Path path = itor.next();
				return wrap(fs, path);
			}

			@Override
			public void remove() {
				itor.remove();
			}
		};
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.iterator() returns [{0}]", new Object[] { newItor }));
		}
		return newItor;
	}

	@Override
	public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
		// throw new UnsupportedOperationException();
		WatchKey watchKey = delegatePath.register(watcher, events, modifiers);
		WatchKey newWatchKey = MyWatchKey.wrap(watchKey);
		if (debug) {
			String eventsStr = (events != null) ? Arrays.toString(events) : "null";
			String modifiersStr = (modifiers != null) ? Arrays.toString(modifiers) : "null";
			Printer.println(MessageFormat.format("MyPath.register(WatchService, WatchEvent.Kind, WatchEvent.Modifier) watcher = {0}, events = {1}, modifiers = {2} returns [{3}]", new Object[] { watcher, eventsStr, modifiersStr, newWatchKey }));
		}
		return newWatchKey;
	}

	@Override
	public WatchKey register(WatchService watcher, WatchEvent.Kind<?>... events) throws IOException {
		// throw new UnsupportedOperationException();
		WatchKey watchKey = delegatePath.register(watcher, events);
		WatchKey newWatchKey = MyWatchKey.wrap(watchKey);
		if (debug) {
			String eventsStr = (events != null) ? Arrays.toString(events) : "null";
			Printer.println(MessageFormat.format("MyPath.register(WatchService, WatchEvent.Kind) watcher = {0}, events = {1} returns [{2}]", new Object[] { watcher, eventsStr, newWatchKey }));
		}
		return newWatchKey;
	}

	@Override
	public int compareTo(Path other) {
		int compareTo = delegatePath.compareTo(unwrap(other));
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.compareTo(Path) other = {0} returns [{1}]", new Object[] { other, compareTo }));
		}
		return compareTo;
	}

	@Override
	public boolean equals(Object other) {
		boolean equals = false;
		if (other instanceof Path) {
			equals = this.delegatePath.equals(unwrap((Path) other));
		}
		if (debug) {
			Printer.println(MessageFormat.format("MyPath.equals(Object) other = {0} returns [{1}]", new Object[] { other, equals }));
		}
		return equals;
	}

	@Override
	public int hashCode() {
		int hashCode = this.delegatePath.hashCode();
		if (debug) {
			// Printer.println(MessageFormat.format("MyPath.hashCode() returns [{0}]", new Object[] { hashCode }));
		}
		return hashCode;
	}

	@Override
	public String toString() {
		String toString = this.delegatePath.toString();
		if (debug) {
			// Printer.println(MessageFormat.format("MyPath.toString() returns [{0}]", new Object[] { toString }));
		}
		return toString;
	}

}

// if (path == null) {
// throw new NullPointerException();
// }
// if (!(path instanceof PassThroughPath)) {
// throw new ProviderMismatchException();
// }