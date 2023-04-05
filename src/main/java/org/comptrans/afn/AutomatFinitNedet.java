package org.comptrans.afn;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.comptrans.afn.models.AutomatFinitNedetModel;
import org.comptrans.afn.models.StateModel;
import org.json.simple.JSONObject;

public class AutomatFinitNedet {

    public AutomatFinitNedetModel lafn;
    public AutomatFinitNedet(JSONObject lafn) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.lafn = objectMapper.convertValue(lafn, AutomatFinitNedetModel.class);
    }

    public String validateStringInAlphabet(String value) {
        char[] valueAsChars = value.toCharArray();
        if (iterateAndValidate(this.lafn.lAfn[0], 0, valueAsChars)) {
            return "DA";
        } else {
            return "NU";
        }
    }

    private boolean iterateAndValidate(StateModel state, int step, char[] values) {
        boolean localValidate = false;
        if( step == values.length ) {
            return false;
        }
        for(int i = 0 ; i < state.transitions.length ; i++) {
            if (state.transitions[i].value == null) {
                localValidate = (localValidate || iterateAndValidate(this.lafn.lAfn[state.transitions[i].target], step, values));
            } else if (String.valueOf(values[step]).equals(state.transitions[i].value) && step == values.length - 1 && this.lafn.lAfn[state.transitions[i].target].isFinal) {
                return true;
            } else if (String.valueOf(values[step]).equals(state.transitions[i].value)) {
                localValidate = (localValidate || iterateAndValidate(this.lafn.lAfn[state.transitions[i].target], step + 1, values));
            }
        }
        return localValidate;
    }

}
