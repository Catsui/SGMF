package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.PlanoDao;
import model.entities.Plano;

public class PlanoService {
	
	private PlanoDao dao = DaoFactory.createPlanoDao();
	
	public List<Plano> findAll(String tabelaPlano) {
		return dao.findAll(tabelaPlano);	
	}
	
	public void saveOrUpdate(Plano obj, String tabelaPlano) {
		if (obj.getId() == null) {
			dao.insert(obj, tabelaPlano);
		} else {
			dao.update(obj, tabelaPlano);
		}
	}
	
	public Plano findById(Integer id, String tabelaPlano) {
		return dao.findById(id, tabelaPlano);
	}
	
	public void remove(Plano obj, String tabelaPlano) {
		dao.deleteById(obj.getId(), tabelaPlano);
	}

}
