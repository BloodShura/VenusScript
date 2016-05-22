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

package br.shura.venus.resultor;

import br.shura.venus.exception.runtime.IncompatibleTypesException;
import br.shura.venus.exception.runtime.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.operator.BinaryOperator;
import br.shura.venus.value.Value;
import br.shura.x.util.layer.XApi;

/**
 * BinaryOperation.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 02:18
 * @since GAMMA - 0x3
 */
public class BinaryOperation implements Resultor {
  private final Resultor left;
  private final BinaryOperator operator;
  private final Resultor right;

  public BinaryOperation(BinaryOperator operator, Resultor left, Resultor right) {
    XApi.requireNonNull(left, "left");
    XApi.requireNonNull(operator, "operator");
    XApi.requireNonNull(right, "right");

    this.left = left;
    this.operator = operator;
    this.right = right;
  }

  public Resultor getLeft() {
    return left;
  }

  public BinaryOperator getOperator() {
    return operator;
  }

  public Resultor getRight() {
    return right;
  }

  @Override
  public Value resolve(Context context) throws ScriptRuntimeException {
    Value left = getLeft().resolve(context);
    Value right = getRight().resolve(context);
    Value result = getOperator().operate(context, left, right);

    if (result == null) {
      throw new IncompatibleTypesException(context, "Operator " + getOperator() + " cannot be applied with " +
        left.getType() + " and " + right.getType());
    }

    return result;
  }

  @Override
  public String toString() {
    return "operation([" + getLeft() + "] " + getOperator() + " [" + getRight() + "])";
  }
}