package com.osgi.example1.nio;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileStoreAttributeView;
import java.text.MessageFormat;

public class MyFileStore extends FileStore {

	protected static boolean debug = true;

	public static FileStore wrap(FileStore fileStore) {
		FileStore resultFileStore = (fileStore != null) ? ((fileStore instanceof MyFileStore) ? fileStore : new MyFileStore(fileStore)) : fileStore;
		// if (debug) {
		// Printer.println(MessageFormat.format("FileStore.wrap(FileStore) fileStore = ''{0}'' returns FileStore ''{1}''", new Object[] { fileStore,
		// resultFileStore }));
		// }
		return resultFileStore;
	}

	public static FileStore _unwrap(FileStore fileStore) {
		FileStore resultFileStore = (fileStore instanceof MyFileStore) ? ((MyFileStore) fileStore).delegate : fileStore;
		// if (debug) {
		// Printer.println(MessageFormat.format("FileStore.unwrap(FileStore) fileStore = ''{0}'' returns FileStore ''{1}''", new Object[] { fileStore,
		// resultFileStore }));
		// }
		return resultFileStore;
	}

	protected final FileStore delegate;

	/**
	 * 
	 * @param delegate
	 */
	public MyFileStore(FileStore fileStore) {
		if (debug) {
			Printer.println(MessageFormat.format("new MyFileStore(FileStore) fileStore = {0}", new Object[] { fileStore }));
		}
		this.delegate = fileStore;
	}

	@Override
	public String name() {
		String name = this.delegate.name();
		if (debug) {
			Printer.println(MessageFormat.format("MyFileStore.name() returns String [{0}]", new Object[] { name }));
		}
		return name;
	}

	@Override
	public String type() {
		String type = this.delegate.type();
		if (debug) {
			Printer.println(MessageFormat.format("MyFileStore.type() returns String [{0}]", new Object[] { type }));
		}
		return type;
	}

	@Override
	public boolean isReadOnly() {
		boolean isReadOnly = this.delegate.isReadOnly();
		if (debug) {
			Printer.println(MessageFormat.format("MyFileStore.isReadOnly() returns boolean [{0}]", new Object[] { isReadOnly }));
		}
		return isReadOnly;
	}

	@Override
	public long getTotalSpace() throws IOException {
		long totalSpace = this.delegate.getTotalSpace();
		if (debug) {
			Printer.println(MessageFormat.format("MyFileStore.getTotalSpace() returns long [{0}]", new Object[] { totalSpace }));
		}
		return totalSpace;
	}

	@Override
	public long getUsableSpace() throws IOException {
		long usableSpace = this.delegate.getUsableSpace();
		if (debug) {
			Printer.println(MessageFormat.format("MyFileStore.getUsableSpace() returns long [{0}]", new Object[] { usableSpace }));
		}
		return usableSpace;
	}

	@Override
	public long getUnallocatedSpace() throws IOException {
		long unallocatedSpace = this.delegate.getUnallocatedSpace();
		if (debug) {
			Printer.println(MessageFormat.format("MyFileStore.getUnallocatedSpace() returns long [{0}]", new Object[] { unallocatedSpace }));
		}
		return unallocatedSpace;
	}

	@Override
	public boolean supportsFileAttributeView(Class<? extends FileAttributeView> type) {
		boolean support = this.delegate.supportsFileAttributeView(type);
		if (debug) {
			Printer.println(MessageFormat.format("MyFileStore.supportsFileAttributeView(Class<? extends FileAttributeView>) type = {0} returns boolean [{1}]", new Object[] { type, support }));
		}
		return support;
	}

	@Override
	public boolean supportsFileAttributeView(String name) {
		boolean support = this.delegate.supportsFileAttributeView(name);
		if (debug) {
			Printer.println(MessageFormat.format("MyFileStore.supportsFileAttributeView() name = ''{0}'' returns boolean [{1}]", new Object[] { name, support }));
		}
		return support;
	}

	@Override
	public <V extends FileStoreAttributeView> V getFileStoreAttributeView(Class<V> type) {
		V attrView = this.delegate.getFileStoreAttributeView(type);
		if (debug) {
			Printer.println(MessageFormat.format("MyFileStore.getFileStoreAttributeView(Class<V>) type = {0} returns <V extends FileStoreAttributeView> [{1}]", new Object[] { type, attrView }));
		}
		return attrView;
	}

	@Override
	public Object getAttribute(String attribute) throws IOException {
		Object attr = this.delegate.getAttribute(attribute);
		if (debug) {
			Printer.println(MessageFormat.format("MyFileStore.getAttribute(String) attribute = {0} returns Object [{01]", new Object[] { attribute, attr }));
		}
		return attr;
	}

}
