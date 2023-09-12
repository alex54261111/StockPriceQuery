package java.org.example;

public class DoCovnerToIntAndDouble {
    private String doubleRaw;
    private String intRaw;
    private double StrToDouble;
    private Integer StrToInt;
    public DoCovnerToIntAndDouble(){}
    public double DoStrToDouble(){
        StrToDouble = Double.parseDouble(doubleRaw);
        System.out.println("stringToDouble ok");
        return StrToDouble;
    }
    public int DoStrToInt()
    {
        StrToInt = Integer.valueOf(intRaw);
        System.out.println("stringToInt ok");
        return StrToInt.intValue();
    }
    public void setDoubleRaw(String doubleRaw) {
        this.doubleRaw = doubleRaw;
    }
    public void setIntRaw(String intRaw) {
        this.intRaw = intRaw;
    }

    public double getStrToDouble(String doubleValue) {
        StrToDouble = Double.parseDouble(doubleValue);
        return StrToDouble;
    }

    public Integer getStrToInt(String intValue) {
        StrToInt= Integer.valueOf(intValue);
        return StrToInt;
    }
}
