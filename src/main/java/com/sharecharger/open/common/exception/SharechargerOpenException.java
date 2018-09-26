package com.sharecharger.open.common.exception;

import com.sharecharger.open.common.entity.R;

public class SharechargerOpenException extends RuntimeException {

    /** serialVersionUID*/
    private static final long serialVersionUID = -5212079010855161498L;

    public R result;

    public SharechargerOpenException(R result){
        this.result=result;
    }

    public R getResult() {
        return result;
    }
}
