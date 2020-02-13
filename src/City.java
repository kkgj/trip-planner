public class City {
    String cityName,state;
    double latDeg,latMin,longDeg,longMin;

    public City(String city,String state,double LD,double LM,Double LDeg,double LongMin){
        cityName = city;
        latDeg = LD;
        latMin = LM;
        longDeg = LDeg;
        longMin = LongMin;
        this.state = state;
    }
    public String getCityName(){
        return cityName;
    }
    public String getState(){
        return state;
    }
    public double getLatDeg(){
        return latDeg;
    }
    public double getLongDeg(){
        return longDeg;
    }
    public double getLatMin(){
        return latMin;
    }
    public double getLongMin(){
        return longMin;
    }
    public String toString(){
        return getCityName()+", "+getState();
    }
}
