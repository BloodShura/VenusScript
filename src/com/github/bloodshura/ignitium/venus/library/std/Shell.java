package com.github.bloodshura.ignitium.venus.library.std;

import com.github.bloodshura.ignitium.sys.XSystem;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.Method;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodArgs;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.value.BoolValue;
import com.github.bloodshura.ignitium.venus.value.StringValue;
import com.github.bloodshura.ignitium.venus.value.Value;

import java.io.IOException;

@MethodArgs({ StringValue.class, BoolValue.class })
@MethodName("shell")
public class Shell extends Method {
	@Override
	public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		StringValue command = (StringValue) descriptor.get(0);
		BoolValue newWindow = (BoolValue) descriptor.get(1);

		try {
			XSystem.getTerminal().runInShell(command.value(), newWindow.value());

			return new BoolValue(true);
		} catch (IOException exception) {
			return new BoolValue(false);
		}
	}
}
