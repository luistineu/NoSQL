import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuario {
	private String idUsuario;
	private String nome;
	private String apelido;
	private Date dataCadastro;
	private ArrayList<String> mensagensEnviadas;
	private ArrayList<String> mensagensRecebidas;
	
	
	public String getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getApelido() {
		return apelido;
	}
	public void setApelido(String apelido) {
		this.apelido = apelido;
	}
	
	public Date getDataCadastro() {
		return dataCadastro;
	}
	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	public ArrayList<String> getMensagensEnviadas() {
		return mensagensEnviadas;
	}
	public void setMensagensEnviadas(ArrayList<String> mensagensEnviadas) {
		this.mensagensEnviadas = mensagensEnviadas;
	}
	
	public ArrayList<String> getMensagensRecebidas() {
		return mensagensRecebidas;
	}
	public void setMensagensRecebidas(ArrayList<String> mensagensRecebidas) {
		this.mensagensRecebidas = mensagensRecebidas;
	}
	
	
}
