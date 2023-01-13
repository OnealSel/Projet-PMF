package controler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import controler.abstractions.IHandler;
import controler.abstractions.IRegulatorListener;
import model.Model;


public class RegulationTOR implements IHandler {
	
	protected float consigneTemperature = 16.0f;
	protected boolean consigneAllumage = false;
	
	protected double histoIn;
	protected Date histoDate;
	
	protected boolean alertLiquefaction = false;
	protected boolean alertTempGap = false;

	protected List<IRegulatorListener> listeners;
	protected Date lastAllumageOn = null;
	
	/**
	 * Constructeur
	 */
	public RegulationTOR() {
		this.listeners = new ArrayList<IRegulatorListener>();
	}
	
	@Override
	public void onNewStatementRead(Model data) {

		// On d�tecte la possible liqu�faction
		detectLiquefaction(data);
		
		// On d�tecte les forts �carts de temp�rature
		detectTemperatureGap(data);
		
		// On d�termine l'�tat de la consigne d'allumage
		applyConsigne(data);
		
	}
//Model model
	protected void applyConsigne(Model data) {
		// On compare � la consigne -5% pour laisser la temp�rature refroidir un peu plus
		// Il s'agit de la valeur d'hyst�r�sis
		boolean consigneAllumage = data.getInternalTemperature() > consigneTemperature * .95;
		// On v�rifie que la consigne a chang�e
		if (this.consigneAllumage != consigneAllumage) {
			// On veut allumer le frigo
			if (consigneAllumage) {
				// Si on a allum� le frigo il y a moins de 2 secondes on ne le rallume pas
				// On simule un syst�me d'�conomie d'�nergie !
				if (lastAllumageOn != null && (new Date().getTime() - lastAllumageOn.getTime()) < 2000) {
					return;
				}
				lastAllumageOn = new Date(); 
			}
			// On m�morise la nouvelle consigne
			this.consigneAllumage = consigneAllumage;
			// On propage un �v�nement
			notifyEtatAllumageChanged(consigneAllumage);
		}
	}

	protected void detectTemperatureGap(Model data) {
		// Variation sup�rieure � 1 �C en 3 secondes
		boolean isTempGap = alertTempGap;
		if (this.histoDate == null || (new Date().getTime() - this.histoDate.getTime()) >= 3000) {
			// On check la variation
			if (this.histoDate != null)
				isTempGap = (data.getInternalTemperature() - this.histoIn > 1);
			// Et on m�morise les nouvelles donn�es
			this.histoDate = new Date();
			this.histoIn = data.getInternalTemperature();
		}
		if (isTempGap != alertTempGap) {
			// On m�morise le nouvel �tat
			alertTempGap = isTempGap;
			// En cas de changement on envoie un event
			notifyAlertTemperatureGap(isTempGap);
		}
	}

	protected void detectLiquefaction(Model data) {
		// On ram�ne le taux d'humidit� entre 0 et 1
		double h = data.getHumidityPercent() / 100;
		
		// On calcule la temp�rature de ros�e
		double tempRose = Math.pow(h , 1.0/8) * (112 + (0.9 * data.getInternalTemperature())) + (0.1 * data.getExternalTemperature()) - 112;
		
		// On d�tecte la possible liqu�faction
		boolean isLiquefaction = (tempRose >= data.getInternalTemperature());
		if (isLiquefaction != alertLiquefaction) {
			// On m�morise le nouvel �tat
			alertLiquefaction = isLiquefaction;
			// En cas de changement d'�tat on envoie un event
			notifyAlarmCondensation(isLiquefaction);
		}
	}

	@Override
	public void setTempConsigne(float tempConsigne) {
		// La temp�rature de consigne a chang�
		if (tempConsigne != this.consigneTemperature) {
			// On enregistre la nouvelle consigne
			this.consigneTemperature = tempConsigne;
			// On propage un �v�nement
			notifyConsigneTemperatureChanged(tempConsigne);
		}
	}

	@Override
	public void addListener(IRegulatorListener obs) {
		this.listeners.add(obs);
	}

	@Override
	public void removeListener(IRegulatorListener obs) {
		this.listeners.remove(obs);
	}

	@Override
	public void notifyConsigneTemperatureChanged(final double tempConsigne) {
		System.out.println("[Regulation] Consigne de temp�rature : " + tempConsigne);
		this.listeners.forEach(new Consumer<IRegulatorListener>() {
			public void accept(IRegulatorListener obs) {
				obs.onConsigneTemperatureChanged(tempConsigne);
			}
		});
	}

	@Override
	public void notifyEtatAllumageChanged(final boolean powerState) {
		System.out.println("[Regulation] Consigne d'allumage : " + powerState);
		this.listeners.forEach(new Consumer<IRegulatorListener>() {
			public void accept(IRegulatorListener obs) {
				obs.onConsigneAllumageChanged(powerState);
			}
		});
	}

	@Override
	public void notifyAlarmCondensation(final boolean state) {
		System.out.println("[Regulation] Alerte de condensation : " + state);
		this.listeners.forEach(new Consumer<IRegulatorListener>() {
			public void accept(IRegulatorListener obs) {
				obs.onAlertCondensationChanged(state);
			}
		});
	}

	@Override
	public void notifyAlertTemperatureGap(final boolean state) {
		System.out.println("[Regulation] Alerte d'�cart de temp�rature : " + state);
		this.listeners.forEach(new Consumer<IRegulatorListener>() {
			@Override
			public void accept(IRegulatorListener obs) {
				obs.onAlertTemperatureGapChanged(state);
			}
		});
	}

	@Override
	public float getConsigneTemperature() {
		return consigneTemperature;
	}

	@Override
	public boolean isConsigneAllumage() {
		return consigneAllumage;
	}

	@Override
	public boolean isAlertLiquefaction() {
		return alertLiquefaction;
	}

	@Override
	public boolean isAlertTempGap() {
		return alertTempGap;
	}

	@Override
	public void onPowerStatusChanged(boolean powerOn) {
		// Non utilis�
	}
	
}
