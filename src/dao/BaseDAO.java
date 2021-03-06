package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import database.ConnectionFactory;

public abstract class BaseDAO {
	
	public BaseDAO() {}
	
	Connection conn;
	
	private String sql = "";
	private String whereFilter = "";
	private String group = "";
	
	protected void _set(PreparedStatement preparedStatement, int position, Object params) throws SQLException {
		if(params == null) {
			preparedStatement.setNull(position, Types.NULL);
		} else if(params instanceof String) {
			preparedStatement.setString(position, (String)params);
		}
	}
	
	protected BaseDAO delete() {
		this.setSql(getSql() + " DELETE ");
		return this;
	}
	
	protected BaseDAO setValue(String fields) {
		this.setSql(this.getSql() + " SET " + fields);
		return this;
	}
	
	protected BaseDAO update(String table) {
		this.setSql(this.getSql() + " UPDATE " + table);
		return this;
	}
	
	protected BaseDAO insertInto(String table, String statement) {
		this.setSql(this.getSql() + " INSERT INTO " + table + " (" + statement + ") ");
		return this;
	}
	
	protected BaseDAO values(String statement) {
		this.setSql(getSql() + " VALUES ( " + statement + " ) ");
		return this;
	}
	
	protected BaseDAO select(String statement) {
		this.setSql(this.getSql() + " SELECT " + statement); 
		return this;
	}
	
	protected BaseDAO from(String statement) {
		this.setSql(this.getSql() + " FROM " + statement);
		return this;
	}
	
	protected BaseDAO where(String statement, String operator, String value) {
		this.setSql(this.getSql() + " WHERE " + statement +" "+ operator +" "+ value);
		return this;
	}
	
	protected BaseDAO where() {
		this.setSql(this.getSql() + " WHERE " + getWhereFilter());
		return this;
	}
	
	protected BaseDAO filter(String statement, String operator, String value) {
		setWhereFilter(statement +" "+ operator +" "+ value);
		return this;
	}
	
	protected BaseDAO group(String group) {
		setGroup(group);
		return this;
	}
	
	protected BaseDAO orderBy(String statement) {
		this.setSql(this.getSql() + " ORDER BY " + statement);
		return this;
	}
	
	protected BaseDAO join(String statement) {
		this.setSql(this.getSql() + " JOIN " + statement);
		return this;
	}
	
	protected BaseDAO innerJoin(String statement) {
		this.setSql(this.getSql() + "INNER JOIN " + statement);
		return this;
	}
	
	protected BaseDAO leftJoin(String statement) {
		this.setSql(this.getSql() + "LEFT JOIN " + statement);
		return this;
	}
	
	protected BaseDAO rightJoin(String statement) {
		this.setSql(this.getSql() + "RIGHT JOIN " + statement);
		return this;
	}
	
	protected BaseDAO groupBy() {
		setSql(getSql() + " GROUP BY " + getGroup());
		return this;
	}
	
	protected BaseDAO returning(String statement) {
		setSql(getSql() + " RETURNING "+ statement);
		return this;
	}
	
	private String getGroup() {
		return group;
	}
	
	private void setGroup(String group) {
		this.group = getGroup() + ((getGroup().isEmpty()) ? "" : ", ")  +  group; 
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
	protected BaseDAO customSql(String sql) {
		this.sql = sql;
		return this;
	}
	
	private String getWhereFilter() {
		return whereFilter;
	}
	
	private void setWhereFilter(String sql) {
		whereFilter = ((whereFilter.isEmpty()) ? " " : whereFilter+" AND " ) + sql;		
	}
	
	public ResultSet freeSqlQuery(String sql) throws SQLException {
		this.sql = sql;
		
		return excecuteQuery();
	}
	
	protected ResultSet apply() throws SQLException {
		ResultSet result = this.excecuteQuery();
		this.conn.close();
		return result;
	}
	
	protected Boolean commit() throws SQLException {
		try {
			this.excecuteUpdate();
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		conn.close();
		return false;
		
	}
	
	protected ResultSet excecuteQuery() throws SQLException {
		PreparedStatement prepStatement = null;
		
		this.connection();
		
		prepStatement = conn.prepareStatement(this.getSql());
		clean();
		return prepStatement.executeQuery();
	}
	
	protected int excecuteUpdate() throws SQLException {
		PreparedStatement prepStatement = null;
		
		this.connection();
		
		prepStatement = conn.prepareStatement(this.getSql());
		clean();
		return prepStatement.executeUpdate();
	}
	
	public void clean() {
		sql = "";
		whereFilter = "";
		group = "";
	}
	
	protected void connection() {
		conn = ConnectionFactory.getConnection
				(
					"master", 
					"admin", 
					"admin"
				);
		try {
			conn.setAutoCommit(true);
			System.out.println("Conectado com sucesso!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Util
	public String quoteStr(String str) {
		return "'"+str+"'";
	}

}
