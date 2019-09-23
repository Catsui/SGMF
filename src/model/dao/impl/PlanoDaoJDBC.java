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
import db.DBIntegrityException;
import model.dao.PlanoDao;
import model.entities.Plano;

public class PlanoDaoJDBC implements PlanoDao {
	
	private Connection conn;
	
	public PlanoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Plano plano) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT into plano "
					+ "(Nome, Mensalidade) "
					+ "VALUES (?,?)",
					Statement.RETURN_GENERATED_KEYS
					);
			
			st.setString(1, plano.getNome());
			st.setDouble(2,  plano.getMensalidade());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					plano.setId(id);
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
	public void update(Plano plano) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE plano "
					+ "SET Nome = ?, Mensalidade = ? "
					+ "WHERE Id = ?"
					);
			
			st.setString(1, plano.getNome());
			st.setDouble(1, plano.getMensalidade());
			st.setInt(2, plano.getId());
					
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
					"DELETE from plano "
					+ "WHERE Id = ?"
					);
			
			st.setInt(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DBIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Plano findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement( 
					"SELECT * from plano "
					+ "where Id = ?"
					);
			
			st.setInt(1,id);
			rs = st.executeQuery();
			while (rs.next()) {
				Plano plano = instantiatePlano(rs);
				return plano;
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
	public List<Plano> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
	
		try {
			st = conn.prepareStatement(
					"SELECT * from plano "
					);
			rs = st.executeQuery();
			List<Plano> list = new ArrayList<>();
			while(rs.next()) {
				Plano plano = instantiatePlano(rs);
				list.add(plano);
			}
			return list;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}
	
	private Plano instantiatePlano(ResultSet rs) throws SQLException {
		Plano plano = new Plano();
		plano.setId(rs.getInt("Id"));
		plano.setNome(rs.getString("Nome"));
		plano.setMensalidade(rs.getDouble("Mensalidade"));
		return plano;
	}

}
