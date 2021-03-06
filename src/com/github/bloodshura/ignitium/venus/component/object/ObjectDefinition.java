package com.github.bloodshura.ignitium.venus.component.object;

import com.github.bloodshura.ignitium.collection.list.XList;
import com.github.bloodshura.ignitium.collection.list.impl.XArrayList;
import com.github.bloodshura.ignitium.venus.component.Container;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.type.ObjectType;
import com.github.bloodshura.ignitium.venus.type.Type;

public class ObjectDefinition extends Container {
	private final XList<Attribute> attributes;
	private final String name;
	private final Type type;

	public ObjectDefinition(String name) {
		this.attributes = new XArrayList<>();
		this.name = name;
		this.type = new ObjectType(name);
	}

	public XList<Attribute> getAttributes() {
		return attributes;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	@Override
	public void setParent(Container parent) {
		super.setParent(parent);
		this.context = new Context(this, parent.getContext());
	}

	@Override
	public String toString() {
		return "objectdef(" + getName() + ')';
	}
}