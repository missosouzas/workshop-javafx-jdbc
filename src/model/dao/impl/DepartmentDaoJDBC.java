package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn) {
			this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO department (Name) "
					+ "VALUES " + "(?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Erro Inesperado, Nenhuma linha afetada!!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE department SET Name = ? " 
					+ " WHERE Id = ? ");

			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE from department "
						+ " WHERE Id = ? ");
			st.setInt(1, id);
					
			int row = st.executeUpdate();
			if (row <= 0) {
				throw new DbException("Não Existe Chave para ser deletada!");
				
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT * FROM department WHERE department.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Department dep = new Department();
				return dep;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT department.*,department.Name as DepName from department	ORDER BY Name");

			rs = st.executeQuery();

			List<Department> list = new ArrayList<>();
				while (rs.next()) {
					Department obj = new Department();
					obj.setId(rs.getInt("Id"));
					obj.setName(rs.getString("Name"));
					list.add(obj);
				
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	

	
}
