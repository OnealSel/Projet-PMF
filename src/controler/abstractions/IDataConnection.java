package controler.abstractions;

import model.Model;

public interface IDataConnection {
	
	/**
	 * Initialise le syst�me.
	 * 
	 * @throws Exception Si le syst�me ne peut �tre intialis�.
	 */
	public void init() throws Throwable;
	
	/**
	 * D�marrer le syst�me.
	 */
	public void start();
	
	/**
	 * Arr�ter le syst�me.
	 */
	public void stop();
	
	/**
	 * Ajouter un observateur.
	 */
	public void addListener(IDataConnectionListener obs);
	
	/**
	 * Retirer un observateur.
	 */
	public void removeListener(IDataConnectionListener obs);
	
	/**
	 * Notifier les observateurs qu'une nouvelle donn�e a �t� lue.
	 */
	public void notifyListeners(Model data);
	
	/**
	 * Notifier les observateurs quand l'�tat d'allumage du r�frig�rateur est modifi�.
	 */
	public void notifyListeners(boolean powerOn);
	
	/**
	 * Activer ou d�sactiver l'alimentation �lectrique du r�frig�rateur.
	 */
	public void setPowerEnabled(boolean value);
	
	/**
	 * Renvoie TRUE si l'alimentation �lectrique est activ�e.
	 */
	public boolean isPowerEnabled();

	/**
	 * Temps d'allumage depuis le d�but.
	 */
	public long getPowerUptime();
	
}
