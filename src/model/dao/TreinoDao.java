package model.dao;

import java.util.List;

import model.entities.Treino;

public interface TreinoDao {
	
	void insert(Treino obj);
	void update(Treino obj);
	void deleteById(Integer id);
	Treino findById(Integer id);
	List<Treino> findAll();

}
