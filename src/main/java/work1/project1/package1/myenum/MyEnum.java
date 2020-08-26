package work1.project1.package1.myenum;

public enum MyEnum {
    CEO, ceo,
    HOD, hod,
    EMPLOYEE, employee,
    NONE, none;

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
