package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.MatriculaModel;

public class MatriculaDAO extends BaseDAO {

	public ArrayList<MatriculaModel> getAllMatriculas(String alunos) throws SQLException{
		ResultSet result = null;
		if(alunos.isEmpty()) {
			result = this.select("*")
					.from("matriculas")
					.apply();			
		}else {
			result = this.select("*")
					.from("matriculas")
					.where("codigo_aluno", "in", "("+alunos+")")
					.apply();
		}

		ArrayList<MatriculaModel> matriculaList = new ArrayList<MatriculaModel>();
		while(result.next()) {
			matriculaList.add(new MatriculaModel()
					.setCodigoMatricula(result.getInt("codigo_matricula"))
					.setCodigoAluno(result.getInt("codigo_aluno"))
					.setDataMatricula(result.getDate("data_matricula"))
					.setDiaVencimento(result.getInt("dia_vencimento"))
					.setDataEncerramento(result.getDate("data_encerramento"))
					);
		}
		return matriculaList;
	}

	public MatriculaModel getOneMatricula(Integer id, boolean isIdMatricula) throws SQLException{
		ResultSet result = null;
		result = this.select("*")
				.from("matriculas")
				.where(isIdMatricula? "codigo_matricula" : "codigo_aluno", "=", id.toString())
				.apply();


		MatriculaModel matricula = new MatriculaModel();
		return result.next() ? matricula.setCodigoMatricula(result.getInt("codigo_matricula"))
				.setCodigoAluno(result.getInt("codigo_aluno"))
				.setDataMatricula(result.getDate("data_matricula"))
				.setDiaVencimento(result.getInt("dia_vencimento"))
				.setDataEncerramento(result.getDate("data_encerramento")) : null;
	}

	public int createMatricula(MatriculaModel matricula) throws SQLException{
		String fields ="codigo_aluno,  data_matricula, dia_vencimento, data_encerramento";
		ResultSet result = this.insertInto("matriculas", fields)
				.values(
						Integer.toString(matricula.getCodigoAluno())+","+
								quoteStr(matricula.getDataMatricula())+","+
								Integer.toString(matricula.getDiaVencimento())+","+
								quoteStr(matricula.getDataEncerramento())	
						)
				.returning("codigo_matricula")
				.apply();
		if(result.next()) {
			return result.getInt(1);
		}else {
			return 0;
		}
	}

	public void updateMatricula(MatriculaModel matricula) throws SQLException{
		this.update("matriculas")
		.setValue(
				" codigo_aluno = "+matricula.getCodigoAluno()+
				", data_matricula = "+quoteStr(matricula.getDataMatricula())+
				", dia_vencimento = "+matricula.getDiaVencimento()+
				", data_encerramento = "+quoteStr(matricula.getDataEncerramento())
				)
		.where("codigo_matricula", "=", Integer.toString(matricula.getCodigoMatricula()))
		.commit();
	}

	public void deleteMatricula(Integer id) throws SQLException{
		this.delete()
		.from("matriculas")
		.where("codigo_matricula", "=", id.toString())
		.commit();
	}

}
