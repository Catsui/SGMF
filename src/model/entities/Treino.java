package model.entities;

import java.util.Date;
import java.util.Map;

public class Treino {

	private Integer id;
	private String nome;
	private Date dataInicio;
	private Map<String, String> mapTreinos;

	public Treino() {

	}

	public Treino(Integer id, String nome, Date dataInicio, Map<String, String> mapTreinos) {
		this.id = id;
		this.nome = nome;
		this.dataInicio = dataInicio;
		this.mapTreinos = mapTreinos;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Map<String, String> getMapTreinos() {
		return mapTreinos;
	}

	public void setMapTreinos(Map<String, String> mapTreinos) {
		this.mapTreinos = mapTreinos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Treino other = (Treino) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Treino [id=" + id + ", nome=" + nome + ", dataInicio=" + dataInicio + ", mapTreinos=" + mapTreinos
				+ "]";
	}

}
