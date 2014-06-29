package org.treasure.island.model;

public class Country {

	String name;
	
	String continent;
	
	double population;

	public Country(String name, String continent, double population) {
		super();
		this.name = name;
		this.continent = continent;
		this.population = population;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContinent() {
		return continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public double getPopulation() {
		return population;
	}

	public void setPopulation(double population) {
		this.population = population;
	}
	
	
}
