package other.orbit.infra.runtime.indexes.service;

import java.util.Map;

import org.origin.common.command.AbstractCommand;

/*
 * 
 * 1. IndexItemRequest Table
 * ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * requestId	indexProviderId			command					arguments																								status							requestTime				lastUpdateTime
 * 101			origin.index.provider	create_index_item 		{indexproviderid="origin.index.provider", type="tns1", name="name1", properties={p1=v1, p2=v2}}	pending/cancelled/completed		05-20-2016 13:53:30		05-20-2016 13:53:32
 * 102			origin.index.provider	create_index_item		{indexproviderid="origin.index.provider", type="tns1", name="name2", properties={p3=v3, p4=v4}}	pending/cancelled/completed		05-20-2016 14:02:20		05-20-2016 14:02:23
 * 103			origin.index.provider	update_index_item		{indexitemid="1", properties={p1=v1b, p2=v2b}}															pending/cancelled/completed		05-20-2016 14:05:01		05-20-2016 14:05:05
 * 104			origin.index.provider	update_index_item		{indexitemid="2", properties={p3=v3b, p4=v4b}}															pending/cancelled/completed		05-20-2016 14:06:01		05-20-2016 14:06:05
 * 105			origin.index.provider	delete_index_item		{indexitemid="1"}																						pending/cancelled/completed		05-20-2016 14:07:01		05-20-2016 14:07:05
 * 106			origin.index.provider	delete_index_item		{indexitemid="2"}																						pending/cancelled/completed		05-20-2016 14:08:01		05-20-2016 14:08:05
 * ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * 
 * 
 * 2. IndexItemData Table
 * (After 101 and 102) 
 * ----------------------------------------------------------------------------------------------------------------------------
 * indexItemId	indexProviderId			type	name		properties			createTime				lastUpdateTime
 * ----------------------------------------------------------------------------------------------------------------------------
 * 11			origin.index.provider	tns1		name1		{p1=v1, p2=v2}		05-20-2016 13:53:32		05-20-2016 13:53:32
 * 12			origin.index.provider	tns1		name2		{p3=v3, p4=v4}		05-20-2016 14:02:23		05-20-2016 14:02:23
 * ----------------------------------------------------------------------------------------------------------------------------
 * 
 * (After 103 and 104)
 * ----------------------------------------------------------------------------------------------------------------------------
 * indexItemId	indexProviderId			type	name		properties			createTime				lastUpdateTime
 * ----------------------------------------------------------------------------------------------------------------------------
 * 11			origin.index.provider	tns1		name1		{p1=v1b, p2=v2b}	05-20-2016 13:53:32		05-20-2016 14:05:05
 * 12			origin.index.provider	tns1		name2		{p3=v3b, p4=v4b}	05-20-2016 14:02:23		05-20-2016 14:06:05
 * ----------------------------------------------------------------------------------------------------------------------------
 * 
 * (After 105 and 106)
 * --------------------------------------------------------------------------------------------------------
 * indexItemId	indexProviderId			type	name	lastUpdateTime 			properties			
 * --------------------------------------------------------------------------------------------------------
 * (empty list)	
 * --------------------------------------------------------------------------------------------------------
 * 
 * 
 * 3. IndexItemRevision Table
 * --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * revisionId 	command					arguments																																		undoCommand				undoArguments																																					updateTime
 * 1001			create_index_item 		{indexProviderId="origin.index.provider", type="tns1", name="name1", properties={p1=v1, p2=v2}, lastUpdateTime="05-20-2016 13:53:30"}		delete_index_item		{indexItemId=1}																																					{currentTime}
 * 1002			create_index_item 		{indexProviderId="origin.index.provider", type="tns1", name="name2", properties={p3=v3, p4=v4}, lastUpdateTime="05-20-2016 14:02:20"}		delete_index_item		{indexItemId=2}																																					{currentTime}
 * 1003			update_index_item 		{indexItemId=1, properties={p1=v1b, p2=v2b}, lastUpdateTime="05-20-2016 14:02:20"}																update_index_item		{indexitemid=1, lastUpdateTime="05-20-2016 13:53:32", properties={p1=v1, p2=v2}}																				{currentTime}
 * 1004			update_index_item 		{indexItemId=2, properties={p3=v3b, p4=v4b}, lastUpdateTime="05-20-2016 14:02:20"}																update_index_item		{indexitemid=2, lastUpdateTime="05-20-2016 14:02:23", properties={p3=v3, p4=v4}}																				{currentTime}
 * 1005			delete_index_item 		{indexItemId=1}																																	create_index_item		{indexitemid=1, indexproviderid="origin.index.provider", type="tns1", name="name1", properties={p1=v1b, p2=v2b}, lastUpdateTime="05-20-2016 14:05:05"}		{currentTime}
 * 1006			delete_index_item 		{indexItemId=2}																																	create_index_item		{indexitemid=2, indexproviderid="origin.index.provider", type="tns1", name="name2", properties={p3=v3b, p4=v4b}, lastUpdateTime="05-20-2016 14:06:05"}		{currentTime}
 * --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 *
 */
public class IndexItemCommandFactory {

	public AbstractCommand createCommand(String commandName, Map<String, Object> arguments) {
		if (commandName == null || commandName.isEmpty()) {
			throw new IllegalArgumentException("Command name is empty.");
		}
		AbstractCommand command = null;

		if (CreateIndexItemCommand.COMMAND_NAME.equalsIgnoreCase(commandName)) {
			command = CreateIndexItemCommand.parseCommand(arguments);

		} else if (UpdateIndexItemPropertyCommand.COMMAND_NAME.equalsIgnoreCase(commandName)) {

		} else if (DeleteIndexItemCommand.COMMAND_NAME.equalsIgnoreCase(commandName)) {

		}
		return command;
	}

}
