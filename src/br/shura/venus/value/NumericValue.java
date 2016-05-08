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

package br.shura.venus.value;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * NumericValue.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 08/05/16 - 18:41
 * @since GAMMA - 0x3
 */
public class NumericValue implements Value {
  private final BigDecimal value;

  public NumericValue(BigDecimal value) {
    this.value = value;
  }

  @Override
  public BoolValue and(Value value) {
    return null;
  }

  @Override
  public NumericValue divide(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new NumericValue(numeric.value().divide(value(), RoundingMode.FLOOR));
    }

    return null;
  }

  @Override
  public BoolValue equals(Value value) {
    return new BoolValue(value instanceof NumericValue && ((NumericValue) value).value().equals(value()));
  }

  @Override
  public NumericValue minus(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new NumericValue(numeric.value().subtract(value()));
    }

    return null;
  }

  @Override
  public NumericValue multiply(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new NumericValue(numeric.value().multiply(value()));
    }

    return null;
  }

  @Override
  public BoolValue or(Value value) {
    return null;
  }

  @Override
  public NumericValue plus(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new NumericValue(numeric.value().add(value()));
    }

    return null;
  }

  public BigDecimal value() {
    return value;
  }
}