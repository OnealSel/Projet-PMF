package controler;

import java.util.Stack;

public class SizedStack extends Stack<Double> {
	
	private static final long serialVersionUID = 1825012013828872822L;
	
	private int maxSize;

    public SizedStack(int size) {
        super();
        this.maxSize = size;
    }

    @Override
    public Double push(Double object) {
        //If the stack is too big, remove elements until it's the right size.
        while (this.size() >= maxSize) {
            this.remove(0);
        }
        return super.push(object);
    }

	public double getLast() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getAverage() {
		// TODO Auto-generated method stub
		return 0;
	}
    
}
