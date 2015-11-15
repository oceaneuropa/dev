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
import java.net.URL;

import org.apache.felix.fileinstall.ArtifactListener;

/**
 * An artifact that has been dropped into one watched directory.
 */
public class Artifact {

	protected File file;
	protected File jaredDirectory;
	protected URL jaredUrl;
	protected ArtifactListener artifactListener;
	protected URL transformedUrl;
	protected File transformed;
	protected long bundleId = -1;
	protected long checksum;

	public Artifact() {
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public File getJaredDirectory() {
		return jaredDirectory;
	}

	public void setJaredDirectory(File jaredDirectory) {
		this.jaredDirectory = jaredDirectory;
	}

	public URL getJaredUrl() {
		return jaredUrl;
	}

	public void setJaredUrl(URL jaredUrl) {
		this.jaredUrl = jaredUrl;
	}

	public ArtifactListener getArtifactListener() {
		return artifactListener;
	}

	public void setArtifactListener(ArtifactListener artifactListener) {
		this.artifactListener = artifactListener;
	}

	public File getTransformed() {
		return transformed;
	}

	public void setTransformed(File transformed) {
		this.transformed = transformed;
	}

	public URL getTransformedUrl() {
		return transformedUrl;
	}

	public void setTransformedUrl(URL transformedUrl) {
		this.transformedUrl = transformedUrl;
	}

	public long getBundleId() {
		return bundleId;
	}

	public void setBundleId(long bundleId) {
		this.bundleId = bundleId;
	}

	public long getChecksum() {
		return checksum;
	}

	public void setChecksum(long checksum) {
		this.checksum = checksum;
	}

}
