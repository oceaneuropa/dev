package org.origin.common.service;

import java.util.ArrayList;
import java.util.List;

public class CLIAwareImpl implements CLIAware {

	public static class CommandImpl implements Command {
		protected String name;
		protected List<Parameter> parameters = new ArrayList<Parameter>();

		public CommandImpl() {
		}

		@Override
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public Parameter[] getParameters() {
			return this.parameters.toArray(new Parameter[this.parameters.size()]);
		}

		public void addParameter(Parameter parameter) {
			if (parameter != null && !this.parameters.contains(parameter)) {
				this.parameters.add(parameter);
			}
		}

		public void removeParameter(Parameter parameter) {
			if (parameter != null && this.parameters.contains(parameter)) {
				this.parameters.remove(parameter);
			}
		}
	}

	public static class ParameterImpl implements Parameter {
		protected String name;

		public ParameterImpl() {
		}

		@Override
		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	protected String name;
	protected List<Command> commands = new ArrayList<Command>();

	public CLIAwareImpl() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Command[] getCommands() {
		return this.commands.toArray(new Command[this.commands.size()]);
	}

	public void addCommand(Command command) {
		if (command != null && !this.commands.contains(command)) {
			this.commands.add(command);
		}
	}

	public void removeCommand(Command command) {
		if (command != null && this.commands.contains(command)) {
			this.commands.remove(command);
		}
	}

}
