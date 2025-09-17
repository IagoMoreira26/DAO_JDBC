package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection connection;

	public DepartmentDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Department obj) {

		PreparedStatement pst = null;
		try {
			pst = connection.prepareStatement("INSERT INTO department " + "(Name) " + "VALUES " + "(?)",
					Statement.RETURN_GENERATED_KEYS);

			pst.setString(1, obj.getName());

			int rowsAffected = pst.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! no rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(pst);
		}
	}

	@Override
	public void update(Department obj) {

		PreparedStatement pst = null;
		try {
			pst = connection.prepareStatement("UPDATE department " + "SET Name = ? " + "WHERE Id = ? ");

			pst.setString(1, obj.getName());
			pst.setInt(2, obj.getId());
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(pst);
		}
	}

	@Override
	public void deleteById(Integer id) {

		PreparedStatement pst = null;
		try {
			pst = connection.prepareStatement("DELETE FROM department " + "WHERE Id = ?");

			pst.setInt(1, id);

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 0) {
				throw new DbException("Unexpected error! no rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(pst);
		}
	}

	@Override
	public Department findById(Integer id) {

		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = connection.prepareStatement("SELECT * " + "FROM department " + "WHERE Id = ?");

			pst.setInt(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				Department dep = new Department();
				dep.setId(rs.getInt("Id"));
				dep.setName(rs.getString("Name"));
				return dep;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(pst);
			DB.closeResultSet(rs);
		}
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {

		Department dep = new Department();

		new Department();
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));

		return dep;
	}

	@Override
	public List<Department> findAll() {

		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = connection.prepareStatement("SELECT Id, Name " + "FROM department " + "ORDER BY Name");

			rs = pst.executeQuery();
			List<Department> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {

				Department dep = map.get(rs.getInt("Id"));
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("Id"), dep);
				}

				Department obj = instantiateDepartment(rs);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(pst);
			DB.closeResultSet(rs);
		}
	}

}
