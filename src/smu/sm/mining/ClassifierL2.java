package smu.sm.mining;

import smu.sm.global.ClassifierType;

public abstract class ClassifierL2 {
	private ClassifierType type;

	protected ClassifierL2(ClassifierType type) {
		this.setType(type);
	}

	public abstract void train();

	public abstract void test();

	public ClassifierType getType() {
		return type;
	}

	public void setType(ClassifierType type) {
		this.type = type;
	}

}
