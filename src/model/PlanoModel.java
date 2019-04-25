package model;	

 public class PlanoModel {	

 	private String modalidade;	
	private String plano; 	
	private float valor_mensal; 	




 	public String getModalidade() {	
		return modalidade;	
	}	

 	public PlanoModel setModalidade(String modalidade) {	
		this.modalidade = modalidade;	
		return this;
	}	

 	public String getPlano() {	
		return plano;	
	}	

 	public PlanoModel setPlano(String plano) {	
		this.plano = plano;	
		return this;
	}	

 	public float getValorMensal() {	
		return valor_mensal;	
	}	

 	public PlanoModel setValorMensal(float valor_mensal) {	
		this.valor_mensal = valor_mensal;	
		return this;
	}	


 }