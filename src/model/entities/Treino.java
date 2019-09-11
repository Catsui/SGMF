package model.entities;

import java.util.Date;
import java.util.List;

public class Treino {

	private Integer id;
	private Date dataInicio;
	private List<String> listTreinos;

	public Treino() {

	}

	public Treino(Integer id, Date dataInicio, List<String> listTreinos) {
		this.id = id;
		this.dataInicio = dataInicio;
		this.listTreinos = listTreinos;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public List<String> getListTreinos() {
		return listTreinos;
	}

	public void setListTreinos(List<String> listTreinos) {
		this.listTreinos = listTreinos;
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
		return "Treino [id=" + id + ", dataInicio=" + dataInicio + ", listTreinos=" + listTreinos + "]";
	}

}
