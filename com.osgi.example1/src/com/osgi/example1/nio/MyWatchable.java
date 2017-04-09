package com.osgi.example1.nio;

import java.io.IOException;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.Watchable;
import java.text.MessageFormat;
import java.util.Arrays;

public class MyWatchable implements Watchable {

	protected static boolean debug = true;

	/**
	 * 
	 * @param watchable
	 * @return
	 */
	public static Watchable wrap(Watchable watchable) {
		Watchable resultWatchable = (watchable != null) ? ((watchable instanceof MyWatchable) ? watchable : new MyWatchable(watchable)) : null;
		if (debug) {
			Printer.println(MessageFormat.format("MyWatchKey.wrap(WatchKey) watchKey = ''{0}'' returns WatchKey [{1}]", new Object[] { watchable, resultWatchable }));
		}
		return resultWatchable;
	}

	/**
	 * 
	 * @param watchable
	 * @return
	 */
	public static Watchable unwrap(Watchable watchable) {
		Watchable resultWatchable = (watchable instanceof MyWatchable) ? ((MyWatchable) watchable).delegate : watchable;
		if (debug) {
			Printer.println(MessageFormat.format("MyWatchKey.unwrap(WatchKey) watchKey = ''{0}'' returns WatchKey [{1}]", new Object[] { watchable, resultWatchable }));
		}
		return resultWatchable;
	}

	protected final Watchable delegate;

	public MyWatchable(Watchable watchable) {
		if (debug) {
			Printer.println(MessageFormat.format("MyWatchable(Watchable) watchable = {0}", new Object[] { watchable }));
		}
		this.delegate = watchable;
	}

	@Override
	public WatchKey register(WatchService watcher, Kind<?>... events) throws IOException {
		WatchKey watchKey = this.delegate.register(watcher, events);
		WatchKey newWatchKey = MyWatchKey.wrap(watchKey);
		if (debug) {
			String eventsStr = (events != null) ? Arrays.toString(events) : "null";
			Printer.println(MessageFormat.format("MyWatchable.register(WatchService, Kind<?>...) returns WatchKey [{0}]", new Object[] { watcher, eventsStr, newWatchKey }));
		}
		return newWatchKey;
	}

	@Override
	public WatchKey register(WatchService watcher, Kind<?>[] events, Modifier... modifiers) throws IOException {
		WatchKey watchKey = this.delegate.register(watcher, events, modifiers);
		WatchKey newWatchKey = MyWatchKey.wrap(watchKey);
		if (debug) {
			String eventsStr = (events != null) ? Arrays.toString(events) : "null";
			String modifiersStr = (modifiers != null) ? Arrays.toString(modifiers) : "null";
			Printer.println(MessageFormat.format("MyWatchable.register(WatchService, Kind<?>, Modifier...) returns WatchKey [{0}]", new Object[] { watcher, eventsStr, modifiersStr, newWatchKey }));
		}
		return newWatchKey;
	}

}
