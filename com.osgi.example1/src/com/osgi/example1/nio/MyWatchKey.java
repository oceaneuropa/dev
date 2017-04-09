package com.osgi.example1.nio;

import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.Watchable;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

public class MyWatchKey implements WatchKey {

	protected static boolean debug = true;

	/**
	 * 
	 * @param watchKey
	 * @return
	 */
	public static WatchKey wrap(WatchKey watchKey) {
		WatchKey resultWatchKey = (watchKey != null) ? ((watchKey instanceof MyWatchKey) ? watchKey : new MyWatchKey(watchKey)) : null;
		if (debug) {
			Printer.println(MessageFormat.format("MyWatchKey.wrap(WatchKey) watchKey = ''{0}'' returns WatchKey [{1}]", new Object[] { watchKey, resultWatchKey }));
		}
		return resultWatchKey;
	}

	/**
	 * 
	 * @param watchKey
	 * @return
	 */
	public static WatchKey unwrap(WatchKey watchKey) {
		WatchKey resultWatchKey = (watchKey instanceof MyWatchKey) ? ((MyWatchKey) watchKey).delegate : watchKey;
		if (debug) {
			Printer.println(MessageFormat.format("MyWatchKey.unwrap(WatchKey) watchKey = ''{0}'' returns WatchKey [{1}]", new Object[] { watchKey, resultWatchKey }));
		}
		return resultWatchKey;
	}

	protected final WatchKey delegate;

	public MyWatchKey(WatchKey watchKey) {
		if (debug) {
			Printer.println(MessageFormat.format("MyWatchKey(WatchKey) watchKey = {0}", new Object[] { watchKey }));
		}
		this.delegate = watchKey;
	}

	@Override
	public boolean isValid() {
		boolean isValid = this.delegate.isValid();
		if (debug) {
			Printer.println(MessageFormat.format("MyWatchKey.isValid() returns boolean [{0}]", new Object[] { isValid }));
		}
		return isValid;
	}

	@Override
	public List<WatchEvent<?>> pollEvents() {
		List<WatchEvent<?>> events = this.delegate.pollEvents();
		if (debug) {
			String eventsStr = (events != null) ? Arrays.toString(events.toArray(new WatchEvent<?>[events.size()])) : "null";
			Printer.println(MessageFormat.format("MyWatchKey.pollEvents() returns List<WatchEvent<?>> [{0}]", new Object[] { eventsStr }));
		}
		return events;
	}

	@Override
	public Watchable watchable() {
		Watchable watchable = this.delegate.watchable();
		Watchable newWatchable = MyWatchable.wrap(watchable);
		if (debug) {
			Printer.println(MessageFormat.format("MyWatchKey.watchable() returns Watchable [{0}]", new Object[] { newWatchable }));
		}
		return newWatchable;
	}

	@Override
	public boolean reset() {
		boolean reset = this.delegate.reset();
		if (debug) {
			Printer.println(MessageFormat.format("MyWatchKey.reset() returns boolean [{0}]", new Object[] { reset }));
		}
		return reset;
	}

	@Override
	public void cancel() {
		this.delegate.cancel();
		if (debug) {
			Printer.println(MessageFormat.format("MyWatchKey.cancel()", new Object[] {}));
		}
	}

}
