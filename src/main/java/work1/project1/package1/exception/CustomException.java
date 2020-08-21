package work1.project1.package1.exception;

import java.util.Date;

public class CustomException extends  Exception{


    public CustomException(){

        super("Error Occured!!");
    }
    public CustomException(String s){

        super(s);
    }


}
