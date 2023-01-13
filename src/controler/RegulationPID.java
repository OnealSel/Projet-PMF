package controler;

import model.Model;

public class RegulationPID extends RegulationTOR {
	
	protected double Kp;
	protected double Ki;
	protected double Kd;
	protected SizedStack queue;
	
	public RegulationPID(double Kp, double Ki, int Kd) {
		this(Kp, Ki, Kd, 50);
	}

	public RegulationPID(double Kp, double Ki, int Kd, int queueLength) {
		this.Kp = Kp;
		this.Ki = Ki;
		this.Kd = Kd;
		this.queue = new SizedStack(queueLength);
	}

	protected void applyConsigne(Model data) {
		System.out.println(data);
		
		double pwr = (consigneTemperature - data.getInternalTemperature()) * Kp
				+ (consigneTemperature - queue.getLast()) * Kd 
				+ (consigneTemperature - queue.getAverage()) * Ki;
		
		System.out.println(pwr);
		
//		// On compare � la consigne -5% pour laisser la temp�rature refroidir un peu plus
//		// Il s'agit de la valeur d'hyst�r�sis
//		boolean consigneAllumage = data.getInteriorTemperature() > consigneTemperature * .95;
//		// On v�rifie que la consigne a chang�e
//		if (this.consigneAllumage != consigneAllumage) {
//			// On veut allumer le frigo
//			if (consigneAllumage) {
//				// Si on a allum� le frigo il y a moins de 2 secondes on ne le rallume pas
//				// On simule un syst�me d'�conomie d'�nergie !
//				if (lastAllumageOn != null && (new Date().getTime() - lastAllumageOn.getTime()) < 2000) {
//					return;
//				}
//				lastAllumageOn = new Date(); 
//			}
//			// On m�morise la nouvelle consigne
//			this.consigneAllumage = consigneAllumage;
//			// On propage un �v�nement
//			notifyConsigneAllumageChanged(consigneAllumage);
//		}
	}

}
