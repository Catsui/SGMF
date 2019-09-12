package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DBException;
import model.dao.AlunoDao;
import model.entities.Aluno;

public class AlunoDaoJDBC implements AlunoDao {

	private Connection conn;
	
	public AlunoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Aluno aluno) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO aluno "
					+ "(Nome, DataNasc, Telefone, DataInicioTreino, Treino) "
					+ "VALUES "
					+ "(?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS
					);
					
			st.setString(1,aluno.getNome());
			st.setDate(2,new java.sql.Date(aluno.getDataNasc().getTime()));
			st.setString(3, aluno.getTelefone());
			st.setDate(4, new java.sql.Date(aluno.getDataInicio().getTime()));
			st.setString(5, aluno.getTreino());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					aluno.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DBException("Erro inesperado: Nenhuma linha foi afetada.");
			}
		
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Aluno aluno) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE aluno "
					+ "SET Nome = ?, DataNasc = ?, Telefone = ?, DataInicioTreino = ?, Treino = ? "
					+ "WHERE Id = ?");
			
			st.setString(1, aluno.getNome());
			st.setDate(2, new java.sql.Date(aluno.getDataNasc().getTime()));
			st.setString(3, aluno.getTelefone());
			st.setDate(4, new java.sql.Date(aluno.getDataInicio().getTime()));
			st.setString(5, aluno.getTreino());
			
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE FROM aluno WHERE Id = ?");		
			st.setInt(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Aluno findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT aluno.* WHERE Id = ?");
					
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Aluno aluno = instantiateAluno(rs);
				return aluno;
			} 
			return null;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}					
	}

	private Aluno instantiateAluno(ResultSet rs) throws SQLException {
		Aluno aluno = new Aluno();
		aluno.setId(rs.getInt("Id"));
		aluno.setNome(rs.getString("Nome"));
		aluno.setDataNasc(rs.getDate("DataNasc"));
		aluno.setTelefone(rs.getString("Telefone"));
		aluno.setDataInicio(rs.getDate("DataInicio"));	
		aluno.setTreino(rs.getString("Treino"));
		return aluno;
	}

	@Override
	public List<Aluno> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st=conn.prepareStatement("SELECT aluno.* ORDER by Id");
			
			rs = st.executeQuery();
			
			List<Aluno> list = new ArrayList<>();
			while(rs.next()) {
				Aluno aluno = instantiateAluno(rs);
				list.add(aluno);
			}
			return list;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
}
