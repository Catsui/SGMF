package model.services;

import java.util.Date;
import java.util.List;

import model.dao.AlunoDao;
import model.dao.DaoFactory;
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
	
	public List<Aluno> findByVencimento(Date data) {
		return dao.findByVencimento(data);
	}
	
	public void updateVencimento(Aluno obj) {
		dao.updateVencimento(obj);
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

	public void backupDados() {
		dao.backupDados();
	}
	
	public void lerBackup(String filepath) {
		dao.lerBackup(filepath);
	}


}
