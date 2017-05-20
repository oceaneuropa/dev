package org.orbit.component.api.tier3.domain;

import java.util.Map;

import org.orbit.component.api.tier3.domain.request.AddMachineRequest;
import org.orbit.component.api.tier3.domain.request.AddTransferAgentRequest;
import org.orbit.component.api.tier3.domain.request.UpdateMachineRequest;
import org.orbit.component.api.tier3.domain.request.UpdateTransferAgentRequest;
import org.origin.common.rest.client.ClientException;

public interface DomainMgmt {

	/**
	 * Get service name.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Get service URL.
	 * 
	 * @return
	 */
	String getURL();

	/**
	 * Get configuration properties.
	 * 
	 * @return
	 */
	Map<String, Object> getProperties();

	/**
	 * Update configuration properties.
	 * 
	 * @param properties
	 */
	void update(Map<String, Object> properties);

	/**
	 * Ping the service.
	 * 
	 * @return
	 */
	public boolean ping();

	// ------------------------------------------------------
	// Machine management
	// ------------------------------------------------------
	public MachineConfig[] getMachines() throws ClientException;

	public MachineConfig addMachine(String machineId) throws ClientException;

	public boolean addMachine(AddMachineRequest addMachineRequest) throws ClientException;

	public boolean updateMachine(UpdateMachineRequest updateMachineRequest) throws ClientException;

	public boolean removeMachine(String machineId) throws ClientException;

	public Map<String, Object> getMachineProperties(String machineId);

	public boolean setMachineProperty(String machineId, String name, Object value);

	public Object getMachineProperty(String machineId, String name);

	public boolean removeMachineProperty(String machineId, String name);

	// ------------------------------------------------------
	// TransferAgent management
	// ------------------------------------------------------
	public TransferAgentConfig[] getTransferAgents(String machineId) throws ClientException;

	public TransferAgentConfig getTransferAgent(String machineId, String transferAgentId) throws ClientException;

	public boolean addTransferAgent(String machineId, AddTransferAgentRequest addTransferAgentRequest) throws ClientException;

	public boolean updateTransferAgent(String machineId, UpdateTransferAgentRequest updateTransferAgentRequest) throws ClientException;

	public boolean removeTransferAgent(String machineId, String transferAgentId) throws ClientException;

	public Map<String, Object> getTransferAgentProperties(String machineId, String transferAgentId);

	public boolean setTransferAgentProperty(String machineId, String transferAgentId, String name, Object value);

	public Object getTransferAgentProperty(String machineId, String transferAgentId, String name);

	public boolean removeTransferAgentProperty(String machineId, String transferAgentId, String name);

	// ------------------------------------------------------
	// TransferAgent life cycle
	// ------------------------------------------------------
	// public TransferAgent getTransferAgent(String machineId, String transferAgentId);

}
