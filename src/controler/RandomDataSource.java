package controler;

import controler.abstractions.AbstractDataConnection;
import model.Model;

public class RandomDataSource extends AbstractDataConnection implements Runnable {

	private Thread thread;
	
	public RandomDataSource() {
		thread = new Thread(this);
	}

	@Override
	public void init() throws Throwable {
	}
	
	@Override
	public void start() {
		thread.start();
	}
	
	@Override
	public void run() {
		
		while (!Thread.interrupted()) {
			
			// On g�n�re de fausses donn�es
			notifyListeners(new Model(
					Math.random() * 100,
					Math.random() * 30,
					Math.random() * 30
			));
			
			sleep(500);
			
		}
		
	}

	@Override
	public void setPowerEnabled(boolean powerOn) {
		if (this.powerEnabled != powerOn) {
			this.powerEnabled = powerOn;
			notifyListeners(powerOn);
		}
	}

	@Override
	public void stop() {
		thread.interrupt();
	}

}
