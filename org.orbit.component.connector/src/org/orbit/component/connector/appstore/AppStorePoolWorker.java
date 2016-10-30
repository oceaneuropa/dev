package org.orbit.component.connector.appstore;

/**
 * The pool worker periodically accessing the distributed indexing service to get the registered app store server instances by checking the index
 * items of app store services. The pool worker then can use the result to update the AppStorePool by (1) creating new AppStoreImpl instance for new
 * index item and adding it to the pool; and (2) removing AppStoreImpl instance from the pool when the index item's heart beat time expired. (The
 * worker could perform an additional ping action with the AppStoreImpl to confirm an unavailable app store server before removing the AppStoreImpl
 * from the pool.)
 * 
 * An app store server instance, on the other hand, when started, should registers itself to the indexing service as an index item with properties
 * (e.g. url, payload, last heart beat time, etc.) for accessing the app store server. The app store server should periodically updates the heart beat
 * time on its index item, so that app store clients can know (by accessing the distributed indexing service): (1) what are the available app store
 * server instances; and (2) the availability of each app store server instance.
 * 
 * 
 * 
 */
public class AppStorePoolWorker {

}
