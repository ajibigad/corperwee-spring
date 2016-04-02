package com.ajibigad.corperwee.model.apiModels;

/**
 * Created by Julius on 02/04/2016.
 */
public class CorperweeResponseEnvelope {

    private Object data;
    private boolean success;

    public CorperweeResponseEnvelope(){

    }

    public CorperweeResponseEnvelope(boolean success, Object data){
        this.success = success;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
