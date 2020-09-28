package work1.project1.package1.myenum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class EnumToLower {


    public MyEnum convert(MyEnum val){
        switch (val){
            case EMPLOYEE:
                return MyEnum.employee;
            case HOD:
                return MyEnum.hod;
            case CEO:
                return MyEnum.ceo;
            case NONE:
                return MyEnum.none;
            default:
                return val;
        }
    }
}
