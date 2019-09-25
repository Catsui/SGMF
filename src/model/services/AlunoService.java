package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.AlunoDao;
import model.entities.Aluno;

public class AlunoService {
	
	private AlunoDao dao = DaoFactory.createAlunoDao();
	
	public List<Aluno> findAll() {
		return dao.findAll();	
	}
	
	public List<Aluno> findByName(String nome, int length) {
		return dao.findByName(nome, length);
	}
	
	public List<Aluno> findByPresenca(Boolean presenca) {
		return dao.findByPresenca(presenca);
	}
	
	public void updatePresenca(Aluno obj) {
		dao.updatePresenca(obj);
	}
	
	public void saveOrUpdate(Aluno obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void saveByPresenca(Boolean presenca, String filepath) {
		dao.saveByPresenca(presenca, filepath);
	}
	
	public void remove(Aluno obj) {
		dao.deleteById(obj.getId());
	}

}
