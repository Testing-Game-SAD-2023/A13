/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.g2.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VariableValidationLogicComponent extends GenericLogicComponent {
    private final String variableToCheck;
    private final List<String> allowedValues;

    private boolean checkNull;
    private boolean checkAllowedValues;

    public VariableValidationLogicComponent(String variableToCheck) {
        this.variableToCheck = variableToCheck;
        this.allowedValues = new ArrayList<>();
        this.checkNull = false;// Di default i controlli sono disattivati
        this.checkAllowedValues = false;
    }

    public void setCheckNull() {
        this.checkNull = true;
    }

    public void setCheckAllowedValues(String... allowedValuesArray) {
        this.allowedValues.clear();  // Pulisce la lista esistente
        this.allowedValues.addAll(Arrays.asList(allowedValuesArray));
        this.checkAllowedValues = true; // Attiva il controllo
    }

    public void setCheckAllowedValues(List<String> allowedValues) {
        this.allowedValues.clear();  // Pulisce la lista esistente
        if (allowedValues != null) {
            this.allowedValues.addAll(allowedValues);
            this.checkAllowedValues = true; // Attiva il controllo
        }
    }

    @Override
    public boolean executeLogic() {
        if (checkNull && variableToCheck == null) {
            this.errorCode = "NULL_VARIABLE";
            return false;
        }
        if (checkAllowedValues && !allowedValues.contains(variableToCheck)) {
            this.errorCode = "VALUE_NOT_ALLOWED";
            return false;
        }
        return true; // La variabile rispetta i parametri
    }

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }
}
