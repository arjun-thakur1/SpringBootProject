package Work1.Project1.Package.exception;

public class NotFoundException extends  Exception{
    public NotFoundException(){
        super("Error Occured!!");
    }
    public NotFoundException(String s){
        super(s);
    }

}
