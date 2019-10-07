package model.dao;

import java.util.List;

import model.entities.Aluno;

public interface AlunoDao {

	void insert(Aluno aluno);

	void update(Aluno aluno);

	void updatePresenca(Aluno aluno);

	void deleteById(Integer id);

	Aluno findById(Integer id);

	List<Aluno> findAll();

	List<Aluno> findByName(String nome, int length);

	List<Aluno> findByPresenca(Boolean presenca);

	void saveByPresenca(Boolean presenca, String filepath);
	
	void backupDados(String filepath);

}
