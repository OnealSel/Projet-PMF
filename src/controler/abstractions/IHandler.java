package controler.abstractions;

public interface IHandler extends IDataConnectionListener {

	/**
	 * Ajouter un observateur.
	 */
	public void addListener(IRegulatorListener obs);
	
	/**
	 * Retirer un observateur.
	 */
	public void removeListener(IRegulatorListener obs);
	
	/**
	 * Notifier les observateurs lorsque la consigne d'allumage change.
	 */
	public void notifyEtatAllumageChanged(boolean powerState);

	/**
	 * Notifier les observateurs quand l'alerte de condensation change d'�tat.
	 */
	public void notifyAlarmCondensation(boolean state);

	/**
	 * Notifier les observateurs quand une nouvelle temp�rature de consigne a �t� donn�e.
	 */
	public void notifyConsigneTemperatureChanged(double tempConsigne);

	/**
	 * Notifier les observateurs quand l'alerte d'�cart de temp�rature change d'�tat.
	 */
	public void notifyAlertTemperatureGap(boolean state);
	
	/**
	 * Modifier la temp�rature de consigne, en �C.
	 */
	public void setTempConsigne(float tempConsigne);
	
	/**
	 * Recup�re la temp�rature de consigne, en �C.
	 */
	public float getConsigneTemperature();

	/**
	 * Renvoie TRUE si la consigne d'allumage est activ�e.
	 */
	public boolean isConsigneAllumage();

	/**
	 * Renvoie TRUE si l'alerte de d�tection de liqu�faction est activ�e. 
	 */
	public boolean isAlertLiquefaction();

	/**
	 * Renvoie TRUE si l'alerte d'�cart de temp�rature est activ�e.
	 */
	public boolean isAlertTempGap();
	
}
