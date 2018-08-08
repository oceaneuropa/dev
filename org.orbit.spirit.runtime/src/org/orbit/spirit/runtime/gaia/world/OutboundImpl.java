package org.orbit.spirit.runtime.gaia.world;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;

public class OutboundImpl implements Outbound {

	public class Context {
		protected PipedInputStream input;
		protected Thread thread;
		protected JsonParser jsonParser;
		protected boolean isClosed = false;

		public PipedInputStream getInput() {
			return input;
		}

		public void setInput(PipedInputStream input) {
			this.input = input;
		}

		public Thread getThread() {
			return thread;
		}

		public void setThread(Thread thread) {
			this.thread = thread;
		}

		public JsonParser getJsonParser() {
			return jsonParser;
		}

		public void setJsonParser(JsonParser jsonParser) {
			this.jsonParser = jsonParser;
		}

		public boolean isClosed() {
			return isClosed;
		}

		public void setClosed(boolean isClosed) {
			this.isClosed = isClosed;
		}
	}

	protected JsonParserFactory jsonFactory;
	protected PipedOutputStream output;
	protected Map<PipedOutputStream, Context> contextMap = new HashMap<PipedOutputStream, Context>();

	public OutboundImpl() {
		this.jsonFactory = Json.createParserFactory(null);
	}

	@Override
	public OutputStream getOutputStream() {
		if (this.output == null) {
			this.output = new PipedOutputStream();
			try {
				final Context context = new Context();
				final PipedInputStream input = new PipedInputStream(this.output);
				Thread thread = new Thread() {
					@Override
					public void run() {
						JsonParser jsonParser = parse(jsonFactory, input, context);
						context.setJsonParser(jsonParser);
					}
				};
				context.setInput(input);
				context.setThread(thread);
				this.contextMap.put(this.output, context);

				thread.start();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this.output;
	}

	@Override
	public void close() {
		if (this.output != null) {
			try {
				this.output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Context context = this.contextMap.remove(this.output);
			if (context != null) {
				context.setClosed(true);

				PipedInputStream input = context.getInput();
				if (input != null) {
					try {
						System.out.println("PipedInputStream is being closed...");
						input.close();
						System.out.println("PipedInputStream is closed.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				JsonParser jsonParser = context.getJsonParser();
				if (jsonParser != null) {
					System.out.println("jsonParser is being closed...");
					jsonParser.close();
					System.out.println("JsonParser is closed.");
				}

				// Thread thread = context.getThread();
			}

			this.output = null;
		}
	}

	/**
	 * 
	 * @param jsonFactory
	 * @param input
	 * @param context
	 * @return
	 */
	protected JsonParser parse(JsonParserFactory jsonFactory, InputStream input, Context context) {
		final JsonParser jsonParser = jsonFactory.createParser(input);
		while (jsonParser.hasNext()) {
			if (context.isClosed()) {
				System.out.println("Context is closed.");
				return jsonParser;
			}

			JsonParser.Event event = jsonParser.next();
			switch (event) {
			case START_OBJECT: {
				// handler.startObject();
				System.out.println("START_OBJECT");
				break;
			}
			case START_ARRAY: {
				// handler.startArray();
				System.out.println("START_ARRAY");
				break;
			}
			case KEY_NAME: {
				// handler.keyName(jsonParser.getString());
				String name = jsonParser.getString();
				System.out.println("KEY_NAME String:" + name);
				break;
			}
			case VALUE_NULL: {
				// handler.nullValue();
				System.out.println("VALUE_NULL");
				break;
			}
			case VALUE_FALSE: {
				// handler.booleanValue(false);
				System.out.println("VALUE_FALSE Boolean:false");
				break;
			}
			case VALUE_TRUE: {
				// handler.booleanValue(true);
				System.out.println("VALUE_TRUE Boolean:true");
				break;
			}
			case VALUE_NUMBER: {
				// handler.numberValue(jsonParser.getBigDecimal());
				BigDecimal bigDecimal = jsonParser.getBigDecimal();
				System.out.println("VALUE_NUMBER BigDecimal:" + bigDecimal.toString());
				break;
			}
			case VALUE_STRING: {
				// handler.stringValue(jsonParser.getString());
				String string = jsonParser.getString();
				System.out.println("VALUE_STRING String:" + string);
				break;
			}
			case END_OBJECT: {
				// handler.endObject();
				System.out.println("END_OBJECT");
				break;
			}
			case END_ARRAY: {
				// handler.endArray();
				System.out.println("END_ARRAY");
				break;
			}
			default: {
				throw new IllegalStateException("Unsupported JsonParser.EVENT: " + event);
			}
			}
		} // event loop

		// jsonParser.close();
		System.out.println("jsonParser.hasNext() is false. Should jsonParser.close() here or outside by caller?");
		return jsonParser;
	}

}
