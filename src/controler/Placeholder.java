package controler;

import java.util.Optional;

public class Placeholder<T> {
	
	private T _value;

	public Placeholder() {
		_value = null;
	}
	
	public Placeholder(T value) {
		_value = value;
	}

	public T get() {
		return _value;
	}

	public void set(T value) {
		_value = value;
	}
	
	public Optional<T> toOptional() {
		return Optional.ofNullable(_value);
	}

	public boolean isNull() {
		return _value == null;
	}
	
}
