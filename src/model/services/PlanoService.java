package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.PlanoDao;
import model.entities.Plano;

public class PlanoService {
	
	private PlanoDao dao = DaoFactory.createPlanoDao();
	
	public List<Plano> findAll() {
		return dao.findAll();	
	}
	
	public void saveOrUpdate(Plano obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void remove(Plano obj) {
		dao.deleteById(obj.getId());
	}

}
