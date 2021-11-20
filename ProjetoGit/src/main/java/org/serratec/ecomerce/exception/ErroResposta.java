package org.serratec.ecomerce.exception;

public class ErroResposta {

	private int status;
	private String titulo;
	private String dataehora;
	
	public ErroResposta() {
		
	}
	
	public ErroResposta(int status, String titulo, String dataehora) {
		super();
		this.status = status;
		this.titulo = titulo;
		this.dataehora = dataehora;
	}

	public int getStatus() {
		return status;
	}




	public void setStatus(int status) {
		this.status = status;
	}




	public String getTitulo() {
		return titulo;
	}




	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}




	public String getDataehora() {
		return dataehora;
	}




	public void setDataehora(String dataehora) {
		this.dataehora = dataehora;
	}
	
	
	
}
