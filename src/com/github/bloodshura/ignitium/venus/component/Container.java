package com.github.bloodshura.ignitium.venus.component;

import com.github.bloodshura.ignitium.collection.list.XList;
import com.github.bloodshura.ignitium.collection.list.impl.XArrayList;
import com.github.bloodshura.ignitium.collection.view.XView;
import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.component.object.ObjectDefinition;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.exception.runtime.UndefinedFunctionException;
import com.github.bloodshura.ignitium.venus.exception.runtime.UndefinedValueTypeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.Definition;
import com.github.bloodshura.ignitium.venus.function.Function;
import com.github.bloodshura.ignitium.venus.type.Type;
import com.github.bloodshura.ignitium.venus.value.FunctionRefValue;
import com.github.bloodshura.ignitium.venus.value.Value;

public abstract class Container extends Component {
	protected Context context;
	private final XList<Component> children;

	public Container() {
		this.children = new XArrayList<>();

		getChildren().addInsertionListener(component -> component.setParent(this));
	}

	public Function findFunction(Context context, String name, XView<Type> argumentTypes) throws ScriptRuntimeException {
		XApi.requireNonNull(name, "name");

		if (context.hasVar(name)) {
			Value value = context.getVarValue(name); // Should not need to catch UndefinedVariableException since we already
			// checked that the variable exists
			if (value instanceof FunctionRefValue) {
				FunctionRefValue reference = (FunctionRefValue) value;

				return context.getOwner().findFunction(context, reference.value(), null);
			}
		}

		Definition foundVarArgs = null;

		for (Definition definition : getChildren().selectType(Definition.class)) {
			if (definition.accepts(name, argumentTypes)) {
				if (definition.isVarArgs()) {
					foundVarArgs = definition;
				} else {
					return definition;
				}
			}
		}

		if (foundVarArgs != null) {
			return foundVarArgs;
		}

		if (hasParent()) {
			try {
				return getParent().findFunction(context, name, argumentTypes);
			} catch (UndefinedFunctionException ignored) {
			}
		}

		throw new UndefinedFunctionException(context, name, argumentTypes);
	}

	public ObjectDefinition findObjectDefinition(Context context, String name) throws ScriptRuntimeException {
		for (ObjectDefinition definition : getChildren().selectType(ObjectDefinition.class)) {
			if (definition.getName().equals(name)) {
				return definition;
			}
		}

		if (hasParent()) {
			try {
				return getParent().findObjectDefinition(context, name);
			} catch (UndefinedValueTypeException ignored) {
			}
		}

		throw new UndefinedValueTypeException(context, name);
	}

	public Type findType(Context context, String name) throws ScriptRuntimeException {
		for (ObjectDefinition definition : getChildren().selectType(ObjectDefinition.class)) {
			if (definition.getName().equals(name)) {
				return definition.getType();
			}
		}

		if (hasParent()) {
			try {
				return getParent().findType(context, name);
			} catch (UndefinedValueTypeException ignored) {
			}
		}

		throw new UndefinedValueTypeException(context, name);
	}

	public XList<Component> getChildren() {
		return children;
	}

	public Context getContext() {
		return context;
	}

	@Override
	public void setParent(Container parent) {
		super.setParent(parent);
		this.context = parent.getContext();
	}
}
