package model.dao;

import java.util.Date;
import java.util.List;

import model.entities.Aluno;

public interface AlunoDao {

	void insert(Aluno aluno);

	void update(Aluno aluno);
	
	void updateAtivo(Aluno obj);

	void updatePresenca(Aluno aluno);
	
	void updatePagamento(Aluno aluno);

	void deleteById(Integer id);

	Aluno findById(Integer id);

	List<Aluno> findAll();
	
	List<Aluno> findByAtivo(Boolean ativo);

	List<Aluno> findByName(String nome, int length);

	List<Aluno> findByPresenca(Boolean presenca);
	
	List<Aluno> findByPagamento(Date data);
	
	List<Aluno> findByVencimento(Date data);

	void saveByPresenca(Boolean presenca, String filepath);
	
	void backupDados();
	
	void lerBackup(String filepath);

	

	

	

}
