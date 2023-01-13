package controler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import controler.abstractions.IDataConnection;
import controler.abstractions.IDataConnectionListener;
import model.Model;


/**
 * Lisseur de la T� externe.
 */
public class LisseurTempExterne implements IDataConnection, IDataConnectionListener {
	
	private IDataConnection source;
	
	private List<Double> values;

	private int length;
	
	private ArrayList<IDataConnectionListener> listeners;

	public LisseurTempExterne(IDataConnection source, int length) {
		this.source = source;
		this.length = length;
		values = new ArrayList<Double>();
		listeners = new ArrayList<IDataConnectionListener>();
	}
	
	protected double add(double value) {
		values.add(value);
		while (values.size() > length) values.remove(0);
		float sum = 0;
		for (double val : values) sum += val;
		return sum / (double)values.size();
	}
	
	protected double add_debug(double value) {
		values.add(value);
		while (values.size() > length) values.remove(0);
		double sum = 0;
		double min = Float.MAX_VALUE;
		double max = Float.MIN_VALUE;
		for (double val : values) {
			sum += val;
			if (val < min) min = val;
			if (val > max) max = val;
		}
		double avg = sum / (double)values.size();
		System.out.println("[Lissage] Added="+value+" Length="+values.size()+" Min="+min+" Max="+max+" Average="+avg);
		return avg;
	}
	/*
	@Override
	public void notifyListeners(Model data) {
		// On r��crit l'�tat avec une moyenne
		final Model newData = new Model(
				data.getHumidityPercent(),
				data.getInternalTemperature(),
				//add_debug(data.getExteriorTemperature())
				add(data.getExternalTemperature())
		);
		listeners.forEach(new Consumer<IDataConnectionListener>() {
			public void accept(IDataConnectionListener observer) {
				observer.onNewStatementRead(newData);
			}
		});
	} //*/

	@Override
	public void init() throws Throwable {
		this.source.init();
	}

	@Override
	public void start() {
		// On inscrit cet objet comme listener de la source
		this.source.addListener(this);
		// On d�marre la source
		this.source.start();
	}

	@Override
	public void stop() {
		// On se retire des observateurs
		this.source.removeListener(this);
		// Et on arr�te la source
		this.source.stop();
	}

	@Override
	public void addListener(IDataConnectionListener obs) {
		this.listeners.add(obs);
	}

	@Override
	public void removeListener(IDataConnectionListener obs) {
		this.listeners.remove(obs);
	}

	/**
	 * Notifier les observateurs qu'une nouvelle donn�e a �t� lue.
	 *
	 * @param data
	 */
	@Override
	public void notifyListeners(Model data) {
		// On r��crit l'�tat avec une moyenne
		final Model newData = new Model(
				data.getHumidityPercent(),
				data.getInternalTemperature(),
				//add_debug(data.getExteriorTemperature())
				add(data.getExternalTemperature())
		);
		listeners.forEach(new Consumer<IDataConnectionListener>() {
			public void accept(IDataConnectionListener observer) {
				observer.onNewStatementRead(newData);
			}
		});

	}

	@Override
	public void notifyListeners(final boolean powerOn) {
		listeners.forEach(new Consumer<IDataConnectionListener>() {
			public void accept(IDataConnectionListener observer) {
				observer.onPowerStatusChanged(powerOn);
			}
		});
	}

	@Override
	public void setPowerEnabled(boolean value) {
		this.source.setPowerEnabled(value);
	}

	@Override
	public boolean isPowerEnabled() {
		return this.source.isPowerEnabled();
	}

	@Override
	public long getPowerUptime() {
		return this.source.getPowerUptime();
	}

	@Override
	public void onNewStatementRead(Model data) {
		// On redirige vers nos observateurs � nous
		notifyListeners(data);
	}

	@Override
	public void onPowerStatusChanged(boolean powerOn) {
		// On redirige vers nos observateurs � nous
		notifyListeners(powerOn);
	}

}
