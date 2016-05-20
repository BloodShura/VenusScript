//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2016, João Vitor Verona Biazibetti - All Rights Reserved                /
//                                                                                       /
// Licensed under the GNU General Public License v3;                                     /
// you may not use this file except in compliance with the License.                      /
//                                                                                       /
// You may obtain a copy of the License at                                               /
//     http://www.gnu.org/licenses/gpl.html                                              /
//                                                                                       /
// Unless required by applicable law or agreed to in writing, software                   /
// distributed under the License is distributed on an "AS IS" BASIS,                     /
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.              /
// See the License for the specific language governing permissions and                   /
// limitations under the License.                                                        /
//                                                                                       /
// Written by João Vitor Verona Biazibetti <joaaoverona@gmail.com>, March 2016           /
// https://www.github.com/BloodShura                                                     /
//////////////////////////////////////////////////////////////////////////////////////////

package br.shura.venus.library.crypto;

import br.shura.crypto.IDecrypter;
import br.shura.crypto.exception.CryptoException;
import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.function.Function;
import br.shura.venus.function.FunctionCallDescriptor;
import br.shura.venus.value.BoolValue;
import br.shura.venus.value.StringValue;
import br.shura.venus.value.Value;
import br.shura.venus.value.ValueType;
import br.shura.venus.value.VariableRefValue;
import br.shura.x.collection.view.ArrayView;
import br.shura.x.collection.view.View;

/**
 * DecryptFunction.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 17/05/16 - 13:01
 * @since GAMMA - 0x3
 */
public class DecryptFunction implements Function {
  private final View<ValueType> argumentTypes;
  private final IDecrypter decrypter;
  private final String name;

  public DecryptFunction(String name, IDecrypter decrypter) {
    this.argumentTypes = new ArrayView<>(ValueType.STRING, ValueType.VARIABLE_REFERENCE);
    this.decrypter = decrypter;
    this.name = name;
  }

  @Override
  public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    StringValue value = (StringValue) descriptor.get(0);
    VariableRefValue reference = (VariableRefValue) descriptor.get(1);

    try {
      String result = getDecrypter().decryptToStr(value.value());

      context.setVar(reference.value().getName(), new StringValue(result));

      return new BoolValue(true);
    }
    catch (CryptoException exception) {
      return new BoolValue(false);
    }
  }

  @Override
  public View<ValueType> getArgumentTypes() {
    return argumentTypes;
  }

  public IDecrypter getDecrypter() {
    return decrypter;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isVarArgs() {
    return false;
  }
}