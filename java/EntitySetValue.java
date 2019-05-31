
/**车辆管理类（名字起得佛性） */
@Table(name = "t_car_manage")
public class CarManage{
    //车型
    @Column(name = "car_type")
    private Integer carType;
    //车牌号
    @Column(name = "license_plate")
    private String licensePlate;

    private String carTypeEx;

    public String getCarTypeEx() {
        if (this.getCarType() != null) {
            //注意了
            carTypeEx = CarTypelEnum.fromCode( this.getCarType() ).toName();
        }
        return carTypeEx;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
    }

     public void setCarTypeEx(String carTypeEx) {
        this.carTypeEx = carTypeEx;
    }
}