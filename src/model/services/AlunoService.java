package model.services;

import java.util.Date;
import java.util.List;

import model.dao.AlunoDao;
import model.dao.DaoFactory;
import model.entities.Aluno;

public class AlunoService {

	private AlunoDao dao = DaoFactory.createAlunoDao();

	public List<Aluno> findAll(String tabelaAluno, String tabelaPlano) {
		return dao.findAll(tabelaAluno, tabelaPlano);
	}

	public List<Aluno> findByAtivo(Boolean ativo, String tabelaAluno, String tabelaPlano) {
		return dao.findByAtivo(ativo, tabelaAluno, tabelaPlano);
	}
	
	public Aluno findById(Integer id, String tabelaAluno, String tabelaPlano) {
		return dao.findById(id, tabelaAluno, tabelaPlano);
	}

	public List<Aluno> findByPlano(Integer planoId, String tabelaAluno, String tabelaPlano) {
		return dao.findByPlano(planoId, tabelaAluno, tabelaPlano);
	}

	public void updateAtivo(Aluno obj, String tabelaAluno) {
		dao.updateAtivo(obj, tabelaAluno);
	}

	public List<Aluno> findByName(String nome, int length, String tabelaAluno, String tabelaPlano) {
		return dao.findByName(nome, length, tabelaAluno, tabelaPlano);
	}

	public List<Aluno> findByPresenca(Boolean presenca, String tabelaAluno, String tabelaPlano) {
		return dao.findByPresenca(presenca, tabelaAluno, tabelaPlano);
	}

	public void updatePresenca(Aluno obj, String tabela) {
		dao.updatePresenca(obj, tabela);
	}

	public List<Aluno> findByPagamento(Date data, String tabelaAluno, String tabelaPlano) {
		return dao.findByPagamento(data, tabelaAluno, tabelaPlano);
	}

	public List<Aluno> findByVencimento(Date data, String tabelaAluno, String tabelaPlano) {
		return dao.findByVencimento(data, tabelaAluno, tabelaPlano);
	}

	public void updatePagamento(Aluno obj, String tabelaAluno) {
		dao.updatePagamento(obj, tabelaAluno);
	}

	public void saveOrUpdate(Aluno obj, String tabelaAluno) {
		if (obj.getId() == null) {
			dao.insert(obj, tabelaAluno);
		} else {
			dao.update(obj, tabelaAluno);
		}
	}

	public void saveByPresenca(Boolean presenca, String filepath) {
		dao.saveByPresenca(presenca, filepath);
	}

	public void remove(Aluno obj, String tabela) {
		dao.deleteById(obj.getId(), tabela);
	}

	public void backupDados() {
		dao.backupDados();
	}

	public void lerBackup(String filepath) {
		dao.lerBackup(filepath);
	}

	public Integer contarAlunos(String tabelaAluno, String tabelaPlano) {
		return dao.contarAlunos(tabelaAluno, tabelaPlano);
	}

}
