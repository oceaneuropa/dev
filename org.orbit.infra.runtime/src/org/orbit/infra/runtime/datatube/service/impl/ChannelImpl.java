package org.orbit.infra.runtime.datatube.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.orbit.infra.runtime.datatube.service.Channel;
import org.orbit.infra.runtime.datatube.service.MessageListener;

public class ChannelImpl implements Channel {

	protected static int DEFAULT_CORE_THREADS_NUM = 1;
	protected static int DEFAULT_MAX_THREADS_NUM = 10;
	protected static int DEFAULT_KEEP_ALIVE_SECONDS = 20;

	protected ThreadPoolExecutor threadPoolExecutor;
	protected int coreThreadsNum = DEFAULT_CORE_THREADS_NUM;
	protected int maxThreadsNum = DEFAULT_MAX_THREADS_NUM;
	protected int keepAliveSeconds = DEFAULT_KEEP_ALIVE_SECONDS;

	protected List<MessageListener> messageListeners;
	protected MessageListenerSupportImpl messageListenerSupport = new MessageListenerSupportImpl();

	public ChannelImpl() {
		this.messageListeners = new ArrayList<MessageListener>();
		this.threadPoolExecutor = createThreadPoolExecutor();
	}

	protected ThreadPoolExecutor createThreadPoolExecutor() {
		ThreadFactory threadFactory = getThreadFactory();
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(this.coreThreadsNum, this.maxThreadsNum, this.keepAliveSeconds, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), threadFactory);
		threadPoolExecutor.allowCoreThreadTimeOut(true);
		return threadPoolExecutor;
	}

	protected ThreadFactory getThreadFactory() {
		ThreadFactory threadFactory = new ThreadFactory() {
			@Override
			public Thread newThread(Runnable runnable) {
				Thread thread = new Thread(runnable, "ChannelWorker");
				thread.setDaemon(true);
				return thread;
			}
		};
		return threadFactory;
	}

	/** Implement MessageListener interface */
	@Override
	public int onMessage(final String senderId, final String message) throws IOException {
		for (Iterator<MessageListener> itor = this.messageListeners.iterator(); itor.hasNext();) {
			final MessageListener listener = itor.next();
			this.threadPoolExecutor.execute(new Runnable() {
				@Override
				public void run() {
					listener.onMessage(senderId, message);
				}
			});
		}
		return 1;
	}

	/** Implement MessageListenerSupport interface */
	@Override
	public List<MessageListener> getMessageListeners() {
		return this.messageListenerSupport.getMessageListeners();
	}

	@Override
	public boolean containsMessageListener(MessageListener listener) {
		return this.messageListenerSupport.containsMessageListener(listener);
	}

	@Override
	public boolean addMessageListener(MessageListener listener) {
		return this.messageListenerSupport.addMessageListener(listener);
	}

	@Override
	public synchronized boolean removeMessageListener(MessageListener listener) {
		return this.messageListenerSupport.removeMessageListener(listener);
	}

	@Override
	public void dispose() {
		this.messageListenerSupport.dispose();
	}

}

// corePoolSize (0)
// --- the number of threads to keep in the pool, even if they are idle, unless allowCoreThreadTimeOut is set.
// maximumPoolSize (10)
// --- max number of pooled threads
// keepAliveTime (20 seconds)
// --- when the number of threads is greater than the core, this is the maximum time that excess idle threads will wait for new tasks before
// terminating.
// workQueue (SynchronousQueue)
// --- the queue to use for holding tasks before they are executed. This queue will hold only the Runnable tasks submitted by the execute method.
// threadFactory (ThreadFactory)
// --- the factory to use when the executor creates a new thread

// This threadPoolExecutor doesn't keep idle threads in the pool.
// Every idle thread wait for 20 seconds and then gets terminated.
