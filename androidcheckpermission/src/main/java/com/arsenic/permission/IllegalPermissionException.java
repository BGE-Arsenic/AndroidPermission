package com.arsenic.permission;

/**
 * Created BGE-Arsenic apple on 2017/12/21.
 */

public class IllegalPermissionException extends RuntimeException{

    public IllegalPermissionException(){
        super();
    }

    public IllegalPermissionException(String s){
        super(s);
    }

    public IllegalPermissionException(Throwable because){
        super(because);
    }

    public IllegalPermissionException(String message,Throwable because){
        super(message,because);
    }

}
