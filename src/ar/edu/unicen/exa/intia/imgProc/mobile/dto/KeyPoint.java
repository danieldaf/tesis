package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

public class KeyPoint {

	protected Long id;
	protected Estadistica estadistica;
	
	protected Double x;
	protected Double y;
	protected Double size;
	protected Double angle;
	protected Double response;
	protected Integer octave;
	protected Integer classId;
	
	public KeyPoint(Long id, Estadistica estadistica, Double x, Double y,
			Double size, Double angle, Double response, Integer octave,
			Integer classId) {
		this.id = id;
		this.estadistica = estadistica;
		this.x = x;
		this.y = y;
		this.size = size;
		this.angle = angle;
		this.response = response;
		this.octave = octave;
		this.classId = classId;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Estadistica getEstadistica() {
		return estadistica;
	}
	public void setEstadistica(Estadistica estadistica) {
		this.estadistica = estadistica;
	}
	public Double getX() {
		return x;
	}
	public void setX(Double x) {
		this.x = x;
	}
	public Double getY() {
		return y;
	}
	public void setY(Double y) {
		this.y = y;
	}
	public Double getSize() {
		return size;
	}
	public void setSize(Double size) {
		this.size = size;
	}
	public Double getAngle() {
		return angle;
	}
	public void setAngle(Double angle) {
		this.angle = angle;
	}
	public Double getResponse() {
		return response;
	}
	public void setResponse(Double response) {
		this.response = response;
	}
	public Integer getOctave() {
		return octave;
	}
	public void setOctave(Integer octave) {
		this.octave = octave;
	}
	public Integer getClassId() {
		return classId;
	}
	public void setClass_id(Integer classId) {
		this.classId = classId;
	}

}
