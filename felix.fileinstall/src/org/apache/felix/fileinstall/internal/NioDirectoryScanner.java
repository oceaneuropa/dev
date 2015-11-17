/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.fileinstall.internal;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.osgi.framework.BundleContext;

public class NioDirectoryScanner extends DirectoryScanner {

	protected BundleContext bundleContext;
	protected PathMatcher fileMatcher;
	protected WatcherService watcherService;
	protected Set<File> changedFiles = new HashSet<File>();

	/**
	 * Create a scanner for the specified directory and file filter
	 *
	 * @param directory
	 *            the directory to scan
	 * @param filterString
	 *            a filter for file names
	 */
	public NioDirectoryScanner(BundleContext bundleContext, File directory, String filterString) throws IOException {
		super(directory, filterString);

		this.bundleContext = bundleContext;

		if (filterString != null) {
			this.fileMatcher = FileSystems.getDefault().getPathMatcher("regex:" + filterString);
		} else {
			this.fileMatcher = null;
		}

		this.watcherService = new WatcherService() {
			@Override
			protected void process(Path path) {
				File file = path.toFile();
				while (!file.getParentFile().equals(directory)) {
					file = file.getParentFile();
					if (file == null) {
						return;
					}
				}
				synchronized (changedFiles) {
					changedFiles.add(file);
				}
			}

			@Override
			protected void onRemove(Path path) {
				File file = path.toFile();
				while (!file.getParentFile().equals(directory)) {
					file = file.getParentFile();
					if (file == null) {
						return;
					}
				}
				synchronized (changedFiles) {
					changedFiles.add(file);
				}
			}

			@Override
			protected void debug(String message, Object... args) {
				log(Util.Logger.LOG_DEBUG, message, args);
			}

			@Override
			protected void warn(String message, Object... args) {
				log(Util.Logger.LOG_WARNING, message, args);
			}

			protected void log(int level, String message, Object... args) {
				String msg = String.format(message, args);
				Util.log(bundleContext, level, msg, null);
			}
		};
		this.watcherService.setFileMatcher(fileMatcher);
		this.watcherService.setRootDirectory(this.directory);
		this.watcherService.init();
		this.watcherService.rescan();
	}

	@Override
	public Set<File> scan(boolean reportImmediately) {
		watcherService.processEvents();

		synchronized (changedFiles) {
			if (changedFiles.isEmpty()) {
				return new HashSet<File>();
			}

			Set<File> files = new HashSet<File>();
			Set<File> removedFiles = new HashSet<File>();

			if (reportImmediately) {
				removedFiles.addAll(storedFileChecksumsMap.keySet());
			}

			for (Iterator<File> iterator = changedFiles.iterator(); iterator.hasNext();) {
				File file = iterator.next();
				long lastChecksum = latestFileChecksumsMap.get(file) != null ? (Long) latestFileChecksumsMap.get(file) : 0;
				long storedChecksum = storedFileChecksumsMap.get(file) != null ? (Long) storedFileChecksumsMap.get(file) : 0;
				long newChecksum = checksum(file);

				latestFileChecksumsMap.put(file, newChecksum);

				if (file.exists()) {
					// Only handle file when it does not change anymore and it has changed since last reported
					if ((newChecksum == lastChecksum || reportImmediately)) {
						if (newChecksum != storedChecksum) {
							storedFileChecksumsMap.put(file, newChecksum);
							files.add(file);
						} else {
							iterator.remove();
						}
						if (reportImmediately) {
							removedFiles.remove(file);
						}
					}
				} else {
					// File has been deleted.
					if (!reportImmediately) {
						removedFiles.add(file);
					}
				}
			}

			// Make sure we'll handle a file that has been deleted
			files.addAll(removedFiles);

			// Remove no longer used checksums
			for (File removedFile : removedFiles) {
				latestFileChecksumsMap.remove(removedFile);
				storedFileChecksumsMap.remove(removedFile);
			}

			return files;
		}
	}

	public void close() throws IOException {
		watcherService.close();
	}

}
