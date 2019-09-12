package model.dao;

import db.DB;
import model.dao.impl.AlunoDaoJDBC;
import model.dao.impl.TreinoDaoJDBC;

public class DaoFactory {
	
	public static AlunoDao createAlunoDao() {
		return new AlunoDaoJDBC(DB.getConnection());
	}
	
	public static TreinoDao createTreinoDao() {
		return new TreinoDaoJDBC(DB.getConnection());
	}

}
