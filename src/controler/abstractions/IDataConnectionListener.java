package controler.abstractions;

import model.Model;

//import java.beans.Statement;

public interface IDataConnectionListener {
	
	/**
	 * Quand une nouvelle donn�e est lue.
	 */
	public void onNewStatementRead(Model data);

	/**
	 * Quand l'�tat d'allumage du r�frig�rateur a chang�.
	 */
	public void onPowerStatusChanged(boolean powerOn);
	
}
