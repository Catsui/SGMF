package model.entities;

import java.util.Date;

public class Aluno {

	private Integer id;
	private String nome;
	private String telefone;
	private Date dataNasc;
	private Integer frequencia;
	private Date dataInicio;

	public Aluno() {

	}

	public Aluno(Integer id, String nome, String telefone, Date dataNasc, Integer frequencia, Date dataInicio) {
		this.id = id;
		this.nome = nome;
		this.telefone = telefone;
		this.dataNasc = dataNasc;
		this.frequencia = frequencia;
		this.dataInicio = dataInicio;
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

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Date getDataNasc() {
		return dataNasc;
	}

	public void setDataNasc(Date dataNasc) {
		this.dataNasc = dataNasc;
	}

	public Integer getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(Integer frequencia) {
		this.frequencia = frequencia;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@Override
	public String toString() {
		return "Aluno [id=" + id + ", nome=" + nome + ", telefone=" + telefone + ", dataNasc=" + dataNasc
				+ ", frequencia=" + frequencia + ", dataInicio=" + dataInicio + "]";
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
		Aluno other = (Aluno) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
