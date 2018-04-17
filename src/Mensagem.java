import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Mensagem {
	private String mensagemId;
	private String texto;
	private LocalDate dataCriacao;
	private ArrayList<Mensagem> Respostas;
	private ArrayList<String> destinatarios; 
	
	public ArrayList<String> getDestinatarios() {
		return destinatarios;
	}
	public void setDestinatarios(ArrayList<String> destinatarios) {
		this.destinatarios = destinatarios;
	}
	public String getMensagemId() {
		return mensagemId;
	}
	public void setMensagemId(String mensagemId) {
		this.mensagemId = mensagemId;
	}
	
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public LocalDate getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	public ArrayList<String> getRespostas() {
		return Respostas;
	}
	public void setRespostas(ArrayList<String> respostas) {
		Respostas = respostas;
	}
	
}
