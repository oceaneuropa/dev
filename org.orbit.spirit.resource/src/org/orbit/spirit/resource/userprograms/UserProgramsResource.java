package org.orbit.spirit.resource.userprograms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

import org.orbit.spirit.model.userprograms.UserPrograms;
import org.orbit.spirit.resource.util.UserProgramsReader;
import org.orbit.spirit.resource.util.UserProgramsWriter;
import org.origin.common.resource.impl.ResourceImpl;

public class UserProgramsResource extends ResourceImpl {

	/**
	 * 
	 * @param uri
	 */
	public UserProgramsResource(URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(InputStream input) throws IOException {
		UserProgramsReader reader = new UserProgramsReader();
		reader.read(this, input);
	}

	@Override
	protected void doSave(OutputStream output) throws IOException {
		UserProgramsWriter writer = new UserProgramsWriter();
		writer.write(this, output);
	}

	public UserPrograms getUserPrograms() {
		UserPrograms userPrograms = null;
		List<Object> contents = getContents();
		for (Object content : contents) {
			if (content instanceof UserPrograms) {
				userPrograms = (UserPrograms) content;
				break;
			}
		}
		return userPrograms;
	}

}
