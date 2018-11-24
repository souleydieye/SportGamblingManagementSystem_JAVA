package fr.uv1.bettingServices.manager;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import fr.uv1.bettingServices.exceptions.AuthenticationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingSubscriberException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.player.*;
import fr.uv1.bettingServices.competition.Competition;
import fr.uv1.bettingServices.db.DBConnection;


public class PlayersManager {
	
	private final static String VERIFYUSERNAME = "[A-Za-z0-9]{4,}";

	public static void AuthenticateUsername(String username,String pwdSubs) throws AuthenticationException{
		//On vérifie que que l'utilisateur existe
		try {
			if (findPlayerByUserName(username)==null){throw new AuthenticationException("le joueur n'existe pas");}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// On vérifie que le couple ( username,pwdSubs) est bon
		try {
			if(!(findPlayerByUserName(username).getPwdPlayer()==pwdSubs)){
				throw new AuthenticationException("le mot de pass saisi est incorrect");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static String subscribe(String lastName, String firstName, String username, String borndate) throws BadParametersException,SQLException {
		
	
		if(!hasValidUserName(username)){
			throw new BadParametersException("UserName invalid");
		}
		//si userName est valide
		// si le username n'existe pas on va l'ajouter
		/* on va creer une nouvelle connection à la base de donnée */
		Connection connection1 = DBConnection.newConnection();
		connection1.setAutoCommit(false);
		try {

			/* avec cette preparedStatement on va inserer un Player à la base de données
			 *  initialement sans mot de passe et sans nombre de jetons
			 */
			PreparedStatement preparedStatement1 = connection1.prepareStatement("INSERT INTO player (LASTNAME, FIRSTNAME, "
					+ "USERNAME,"
					+ "PWD, BORNDATE, TOKENS) "
					+ "values (?, ?, ? , ? , ? , ?)");

			/*manque le problème que le username de player doit etre unique je vais vérifier ça après maintenant je fais la structure  :p */ 
			preparedStatement1.setString(1,lastName);
			preparedStatement1.setString(2, firstName);
			preparedStatement1.setString(3, username);
			preparedStatement1.setString(4, "0000");
			preparedStatement1.setDate(5, Date.valueOf(borndate));
			// c'est le nombre de jetons par defaut 0
			preparedStatement1.setLong(6, 0);	
			
			/* faire de la mise à jour */
			preparedStatement1.executeUpdate();
			/* on va executer la prepared statement */
			preparedStatement1.close();
			
			//TODO : look how the creation of sequence is done
		//	PreparedStatement psIdValue = connection1
			//.prepareStatement("select currval('idplayer_seq') as idplayer");
			
			//ResultSet resultSet = psIdValue.executeQuery();
          //  resultSet.next();
			//int id =  Integer.parseInt(resultSet.getString("idplayer"));
		
			connection1.commit();
			/*on va valider avec commit les executions faites*/
			//the auto-incremented iD will be settled in person object
			/* on va fermer la preparedSTatement*/
			
		} catch (SQLException e1) {
			try {

				connection1.rollback();
				/*on va annuler les executions faites si un problème arrive avec rollback*/

			} catch (SQLException e2) {
				e1.printStackTrace();
				/*pour afficher les details de l'erreur pour le diagnostic pour mentionner ce qui arrivé
				 *  et dans quel bout de code il y a un problème */
			}
		}
		connection1.close();
		/*on ferme la connection */
		/*on retourne le player pour une future utilisaiton*/
		return username;
	}

	public static Player findPlayerByUserName(String username) throws SQLException {
		/* pareil on ouvre une nouvelle connection à la base */
		Connection connection1 = DBConnection.newConnection();
		/* on prepare une prepared statemnet */
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("SELECT * FROM player WHERE USERNAME LIKE ?");
		preparedStatement1.setString(1, username);
		/* on va chercher dans la base tous les players avec username passé 
		 * en parametre mais vu qu'il y a un unique on obtiendra au plus un player s'il existe */
		ResultSet resultSet1 = preparedStatement1.executeQuery();
		/* on va mettre le resultat de la requete dans resultSet1*/
		Player playerRecherchee = null;
		/* on va maintenant chercher dans le resultat s'il y a player avec un username pareil */

		while (resultSet1.next()) {
			/*remarque tres importante pour ce qui suit:
			 * vu qu'on a un seul player au plus ( le username est unique) on va 
			 * trouver dans le resultSet au plus un element qui verifie ça
			 * et donc si il y a un resultSet.next c'est que il y a un element qui est trouvé 
			 * on va donc instancier directement un player et on sort de la boucle */
			try {
				playerRecherchee = new Player(
						resultSet1.getString("LASTNAME"),
						resultSet1.getString("FIRSTNAME"),
						resultSet1.getString("USERNAME"),
						resultSet1.getDate("BORNDATE"),
						resultSet1.getLong("TOKENS")
						);
			} catch (BadParametersException e) {
				// TODO Auto-generated catch block
				System.out.println("erreur est survenue pour trouver le player correspandant, veuillez reessayer");
				e.printStackTrace();
			}
		}

		resultSet1.close();
		preparedStatement1.close();
		connection1.close();

		return playerRecherchee;
	}
	
	public static Player findPlayerById(int id) throws SQLException {
		
		Connection connection1 = DBConnection.newConnection();
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("SELECT * FROM player WHERE id LIKE ?");
		preparedStatement1.setInt(1, id);
		ResultSet resultSet1 = preparedStatement1.executeQuery();
		Player playerRecherchee = null;
		
		while (resultSet1.next()) {
			try {
				playerRecherchee = new Player(
						resultSet1.getString("LASTNAME"),
						resultSet1.getString("FIRSTNAME"),
						resultSet1.getString("USERNAME"),
						resultSet1.getDate("BORNDATE"),
						resultSet1.getLong("TOKENS")
						);
			} catch (BadParametersException e) {
				// TODO Auto-generated catch block
				System.out.println("erreur est survenue pour trouver le player correspandant, veuillez reessayer");
				e.printStackTrace();
			}
		}

		resultSet1.close();
		preparedStatement1.close();
		connection1.close();

		return playerRecherchee;
	}

	public static long deletePlayerById(int id) throws SQLException {

		Connection connection1 = DBConnection.newConnection();
		try {
			PreparedStatement psDelete = connection1
					.prepareStatement("DELETE FROM Player WHERE id LIKE ?");
		
			psDelete.setInt(1,id);
			psDelete.executeUpdate();
			
			System.out.println("personne supprimé");
			psDelete.close();
			connection1.close();
			return id; 
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("erreur parvenue ");
			return -1;
		}
		
	}
	
    public static long creditSubscriberById(int id, long numberTokens) throws BadParametersException{
    	
    	Player p=null;
		try {
			p = findPlayerById(id);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	if (numberTokens == 0L) {
			throw new BadParametersException("");
		}
    	if (numberTokens>0){
    		try {
				if(p!=null){
		    		p.setNbTokens(p.getNbTokens()+numberTokens);
					update(p);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		return p.getNbTokens();
    	}
    	else {
    		return -1;
    	}
    
    }
    
    public static void creditSubscriberByUserName(String username, long numberTokens)
			throws AuthenticationException, ExistingSubscriberException,
			BadParametersException, SQLException{
    	
 
		
		Player p = findPlayerByUserName(username);
		if(p==null){throw new ExistingSubscriberException();}
		
    	
    	if (numberTokens == 0L | numberTokens<0) {
			throw new BadParametersException("");
		}
    	
    	
    		try {
				
		    		p.setNbTokens(p.getNbTokens()+numberTokens);
					update(p);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    
 
    public static void debitSubscriber(String username, long numberTokens, String managerPwd)
			throws AuthenticationException, ExistingSubscriberException,
			SubscriberException, BadParametersException, SQLException{
    	
       	

		Player p = findPlayerByUserName(username);
		if(p==null){throw new ExistingSubscriberException();}
		
    	
    	if (numberTokens == 0L | numberTokens<0) {
			throw new BadParametersException("");
		}
    	
    	if (numberTokens == 0L | numberTokens<0 ) {
			throw new BadParametersException("nombre de tokens à debiter est bizarre !!");
		}
    	if (numberTokens > p.getNbTokens()){throw new SubscriberException();}
    		try {
				
		    		p.setNbTokens(p.getNbTokens()-numberTokens);
					update(p);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
    
    
    public static long debitSubscriberByUserName(String username, long numberTokensToDebit) throws BadParametersException{
    	
       	
    	Player p=null;
		try {
			p = findPlayerByUserName(username);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	if (numberTokensToDebit == 0L) {
			throw new BadParametersException("nombre de tokens à debiter est bizarre !!");
		}
    	if (numberTokensToDebit>0&&numberTokensToDebit<=p.getNbTokens()){
    		try {
				if(p!=null){
		    		p.setNbTokens(p.getNbTokens()-numberTokensToDebit);
					update(p);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		p.setNbTokens(p.getNbTokens()-numberTokensToDebit);
    		return p.getNbTokens();
    	}
    	else {
    		return -1;
    	}
    
    }
    
	
	/*	j'ai fait une décision de les manipuler après pour la mise comme mentionnée dans l'interface"
	 *  "java.util.List<java.util.List<java.lang.String>>" c'est à dire on va mettre ça ici juste une 
	 *  pour retourner un type pareil liste des players et on va les manupiler après "*/
	
	/*
	public static ArrayList<Player> listPlayers() throws SQLException {
		Connection connection1 = DBConnection.newConnection();
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("SELECT * FROM player ORDER BY username, first_name, last_name");
		ResultSet resultSet1 = preparedStatement1.executeQuery();
		
		*/
		/*on va creer un liste pour contenir tous les elements trouves dans la resultset*/
	/*	ArrayList<Player> playerList = new ArrayList<Player>();
		Player player = null;
		int count = 0;
		while (resultSet1.next()) {
			count ++;			
			try {
				player = new Player(
						resultSet1.getString("LASTNAME"),
						resultSet1.getString("FIRSTNAME"),
						resultSet1.getString("USERNAME"),
						resultSet1.getDate("BORNDATE"),
						resultSet1.getLong("TOKENS")
						);
			} catch (BadParametersException e) {
				// TODO Auto-generated catch block
				System.out.println("erreur est survenue pour trouver la liste des players, veuillez reessayer");
				e.printStackTrace();
			}
			playerList.add(player);
		}
		resultSet1.close();
		preparedStatement1.close();
		connection1.close();

		return playerList;
	}
*/
	
	public static List<List<String>> listPlayers() throws SQLException {
		
		Connection connection1 = DBConnection.newConnection();
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("SELECT * FROM player ORDER BY username");
		ResultSet resultSet1 = preparedStatement1.executeQuery();
		/*on va creer un liste pour contenir tous les elements trouves dans la resultset*/

		List<List<String>> listeePlayers = new ArrayList<>();
		List<String> list =new ArrayList<>();
		
		while (resultSet1.next()) {
						
			while (resultSet1.next()) {
						list.add("le nom du player est"+ resultSet1.getString("LASTNAME"));
						list.add("le prenom du player est "+resultSet1.getString("FIRSTNAME"));
						list.add("le username du player est "+resultSet1.getString("USERNAME"));
						list.add("la date de naissance du player est "+resultSet1.getDate("BORNDATE").toString()	);
						list.add("le nombre du jetons restant est "+new Long(resultSet1.getLong("TOKENS")).toString());
						listeePlayers.add(list);	
			}
		}
		
		resultSet1.close();
		preparedStatement1.close();
		connection1.close();
		return listeePlayers;
	}

	//retourner une liste contenant les attiributs d'un player
	public static ArrayList<String> infosSubscriber(String username,String pwdSubs) throws SQLException{
		/*on va chercher un player particulier puis stocker ses valeurs dans une liste de <String>*/
		
		Connection connection1 = DBConnection.newConnection();
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("SELECT * FROM player WHERE USERNAME LIKE ? AND PASSWORD LIKE ?");
		preparedStatement1.setString(1, username);
		preparedStatement1.setString(2, pwdSubs);
		ResultSet resultSet1 = preparedStatement1.executeQuery();
		ArrayList<String> list = new ArrayList<String>();
		while (resultSet1.next()) {
			
					list.add(resultSet1.getString("LASTNAME"));
					list.add(resultSet1.getString("FIRSTNAME"));
					list.add(resultSet1.getString("USERNAME"));
					list.add(resultSet1.getDate("BORNDATE").toString()	);
					list.add(new Long(resultSet1.getLong("TOKENS")).toString());
		}

		resultSet1.close();
		preparedStatement1.close();
		connection1.close();


		return list;
	}
	
	public static Player update(Player player) throws SQLException {
		Connection connection1 = DBConnection.newConnection();
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("UPDATE player SET LASTNAME=?, FIRSTNAME=?, BORNDATE=?, TOKENS=? WHERE USERNAME LIKE ?");
		/* mise à jour de tous les attributs d'un player spécifique*/
		
		preparedStatement1.setString(1, player.getLastName());	
		preparedStatement1.setString(2, player.getFirstName());
		preparedStatement1.setString(3, player.getUserName());
		preparedStatement1.setDate(4, java.sql.Date.valueOf(player.getBornDate().toString()));
		preparedStatement1.setLong(5, player.getNbTokens());	
		
		preparedStatement1.executeUpdate();
		
		preparedStatement1.close();
		
		connection1.close();
		/*retourner pour future utilisation*/ 
		return player;
	}
	
	public static void changeSubsPwd(String username, String newPwd, String currentPwd) throws SQLException {
		
		Connection connection1 = DBConnection.newConnection();
		
		/*on va tester d'abord si le mot de passe ancien est correcte, pour cela on va chercher dans 
		 * la base de donner pour le player qui correspond à ce username et qui correspend à ce mot de passe */
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("SELECT * FROM player WHERE USERNAME LIKE ? AND password LIKE ?");
		preparedStatement1.setString(1, username);
		preparedStatement1.setString(2, currentPwd);
		ResultSet resultSet1 = preparedStatement1.executeQuery();
		preparedStatement1.close();
		if(resultSet1.next()== true){
			
			/* maintenant si le playRecherchee est trouve c'est à 
			 * dire que le mot de passe et le user name sont correctes donc playerRechercheee n'est pas null
			 *  il suffit donc d'executer la mise à jours*/
			PreparedStatement preparedStatement2 = connection1.prepareStatement("UPDATE player SET PWD=? WHERE USERNAME LIKE ?");
			preparedStatement2.setString(1, newPwd);	
			preparedStatement2.setString(2, username);
			preparedStatement2.executeUpdate();
			preparedStatement2.close();
		}
			resultSet1.close();
			connection1.close();
		}
	

	public static long unsubscribe(String username) throws SQLException {
			
		Player p=null;
		p=PlayersManager.findPlayerByUserName(username);
			if(p!=null){
		Connection connection1 = DBConnection.newConnection();
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("DELETE FROM player WHERE USERNAME LIKE ?");
		preparedStatement1.setString(1, username );
		Scanner sc = new Scanner(System.in);
		int i = sc.nextInt();
		
		/*choix 0 pour la confirmation et 1 si non*/
		System.out.println("select 0 for confirmation and 1 if not");
		if(i==0){
			preparedStatement1.executeUpdate();
			preparedStatement1.close();
			connection1.close();
			return p.getNbTokens();
		}
		else{
			preparedStatement1.close();
			connection1.close();
			return p.getNbTokens();
			} 
		}
			return -1;
			
	}
	
	
	public static void deleteBetsCompetition(String competition, String username, String pwdSubs)
			throws AuthenticationException, CompetitionException, ExistingCompetitionException, SQLException, BadParametersException {
		/*
		 * On cherche si la compétition existe déjà
		 */
		if(CompetitionManager.findByName(competition)==false){throw new ExistingCompetitionException("La compétition n'existe pas");}
		Competition compet=CompetitionManager.findByName1(competition);
		Player user=findPlayerByUserName(username);
		
		/*
		 * On vérifie si la compétition est fermée
		 */
		if(compet.isClosed()==true){throw new CompetitionException("la compétition est déjà fermée");}
		Connection connection1 = DBConnection.newConnection();
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("DELETE FROM bet WHERE player=? AND competition=?");
		preparedStatement1.setString(1, Integer.toString(user.getId()) );
		preparedStatement1.setString(2, Integer.toString(compet.getId()) );
		preparedStatement1.executeUpdate();
		preparedStatement1.close();
		connection1.close();
		
	}

	public static boolean hasValidUserName(String username) {

		return username.matches(VERIFYUSERNAME);
	}
	
}







