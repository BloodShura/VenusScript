package com.github.bloodshura.ignitium.venus.component;

import com.github.bloodshura.ignitium.activity.logging.XLogger;
import com.github.bloodshura.ignitium.collection.list.XList;
import com.github.bloodshura.ignitium.collection.list.impl.XArrayList;
import com.github.bloodshura.ignitium.collection.view.XView;
import com.github.bloodshura.ignitium.venus.compiler.VenusParser;
import com.github.bloodshura.ignitium.venus.exception.compile.ScriptCompileException;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.ApplicationContext;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.Function;
import com.github.bloodshura.ignitium.venus.library.LibraryList;
import com.github.bloodshura.ignitium.venus.origin.ScriptOrigin;
import com.github.bloodshura.ignitium.venus.type.Type;

public class Script extends Container {
	private final ApplicationContext appContext;
	private final XList<Script> includes;
	private final LibraryList libraryList;
	private final ScriptOrigin origin;
	private final VenusParser parser;

	public Script(ApplicationContext appContext, ScriptOrigin origin) {
		this.appContext = appContext;
		this.context = new Context(this, null);
		this.includes = new XArrayList<>();
		this.libraryList = new LibraryList();
		this.origin = origin;
		this.parser = new VenusParser(this);
	}

	@Override
	public Function findFunction(Context context, String name, XView<Type> argumentTypes) throws ScriptRuntimeException {
		try {
			return super.findFunction(context, name, argumentTypes);
		} catch (ScriptRuntimeException exception) {
			for (Script script : getIncludes()) {
				try {
					return script.findFunction(context, name, argumentTypes);
				} catch (ScriptRuntimeException ignored) {
				}
			}

			Function function = getLibraryList().findFunction(name, argumentTypes);

			if (function != null) {
				return function;
			}

			throw exception;
		}
	}

	@Override
	public ApplicationContext getApplicationContext() {
		return appContext;
	}

	public String getDisplayName() {
		return getOrigin().getScriptName();
	}

	public XList<Script> getIncludes() {
		return includes;
	}

	public LibraryList getLibraryList() {
		return libraryList;
	}

	public ScriptOrigin getOrigin() {
		return origin;
	}

	public VenusParser getParser() {
		return parser;
	}

	@Override
	public Script getScript() {
		return this;
	}

	public void include(String includePath, boolean maybe) throws ScriptCompileException {
		ScriptOrigin origin = getOrigin().findRelative(includePath);

		if (origin != null) {
			Script script = origin.compile(getApplicationContext());

			getIncludes().add(script);
		} else if (maybe) {
			XLogger.debugln("Not found include script \"" + includePath + "\", but it was marked as maybe.");
		} else {
			throw new ScriptCompileException("Could not find script \"" + includePath + "\"");
		}
	}

	@Deprecated
	@Override
	public void setParent(Container parent) {
		throw new IllegalStateException("Cannot define a script's parent");
	}

	@Override
	public String toString() {
		return "script(name=" + getDisplayName() + ", origin=" + getOrigin() + ", includes=" + getIncludes() + ')';
	}
}
