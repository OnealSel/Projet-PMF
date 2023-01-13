package model;


public class Model {

    private double tempInt;
    private double tempExt;
    private double Humidity;

    /**
     * @param tempInt est la valeur de la temperature interne.
     * @param tempExt est la valeur de la temperature externe.
     * @param Humidity est le pourcentage d'humidite dans le frigo.
     */
    public Model(double tempInt, double tempExt, double Humidity ) {
        this.tempInt = tempInt;
        this.tempExt = tempExt;
        this.Humidity = Humidity;
    }

    /**
     * @return the h
     */
    public double getHumidityPercent() {
        return Humidity;
    }

    /**
     * @return the ext
     */
    public double getExternalTemperature() {
        return tempExt;
    }

    /**
     * @return the inte
     */
    public double getInternalTemperature() {
        return tempInt;
    }
/*
    @Override
    public String toString() {
        return String.format("Tint=%s°C Text=%s°C H=%s%%", tempInt, tempExt, Humidity);
    }
*/
}
