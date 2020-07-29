package Work1.Project1.Package.exception;

public class CustomException extends  Exception{


    public CustomException(){
        super("Error Occured!!");
    }
    public CustomException(String s){
        super(s);
    }

}
