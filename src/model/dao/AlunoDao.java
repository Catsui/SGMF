package model.dao;

import java.util.Date;
import java.util.List;

import model.entities.Aluno;

public interface AlunoDao {

	void insert(Aluno aluno, String tabelaAluno);

	void update(Aluno aluno, String tabelaAluno);
	
	void updateAtivo(Aluno obj, String tabelaAluno);

	void updatePresenca(Aluno aluno, String tabelaAluno);
	
	void updatePagamento(Aluno aluno, String tabelaAluno);

	void deleteById(Integer id, String tabelaAluno);

	Aluno findById(Integer id, String tabelaAluno, String tabelaPlano);

	List<Aluno> findAll(String tabelaAluno, String tabelaPlano);
	
	List<Aluno> findByAtivo(Boolean ativo, String tabelaAluno, String tabelaPlano);

	List<Aluno> findByName(String nome, int length, String tabelaAluno, String tabelaPlano);

	List<Aluno> findByPresenca(Boolean presenca, String tabelaAluno, String tabelaPlano);
	
	List<Aluno> findByPagamento(Date data, String tabelaAluno, String tabelaPlano);
	
	List<Aluno> findByVencimento(Date data, String tabelaAluno, String tabelaPlano);
	
	List<Aluno> findByPlano(Integer planoId, String tabelaAluno, String tabelaPlano);

	void saveByPresenca(Boolean presenca, String filepath);
	
	void backupDados();
	
	void lerBackup(String filepath);

	Integer contarAlunos(String tabelaAluno, String tabelaPlano);


}
