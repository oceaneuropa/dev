package org.orbit.component.webconsole.servlet.appstore;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadServlet1 extends HttpServlet {

	private static final long serialVersionUID = 226770343648937884L;

	protected final int ARBITARY_SIZE = 1048;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		resp.setHeader("Content-disposition", "attachment; filename=sample.txt");

		InputStream in = req.getServletContext().getResourceAsStream("/WEB-INF/sample.txt");

		OutputStream out = resp.getOutputStream();
		byte[] buffer = new byte[ARBITARY_SIZE];

		int numBytesRead;
		while ((numBytesRead = in.read(buffer)) > 0) {
			out.write(buffer, 0, numBytesRead);
		}
	}

}
