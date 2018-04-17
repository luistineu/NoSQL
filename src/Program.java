import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import redis.clients.jedis.Jedis;
import com.google.gson.Gson;

public class Program {

	public static void main(String[] args) {
		Db db = new Db();
		Gson parser = new Gson();
		Jedis jds = new Jedis("localhost");
		System.out.println(jds.ping());
	    Usuario dest = new Usuario();
		Usuario usr = new Usuario(); 
		Mensagem msg = new Mensagem(); 
		Mensagem msgResposta = new Mensagem(); 
		Scanner sc = new Scanner(System.in);
		Date date = new Date();
		String op = "";
		
		do {
			
			System.out.println("\nNOSQL_2017_LUIS TINEU");
			System.out.println("1 - CADASTRO DE USUARIO");
			System.out.println("2 - ENVIAR MENSAGEM");
			System.out.println("3 - CAIXA DE ENTRADA");
			System.out.println("4 - ITENS ENVIADOS");
			System.out.println("0 - SAIR");
			op = sc.nextLine();
			
			switch(op) {
			case"1":
				usr = new Usuario();
				System.out.println("\n----CADASTRO DE USUARIO----");
				System.out.println("\nINFORME O SEU NOME COMPLETO");
				usr.setNome(sc.nextLine());
				
				System.out.println("\nINFORME UM NICKNAME");
				usr.setApelido(sc.nextLine());
				usr.setDataCadastro(date);
	
				System.out.println("\nNOME: "+ usr.getNome() +
									"\nNICKNAME: "+ usr.getApelido() +
									"\nDATA CADASTRO: "+ usr.getDataCadastro().toString());
				
				if(usr.getNome().isEmpty() || usr.getApelido().isEmpty()) 
				{
					System.out.println("Dados incompletos!");
					break;
				}
				else 
				{
					jds.set("usuario:"+usr.getApelido(), parser.toJson(usr));
				}
				
				break;
				
			case"2":
				usr = new Usuario();
				dest = new Usuario();
				msg = new Mensagem();
				parser = new Gson();
				String opcao = "";
				
				ArrayList<String> destinatarios = new ArrayList<>();
				
				ArrayList<String> msgEnviadasDestinatarios = new ArrayList<>();
				ArrayList<String> msgRecebidasDestinatarios = new ArrayList<>();
				
				ArrayList<String> msgEnviadasRemetente = new ArrayList<>();
				ArrayList<String> msgRecebidasRemetente = new ArrayList<>();
				
				System.out.println("\n----ENVIAR MENSAGEM----");
				System.out.println("\nINFORME O SEU NICKNAME: ");
				usr.setApelido(sc.nextLine());
				
				if(jds.get("usuario:"+usr.getApelido()) != null 
					&& usr.getApelido() != null) 
				{
					usr = parser.fromJson(jds.get("usuario:"+usr.getApelido()), Usuario.class);
					
					
					if (usr.getMensagensEnviadas() != null) 
					{
						msgEnviadasRemetente = usr.getMensagensEnviadas();
					}
					
					if (usr.getMensagensRecebidas() != null) 
					{
						msgRecebidasRemetente = usr.getMensagensRecebidas();
					}
							
					
					do {

						String id = String.valueOf(System.currentTimeMillis());
						System.out.println("\nINFORME O NICKNAME A QUEM "
							+ "SERÁ ENTREGUE A MENSAGEM: ");
						dest.setApelido(sc.nextLine());
					
					
						if (jds.get("usuario:"+dest.getApelido()) != null
							&& usr.getApelido() != null) 
						{					
							Usuario destinatario = new Usuario();
							destinatario = parser.fromJson(jds.get("usuario:"+dest.getApelido()), Usuario.class);
							
							if(destinatario.getMensagensRecebidas() != null) 
							{
								msgRecebidasDestinatarios = destinatario.getMensagensRecebidas();
							}
							
							msgRecebidasDestinatarios.add(id);
							destinatario.setMensagensRecebidas(msgRecebidasDestinatarios);
																	
							destinatarios.add(destinatario.getApelido());
							
							jds.set("usuario:"+destinatario.getApelido(), parser.toJson(destinatario));
							
							System.out.println("DESEJA ADICIONAR OUTRO DESTINATÁRIO? (s/n)");
							opcao = sc.nextLine();
											
						
						
						if (opcao.equals("n")) 
						{
							System.out.println("DIGITE A MENSAGEM:");
							msg.setTexto(sc.nextLine());
							
							
							if(msg.getTexto().isEmpty())
							{
								System.out.println("A MENSAGEM NAO PODE SER VAZIA!");
							}
							
							else 
							{								
								msg.setMensagemId(id);
								msg.setDataCriacao(LocalDate.now());
																
								if (destinatarios != null) 
								{ 
									for (String dests : destinatarios) 
									{
										if(dests.equals(usr.getApelido())) 
										{
											msgRecebidasRemetente.add(msg.getMensagemId());
										}
									}
									
								msg.setDestinatarios(destinatarios);
								msgEnviadasRemetente.add(msg.getMensagemId());
									
								usr.setMensagensEnviadas(msgEnviadasRemetente);
								usr.setMensagensRecebidas(msgRecebidasRemetente);
									
								jds.set("mensagem:"+msg.getMensagemId(), parser.toJson(msg));
								jds.set("usuario:"+usr.getApelido(), parser.toJson(usr));
								
								System.out.println("\nMENSAGEM: "+ jds.get("mensagem:"+id));
								System.out.println("\nUSUÁRIO: "+ jds.get("usuario:"+usr.getApelido()));
								System.out.println("\nMENSAGENS ENVIADAS: ");
								
									for(String usr2 : destinatarios) 
									{
										System.out.println(jds.get("usuario:"+destinatario.getApelido()));
									}
									
								}
								
								else 
								{ 
									System.out.println("\nALGO DEU ERRADO!"); 
									break; 
								}							

							}
							
							id = null;						
						}
					}
						
					} while(!opcao.equals("n"));
				}
				break;
							
				
			case"3":
				usr = new Usuario();
				msg = new Mensagem();
				
				parser = new Gson();
				String opcaoResposta = "";
				
				ArrayList<String> destinatariosDaResposta = new ArrayList<>();
				
				ArrayList<String> msgRespostaEnviadasDestinatarios = new ArrayList<>();
				ArrayList<String> msgRespostaRecebidasDestinatarios = new ArrayList<>();
				
				ArrayList<String> msgRespostaEnviadasRemetente = new ArrayList<>();
				ArrayList<String> msgRespostaRecebidasRemetente = new ArrayList<>();

				ArrayList<Mensagem> Respostas = new ArrayList<>();
				
				System.out.println("\n----CAIXA DE ENTRADA----");
				System.out.println("\nLISTA DE MENSAGENS RECEBIDAS");
				System.out.println("\nINFORME O SEU NICKNAME: ");
				usr.setApelido(sc.nextLine());
				
				if(jds.get("usuario:"+usr.getApelido()) != null 
					&& usr.getApelido() != null) 
				{
				
				  usr = parser.fromJson(jds.get("usuario:"+usr.getApelido()), Usuario.class);
				  
			      for(String msgRecb : usr.getMensagensRecebidas()) 
			      { 
			         System.out.println(jds.get("mensagem:"+msgRecb));		         
			      } 
			      		      
			      
			      do {
			      System.out.println("\nDESEJA RESPONDER ALGUMA MENSAGEM? (s/n)");
			      opcaoResposta = sc.nextLine();
			      
			      //FAZER CODIGO AQUI !!!
			      
			      Mensagem msgResp = new Mensagem();
			      if (opcaoResposta.equals("s")) 
					{
						System.out.println("DIGITE O ID DA MENSAGEM QUE PRETENDE RESPONDER:");
						
						msg = new Mensagem();
						msg.setMensagemId(sc.nextLine());
						
						if(jds.get("mensagem:"+msg.getMensagemId()) == null)
						{
							System.out.println("NAO EXISTE MENSAGEM COM ESSE ID");
							break;
						}
						
						else 
						{	
							msg = parser.fromJson(jds.get("mensagem:"+msg.getMensagemId()), Mensagem.class);
							msgResposta =  new Mensagem();
							String idResposta = String.valueOf(System.currentTimeMillis());
							msgResposta.setMensagemId(idResposta);
							msgResposta.setDataCriacao(LocalDate.now());
							if (msg.getRespostas() != null)
							{
								ArrayList<String> msgAux = new ArrayList<>();
								msgAux = msg.getRespostas();
								msg.setRespostas(msgAux);
								
								
							}else {
								ArrayList<String> msgAux = new ArrayList<>();
								msgAux.add(idResposta);
								msg.setRespostas(msgAux);
							}
							System.out.println("DIGITE A MENSAGEM:");
							msgResposta.setTexto(sc.nextLine());
							
							if(msgResposta.getTexto().isEmpty())
							{
								System.out.println("A MENSAGEM NAO PODE SER VAZIA!");
							}
															
							if (destinatariosDaResposta != null) 
							{ 
								for (String dests : destinatariosDaResposta) 
								{
									if(dests.equals(usr.getApelido())) 
									{
										msgRespostaRecebidasRemetente.add(msgResposta.getMensagemId());
									}
								}
								
							msg.setDestinatarios(destinatariosDaResposta);
							msgRespostaEnviadasRemetente.add(msgResposta.getMensagemId());
								
							usr.setMensagensEnviadas(msgRespostaEnviadasRemetente);
							usr.setMensagensRecebidas(msgRespostaRecebidasRemetente);
								
							jds.set("mensagem:"+msgResposta.getMensagemId(), parser.toJson(msg));
							jds.set("usuario:"+usr.getApelido(), parser.toJson(usr));
							
							System.out.println("\nRESPOSTA: "+ jds.get("mensagem:"+idResposta));
							System.out.println("\nUSUÁRIO: "+ jds.get("usuario:"+usr.getApelido()));
					
							
								for(String usr2 : destinatariosDaResposta) 
								{
									System.out.println(jds.get("usuario:"+dest.getApelido()));
								}								
							}
							
							else 
							{ 
								System.out.println("\nALGO DEU ERRADO!"); 
								break;
							}
						}
					}		
			      
			      } while (!opcaoResposta.equals("n"));
				}  							
				
				else
				{
					System.out.println("\nUsuário não encontrado!");
					break;
			    }	
				
				break;
			  
			case"4":
				usr = new Usuario();
				System.out.println("\n----ITENS ENVIADOS----");
				System.out.println("\nLISTA DE MENSAGENS ENVIADAS");
				System.out.println("\nINFORME O SEU NICKNAME: ");
				usr.setApelido(sc.nextLine());
				
				if(jds.get("usuario"+usr.getApelido()) != null 
					&& usr.getApelido() != null) 
				{
					
				  usr = parser.fromJson(jds.get("usuario:"+usr.getApelido()), Usuario.class);
					  
				  for(String msgEnv : usr.getMensagensEnviadas()) 
				  { 
					  System.out.println(jds.get("mensagem:"+msgEnv));		         
				  } 
				      
				}
				
				else 
				{
					System.out.println("\nUsuário não encontrado!");
			    }	
				
				break;
				
			case"0":
				System.out.println("\nSaindo...");
				break;
			default:
				System.out.println("\nOpção Inválida!");
				break; }
			
			System.out.println("\nAperte uma tecla para continuar...");
			sc.nextLine();
			
		} while(!op.equals("0"));
	}
}

