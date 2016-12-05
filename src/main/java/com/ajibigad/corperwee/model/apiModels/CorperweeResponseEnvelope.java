package com.ajibigad.corperwee.model.apiModels;

/**
 * Created by Julius on 02/04/2016.
 */
public class CorperweeResponseEnvelope {

    private Object data;

    public CorperweeResponseEnvelope(){

    }

    public CorperweeResponseEnvelope(Object data){
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
