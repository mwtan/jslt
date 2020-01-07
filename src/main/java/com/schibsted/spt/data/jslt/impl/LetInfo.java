
// Copyright 2018 Schibsted Marketplaces Products & Technology As
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.schibsted.spt.data.jslt.impl;

public class LetInfo extends VariableInfo {
  private LetExpression let;

  public LetInfo(LetExpression let) {
    super(let.getLocation());
    this.let = let;
  }

  public String getName() {
    return let.getVariable();
  }

  public boolean isLet() {
    return false;
  }

  public ExpressionNode getDeclaration() {
    return let.getDeclaration();
  }
}
