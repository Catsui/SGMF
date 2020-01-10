package model.dao.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DBException;
import gui.util.Alerts;
import javafx.scene.control.Alert.AlertType;
import model.dao.AlunoDao;
import model.entities.Aluno;
import model.entities.Plano;

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
					"INSERT INTO aluno " + "(Ativo, Nome, DataNasc, Telefone, DataInicioTreino, PlanoId, Presenca, Treino) "
							+ "VALUES " + "(TRUE,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, aluno.getNome());
			st.setDate(2, new java.sql.Date(aluno.getDataNasc().getTime()));
			st.setString(3, aluno.getTelefone());
			if (aluno.getDataInicio() == null) {
				st.setDate(4, null);
			} else {
				st.setDate(4, new java.sql.Date(aluno.getDataInicio().getTime()));
			}
			
			st.setInt(5, aluno.getPlano().getId());
			st.setBoolean(6, false);
			st.setString(7, aluno.getTreino());

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
			st = conn.prepareStatement("UPDATE aluno "
					+ "SET Nome = ?, DataNasc = ?, Telefone = ?, DataInicioTreino = ?, PlanoId = ?, Treino = ? "
					+ "WHERE Id = ?");

			st.setString(1, aluno.getNome());
			st.setDate(2, new java.sql.Date(aluno.getDataNasc().getTime()));
			st.setString(3, aluno.getTelefone());
			if (aluno.getDataInicio() != null) {
				st.setDate(4, new java.sql.Date(aluno.getDataInicio().getTime()));
			} else {
				st.setDate(4, null);
			}
			st.setInt(5, aluno.getPlano().getId());
			st.setString(6, aluno.getTreino());
			st.setInt(7, aluno.getId());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}
	
	@Override
	public void updateAtivo(Aluno aluno) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE aluno SET Ativo = ? WHERE Id = ?");
			st.setBoolean(1,  aluno.getAtivo());
			st.setInt(2,  aluno.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void updatePresenca(Aluno aluno) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE aluno SET Presenca = ? WHERE Id = ?");
			st.setBoolean(1, aluno.getPresenca());
			st.setInt(2, aluno.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}
	
	@Override
	public void updatePagamento(Aluno aluno) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE aluno SET Pagamento = ?, Referencia = ?, Vencimento = ? WHERE Id = ?");
			st.setDate(1, new java.sql.Date(aluno.getPagamento().getTime()));
			st.setDate(2, new java.sql.Date(aluno.getReferencia().getTime()));
			st.setDate(3, new java.sql.Date(aluno.getVencimento().getTime()));
			st.setInt(4, aluno.getId());
			st.executeUpdate();
		} 
		catch (NullPointerException e) {
			Alerts.showAlert("Erro ao salvar as informações de pagamento", null, "Existem campos "
					+ "obrigatórios sem preenchimento. Verifique as datas informadas e tente novamente.", AlertType.ERROR);
		}
		catch (SQLException e) {
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
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Aluno findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT aluno.*, plano.Nome as PlanoNome " + "FROM aluno INNER JOIN plano "
					+ "ON aluno.PlanoId = plano.Id " + "WHERE aluno.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Plano plano = instantiatePlano(rs);
				Aluno aluno = instantiateAluno(rs, plano);
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
	
	@Override
	public List<Aluno> findByAtivo(Boolean ativo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT aluno.*, plano.Nome as PlanoNome "
					+ "FROM aluno INNER JOIN plano "
					+ "ON aluno.PlanoId = plano.Id "
					+ "WHERE ativo = ?");
			st.setBoolean(1,  ativo);
			rs = st.executeQuery();
			
			List<Aluno> list = new ArrayList<>();
			if (rs.next()) {
				Plano plano = instantiatePlano(rs);
				Aluno aluno = instantiateAluno(rs, plano);
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

	@Override
	public List<Aluno> findByName(String nome, int length) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT aluno.*, plano.Nome as PlanoNome " + "FROM aluno INNER JOIN plano "
					+ "ON aluno.PlanoId = plano.Id " + "WHERE UPPER(substring(aluno.Nome,1,?)) = UPPER(?) ORDER BY aluno.Nome ");
			
			st.setInt(1, length);
			st.setString(2, nome);
			rs = st.executeQuery();

			List<Aluno> list = new ArrayList<>();
			while (rs.next()) {
				Plano plano = instantiatePlano(rs);
				Aluno aluno = instantiateAluno(rs, plano);
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

	private Aluno instantiateAluno(ResultSet rs, Plano plano) throws SQLException {
		Aluno aluno = new Aluno();
		aluno.setId(rs.getInt("Id"));
		aluno.setAtivo(rs.getBoolean("Ativo"));
		aluno.setNome(rs.getString("Nome"));
		aluno.setDataNasc(rs.getDate("DataNasc"));
		aluno.setTelefone(rs.getString("Telefone"));
		aluno.setDataInicio(rs.getDate("DataInicioTreino"));
		aluno.setPlano(plano);
		aluno.setPresenca(rs.getBoolean("Presenca"));
		aluno.setPagamento(rs.getDate("Pagamento"));
		aluno.setReferencia(rs.getDate("Referencia"));
		aluno.setVencimento(rs.getDate("Vencimento"));
		aluno.setTreino(rs.getString("Treino"));
		return aluno;
	}

	private Plano instantiatePlano(ResultSet rs) throws SQLException {
		Plano plano = new Plano();
		plano.setId(rs.getInt("PlanoId"));
		plano.setNome(rs.getString("PlanoNome"));
		return plano;
	}

	@Override
	public List<Aluno> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT aluno.*, plano.Nome as PlanoNome " + "FROM aluno INNER JOIN plano "
					+ "ON aluno.PlanoId = plano.Id " + "WHERE ativo = TRUE ORDER BY SUBSTRING(aluno.Nome, 1, 1)");

			rs = st.executeQuery();

			List<Aluno> list = new ArrayList<>();
			Map<Integer, Plano> map = new HashMap<>();
			while (rs.next()) {
				Plano plano = map.get(rs.getInt("PlanoId"));
				if (plano == null) {
					plano = instantiatePlano(rs);
					map.put(rs.getInt("PlanoId"), plano);
				}
				Aluno aluno = instantiateAluno(rs, plano);
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

	@Override
	public List<Aluno> findByPresenca(Boolean presenca) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT aluno.*, plano.Nome as PlanoNome FROM aluno INNER JOIN plano "
					+ "ON aluno.PlanoId = plano.Id WHERE Presenca = ? ORDER BY aluno.Nome");
			st.setBoolean(1, presenca);
			rs = st.executeQuery();

			List<Aluno> list = new ArrayList<>();
			while (rs.next()) {
				Plano plano = instantiatePlano(rs);
				Aluno aluno = instantiateAluno(rs, plano);
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
	
	@Override
	public List<Aluno> findByPagamento(Date data) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT aluno.*, plano.Nome as PlanoNome FROM aluno INNER JOIN plano "
					+ "ON aluno.PlanoId = plano.Id WHERE Pagamento < ? ORDER BY aluno.Nome");
			if (data != null) {
				st.setDate(1, new java.sql.Date(data.getTime()));
			} else {
				st.setDate(1, null);
			}
			rs = st.executeQuery();
			
			List<Aluno> list = new ArrayList<>();
			while(rs.next()) {
				Plano plano = instantiatePlano(rs);
				Aluno aluno = instantiateAluno(rs, plano);
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
	
	@Override
	public List<Aluno> findByVencimento(Date data) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT aluno.*, plano.Nome as PlanoNome FROM aluno INNER JOIN plano "
					+ "ON aluno.PlanoId = plano.Id WHERE Vencimento < ? AND aluno.Ativo = TRUE ORDER BY aluno.Nome");
			if (data != null) {
				st.setDate(1,  new java.sql.Date(data.getTime()));
			} else {
				st.setDate(1,  null);
			}
			rs = st.executeQuery();
			
			List<Aluno> list = new ArrayList<>();
			while(rs.next()) {
				Plano plano = instantiatePlano(rs);
				Aluno aluno = instantiateAluno(rs, plano);
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
	

	@Override
	public void saveByPresenca(Boolean presenca, String filename) {
		PreparedStatement st = null;
		ResultSet rs = null;
		OutputStream os = null;
		List<Integer> presentes = new ArrayList<>();

		try {
			st = conn.prepareStatement("SELECT aluno.*, plano.Nome as PlanoNome FROM aluno INNER JOIN plano "
					+ "ON aluno.PlanoId = plano.Id WHERE Presenca = ?");
			st.setBoolean(1, presenca);
			rs = st.executeQuery();
			while (rs.next()) {
				Plano plano = instantiatePlano(rs);
				Aluno aluno = instantiateAluno(rs, plano);
				presentes.add(aluno.getId());
			}
			try {
				Path path = Paths.get(System.getProperty("user.dir")+"\\presencas");
				if (!Files.exists(path)) Files.createDirectories(path);
				os = new FileOutputStream(".\\presencas\\"+filename, true);
				os.write(("|" + String.valueOf(LocalDate.now())).getBytes());
				for (int id : presentes) {
					os.write(("," + id).getBytes());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			st = conn.prepareStatement("UPDATE aluno SET Presenca = ? WHERE Presenca = ?");
			st.setBoolean(1, false);
			st.setBoolean(2, true);
			st.executeUpdate();

		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}
	
	@Override
	public void backupDados() {
		DB.backupData();
	}
	
	@Override
	public void lerBackup(String filepath) {
		DB.lerBackup(filepath);
	}
	
	@Override
	public Integer contarAlunos() {
		PreparedStatement st = null;
		ResultSet rs = null;		
		try {
			st = conn.prepareStatement("SELECT aluno.*, plano.Nome as PlanoNome " 
										+ "FROM aluno INNER JOIN plano "
										+ "ON aluno.PlanoId = plano.Id " 
										+ "WHERE ativo = TRUE");
			rs = st.executeQuery();
			rs.last();
			return rs.getRow();
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

}
