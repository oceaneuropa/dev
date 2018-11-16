package org.orbit.infra.runtime.datatube.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.orbit.infra.api.datacast.ChannelMetadata;
import org.orbit.infra.runtime.datatube.service.MessageListener;
import org.orbit.infra.runtime.datatube.service.RuntimeChannel;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.model.DateRecordSupport;
import org.origin.common.model.TransientPropertySupport;

public class RuntimeChannelImpl implements RuntimeChannel {

	protected static int DEFAULT_CORE_THREADS_NUM = 1;
	protected static int DEFAULT_MAX_THREADS_NUM = 10;
	protected static int DEFAULT_KEEP_ALIVE_SECONDS = 20;

	protected ChannelMetadata channelMetadata;
	protected ThreadPoolExecutor threadPoolExecutor;
	protected int coreThreadsNum = DEFAULT_CORE_THREADS_NUM;
	protected int maxThreadsNum = DEFAULT_MAX_THREADS_NUM;
	protected int keepAliveSeconds = DEFAULT_KEEP_ALIVE_SECONDS;

	protected List<MessageListener> messageListeners;
	protected MessageListenerSupportImpl messageListenerSupport = new MessageListenerSupportImpl();
	protected DateRecordSupport<Long> dateRecordSupport = new DateRecordSupport<Long>();
	protected TransientPropertySupport transientPropertySupport = new TransientPropertySupport();
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param channelMetadata
	 */
	public RuntimeChannelImpl(ChannelMetadata channelMetadata) {
		this.channelMetadata = channelMetadata;
		this.messageListeners = new ArrayList<MessageListener>();
		this.threadPoolExecutor = createThreadPoolExecutor();
	}

	@Override
	public ChannelMetadata getChannelMetadata() {
		return this.channelMetadata;
	}

	@Override
	public void setChannelMetadata(ChannelMetadata channelMetadata) {
		this.channelMetadata = channelMetadata;
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

	/** MessageListener */
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

	@Override
	public void dispose() {
		this.messageListenerSupport.dispose();
	}

	/** MessageListenerSupport */
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

	/** DateRecordAware */
	@Override
	public Long getDateCreated() {
		return this.dateRecordSupport.getDateCreated();
	}

	@Override
	public void setDateCreated(Long dateCreated) {
		this.dateRecordSupport.setDateCreated(dateCreated);
	}

	@Override
	public Long getDateModified() {
		return this.dateRecordSupport.getDateModified();
	}

	@Override
	public void setDateModified(Long dateModified) {
		this.dateRecordSupport.setDateModified(dateModified);
	}

	/** TransientPropertyAware */
	@Override
	public <T> T getTransientProperty(String key) {
		return this.transientPropertySupport.getTransientProperty(key);
	}

	@Override
	public <T> T setTransientProperty(String key, T value) {
		return this.transientPropertySupport.setTransientProperty(key, value);
	}

	@Override
	public <T> T removeTransientProperty(String key) {
		return this.transientPropertySupport.removeTransientProperty(key);
	}

	/** IAdaptable */
	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
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
