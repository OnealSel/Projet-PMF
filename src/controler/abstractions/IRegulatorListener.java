package controler.abstractions;

public interface IRegulatorListener {

	/**
	 * Quand la temp�rature de consigne a chang�. Temp�rature en �C.
	 */
	public void onConsigneTemperatureChanged(double temp);
	
	/**
	 * Quand le r�gulateur change sa consigne d'allumage.
	 */
	public void onConsigneAllumageChanged(boolean enabled);

	/**
	 * Quand l'alerte de condensation change d'�tat.
	 */
	public void onAlertCondensationChanged(boolean state);

	/**
	 * Quand l'alerte d'�cart de temp�rature change d'�tat.
	 */
	public void onAlertTemperatureGapChanged(boolean state);
	
}
