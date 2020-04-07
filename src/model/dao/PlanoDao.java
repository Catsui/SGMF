package model.dao;

import java.util.List;

import model.entities.Plano;

public interface PlanoDao {

	void insert(Plano obj, String tabelaPlano);

	void update(Plano obj, String tabelaPlano);

	void deleteById(Integer id, String tabelaPlano);

	Plano findById(Integer id, String tabelaPlano);

	List<Plano> findAll(String tabelaPlano);
}
