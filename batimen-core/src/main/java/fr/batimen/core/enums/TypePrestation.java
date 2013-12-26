package fr.batimen.core.enums;

public enum TypePrestation {

	LOL1("Lol1"), LOL2("Lol2");

	private TypePrestation(String type) {
		this.type = type;
	}

	private String type;

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return type;
	}

}
