package org.origin.common.cluster.impl.jgroup;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jgroups.JChannel;
import org.origin.common.cluster.Event;
import org.origin.common.cluster.EventBus;
import org.origin.common.cluster.EventGroup;
import org.origin.common.cluster.EventListener;

public class EventBusImpl implements EventBus {

	protected Map<JChannel, EventGroup> channelToEventGroupMap = new HashMap<JChannel, EventGroup>();

	@Override
	public boolean addEventListener(EventListener eventListener, String... clusterNames) {
		if (clusterNames == null || clusterNames.length == 0) {
			return false;
		}

		boolean hasSucceed = false;
		boolean hasFailed = false;

		synchronized (this.channelToEventGroupMap) {
			for (String clusterName : clusterNames) {
				clusterName = checkClusterName(clusterName);

				JChannel channel = findChannel(clusterName);
				if (channel == null) {
					try {
						channel = createChannel(clusterName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (channel != null) {
					EventGroup eventGroup = this.channelToEventGroupMap.get(channel);
					if (eventGroup == null) {
						eventGroup = new EventGroupImpl(clusterName, channel);
						try {
							eventGroup.start();
						} catch (Exception e) {
							e.printStackTrace();
						}
						this.channelToEventGroupMap.put(channel, eventGroup);
					}

					if (eventGroup != null) {
						boolean succeed = eventGroup.addEventListener(eventListener);
						if (succeed) {
							hasSucceed = true;
						} else {
							hasFailed = true;
						}
					}
				}
			}
		}

		return (hasSucceed && !hasFailed) ? true : false;
	}

	@Override
	public boolean removeEventListener(EventListener eventListener, String... clusterNames) {
		if (clusterNames == null || clusterNames.length == 0) {
			return false;
		}

		boolean hasSucceed = false;
		boolean hasFailed = false;

		synchronized (this.channelToEventGroupMap) {
			for (String clusterName : clusterNames) {
				clusterName = checkClusterName(clusterName);

				JChannel channel = findChannel(clusterName);
				if (channel != null) {
					EventGroup eventGroup = this.channelToEventGroupMap.get(channel);

					if (eventGroup != null) {
						boolean succeed = eventGroup.removeEventListener(eventListener);
						if (succeed) {
							hasSucceed = true;
						} else {
							hasFailed = true;
						}
					}
				}
			}
		}

		return (hasSucceed && !hasFailed) ? true : false;
	}

	@Override
	public void sendEvent(Event event, String... clusterNames) {
		if (clusterNames == null || clusterNames.length == 0) {
			return;
		}

		synchronized (this.channelToEventGroupMap) {
			for (String clusterName : clusterNames) {
				clusterName = checkClusterName(clusterName);

				JChannel channel = findChannel(clusterName);
				if (channel != null) {
					EventGroup eventGroup = this.channelToEventGroupMap.get(channel);

					if (eventGroup != null) {
						try {
							eventGroup.sendEvent(event);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * Leave a cluster by closing JCannel and EventGroup.
	 * 
	 * @param clusterName
	 */
	public void leave(String clusterName) {
		clusterName = checkClusterName(clusterName);

		synchronized (this.channelToEventGroupMap) {
			JChannel channel = findChannel(clusterName);
			if (channel != null) {
				EventGroup eventGroup = this.channelToEventGroupMap.remove(channel);

				if (eventGroup != null) {
					try {
						eventGroup.stop();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				channel.close();
			}
		}
	}

	/**
	 * 
	 * @param clusterName
	 * @return
	 */
	protected String checkClusterName(String clusterName) {
		if (clusterName == null) {
			clusterName = GLOBAL_EVENTS_GROUP;
		}
		return clusterName;
	}

	/**
	 * 
	 * @param clusterName
	 * @return
	 */
	protected JChannel findChannel(String clusterName) {
		JChannel channel = null;
		synchronized (this.channelToEventGroupMap) {
			for (Iterator<JChannel> channelItor = this.channelToEventGroupMap.keySet().iterator(); channelItor.hasNext();) {
				JChannel currChannel = channelItor.next();
				if (currChannel.getClusterName().equals(clusterName)) {
					channel = currChannel;
					break;
				}
			}
		}
		return channel;
	}

	/**
	 * 
	 * @param clusterName
	 * @return
	 * @throws Exception
	 */
	protected JChannel createChannel(String clusterName) throws Exception {
		JChannel channel = new JChannel();
		channel.connect(clusterName);
		return channel;
	}

}
