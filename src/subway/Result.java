package subway;

import java.util.*;

public class Result {
	private Station starStation;
	private Station endStation;
	private double distance=0;
	private List<Station> passStation = new ArrayList<>();
	public Station getStarStation() {
		return starStation;
	}
	public void setStarStation(Station starStation) {
		this.starStation = starStation;
	}
	public Station getEndStation() {
		return endStation;
	}
	public void setEndStation(Station endStation) {
		this.endStation = endStation;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public List<Station> getPassStation() {
		return passStation;
	}
	public void setPassStation(List<Station> passStation) {
		this.passStation = passStation;
	}
	public Result(Station starStation,Station endStation,double distance) {
		this.starStation=starStation;
		this.endStation=endStation;
		this.distance=distance;
	}
	public Result() {
		
	}
	public String toString() {
		return "Result{"+"starStation="+starStation+", endStation="+endStation+",distance" + distance +", passStation=" + passStation+'}';
	}
}
