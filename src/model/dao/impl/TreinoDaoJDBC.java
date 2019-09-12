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
import model.dao.TreinoDao;
import model.entities.Treino;

public class TreinoDaoJDBC implements TreinoDao {
	
	private Connection conn;
	
	public TreinoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Treino treino) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT into treino "
					+ "(Nome) "
					+ "VALUES (?)",
					Statement.RETURN_GENERATED_KEYS
					);
			
			st.setString(1, treino.getNome());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					treino.setId(id);
				}
				DB.closeResultSet(rs);			
			} else {
				throw new DBException("Erro inesperado. Inserção não realizada");
			}
					
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Treino treino) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE treino "
					+ "SET Nome = ? "
					+ "WHERE Id = ?"
					);
			
			st.setString(1, treino.getNome());
			st.setInt(2, treino.getId());
					
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
			st = conn.prepareStatement(
					"DELETE from treino "
					+ "WHERE Id = ?"
					);
			
			st.setInt(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Treino findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement( 
					"SELECT * from  "
					+ "where Id = ?"
					);
			
			st.setInt(1,id);
			rs = st.executeQuery();
			while (rs.next()) {
				Treino treino = instantiateTreino(rs);
				return treino;
			}
			return null;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Treino> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
	
		try {
			st = conn.prepareStatement(
					"SELECT * from treino "
					);
			rs = st.executeQuery();
			List<Treino> list = new ArrayList<>();
			while(rs.next()) {
				Treino treino = instantiateTreino(rs);
				list.add(treino);
			}
			return list;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}
	
	private Treino instantiateTreino(ResultSet rs) throws SQLException {
		Treino treino = new Treino();
		treino.setId(rs.getInt("Id"));
		treino.setNome(rs.getString("Nome"));
		return treino;
	}

}
