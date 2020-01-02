package model.entities;

import java.io.Serializable;
import java.util.Date;

public class Aluno implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nome;
	private String telefone;
	private Date dataNasc;
	private Date dataInicio;
	private Plano plano;
	private Boolean presenca;
	private Date pagamento;
	private String treino;

	public Aluno() {

	}

	public Aluno(Integer id, String nome, String telefone, Date dataNasc, Date dataInicio, Plano plano, Boolean presenca, Date pagamento, String treino) {
		this.id = id;
		this.nome = nome;
		this.telefone = telefone;
		this.dataNasc = dataNasc;
		this.dataInicio = dataInicio;
		this.plano = plano;
		this.presenca = presenca;
		this.pagamento = pagamento;
		this.treino = treino;
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

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	public Plano getPlano() {
		return plano;
	}
	
	public void setPlano(Plano plano) {
		this.plano = plano;
	}
	
	public Boolean getPresenca() {
		return presenca;
	}
	
	public void setPresenca(Boolean presenca) {
		this.presenca = presenca;
	}
	
	public Date getPagamento() {
		return pagamento;
	}
	
	public void setPagamento(Date pagamento) {
		this.pagamento = pagamento;
	}
	
	public String getTreino() {
		return treino;
	}

	public void setTreino(String treino) {
		this.treino = treino;
	}

	@Override
	public String toString() {
		return "Aluno [id=" + id + ", nome=" + nome + ", telefone=" + telefone + ", dataNasc=" + dataNasc
				+ ", dataInicio=" + dataInicio + " pagamento=" + pagamento + "]";
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
