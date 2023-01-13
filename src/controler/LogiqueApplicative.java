package controler;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import controler.abstractions.IDataConnection;
import controler.abstractions.IDataConnectionListener;
import controler.abstractions.IHandler;
import controler.abstractions.IRegulatorListener;
import model.Model;
//import vue.FullScreenEffect;
import vue.Windowsmonitor;
//import vue.WindowsV;


public class LogiqueApplicative implements IDataConnectionListener, IRegulatorListener {
	
	// Puissance du r�frig�rateur en Watt
	public static long PUISSANCE_FRIGO = 60;
	
	// Prix EDF en � au kWh au 21/11/15
	private static final double TARIF_KWH = 0.14040d;
	
	// Composants
	private Windowsmonitor view;
	private IDataConnection datalink;
	private IHandler regulator;

	/**
	 * D�marrer toute la logique applicative.
	 * @throws Throwable 
	 */
	public void start(Windowsmonitor view, final IDataConnection datalink, final IHandler regulator) throws Throwable {
		
		// On conserve les r�f�rences
		this.view = view;
		this.datalink = datalink;
		this.regulator = regulator;
		
		// Valeur initiales de l'IHM
		this.view.ConsigneTempLabel.setText(String.format("%.1f �C", regulator.getConsigneTemperature()));
		this.view.alertCondensation.setVisible(false);
		this.view.alertTempGap.setVisible(false);
		this.view.chart.limit.setValue(regulator.getConsigneTemperature());

		// On bind les comportements des boutons de r�gulation
		view.btnConsignePlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				LogiqueApplicative.this.regulator.setTempConsigne(regulator.getConsigneTemperature() + 0.5f);
			}
		});
		view.btnConsigneMoins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionListener) {
				LogiqueApplicative.this.regulator.setTempConsigne(regulator.getConsigneTemperature() - 0.5f);
			}
		});
		
		// On ajoute le comportement de fullscreen
		//view.btnFullscreen.addActionListener(new FullScreenEffect(view));

		// On ferme l'application proprement quand la fen�tre se ferme
		view.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				// On ferme la connexion aux donn�es
				datalink.stop();
			}
		});
		
		// Le r�gulateur a besoin d'une connexion � la donn�e pour travailler
		this.datalink.addListener(regulator);

		// Pour mettre � jour l'IHM
		this.datalink.addListener(this);
		this.regulator.addListener(this);
		
		// On affiche la fen�tre
		this.view.setVisible(true);
		
		// Et enfin on lance la source de donn�es !
		//this.datalink.init(); // D�j� fait
		this.datalink.start();
		
	}

	/**
	 * On met � jour l'IHM quand de nouvelles donn�es arrivent.
	 */
	@Override
	public void onNewStatementRead(final Model data) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Update des labels
				view.labelTempInt.setText(String.format("%.1f �C", data.getInternalTemperature()));
				view.labelTempExt.setText(String.format("%.1f �C", data.getExternalTemperature()));
				view.labelHumitidy.setText(String.format("%.1f", data.getHumidityPercent()) + "%");
				// On ajoute la donn�e au chart
				view.chart.addData((float)data.getInternalTemperature(), (float)data.getExternalTemperature());
				// On ajoute de la conso quand le frigo est allum�
				double Wh = datalink.getPowerUptime() / 3600d * PUISSANCE_FRIGO;
				double kWh = Wh / 1000d;
				double prix = kWh * TARIF_KWH;
				String conso;
				if (Wh > 1000) {
					conso = String.format("Consommation : %.2f kWh (%.4f \u20AC)", kWh, prix);
				}
				else {
					conso = String.format("Consommation : %.0f Wh (%.4f \u20AC)", Wh, prix);
				}
				view.labelConsoWatt.setText(conso);
			}
		});
	}
	
	/**
	 * On met � jour l'IHM quand la consigne de temp�rature change.
	 */
	@Override
	public void onConsigneTemperatureChanged(final double temp) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				view.labelConsigneTemp.setText(String.format("%.1f �C", temp));
				LogiqueApplicative.this.view.chart.limit.setValue(temp);
			}
		});
	}
	
	/**
	 * Quand la consigne change, on l'envoi � la source de donn�es.
	 */
	@Override
	public void onConsigneAllumageChanged(boolean enabled) {
		// On contr�le l'allumage ou l'extinction du syst�me
		this.datalink.setPowerEnabled(enabled);
	}

	/**
	 * On met � jour l'IHM quand l'alerte de condensation change d'�tat.
	 */
	@Override
	public void onAlertCondensationChanged(final boolean state) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				view.alertCondensation.setVisible(state);
			}
		});
	}

	/**
	 * On met � jour l'IHM quand l'alerte d'�cart de temp�rature change d'�tat.
	 */
	@Override
	public void onAlertTemperatureGapChanged(final boolean state) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				view.alertTempGap.setVisible(state);
			}
		});
	}

	/**
	 * On met � jour l'IHM quand l'�tat d'allumage du frigo est confirm� par la source.
	 */
	@Override
	public void onPowerStatusChanged(final boolean powerOn) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// On met � jour la vue
				view.labelConsignePower.setText(String.format("",powerOn ? "ON " : "OFF"));
				view.labelConsignePower.setIcon(powerOn ? Windowsmonitor.ICON_YES : Windowsmonitor.ICON_NO);
			}
		});
	}

}
