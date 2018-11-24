package fr.uv1.bettingServices.manager;

import java.sql.Connection;
import java.sql.Date;

import fr.uv1.bettingServices.competitor.*;
import fr.uv1.bettingServices.exceptions.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import fr.uv1.bettingServices.Competitor;
import fr.uv1.bettingServices.competition.*;
import fr.uv1.bettingServices.db.*;


/**
 * @Naanani Hamza
 */

/*********************************** REMARQUES **************************************

 * On peut mélanger les 2 classes pour avoir le Manager et comme ça , ça sera plus évident.
 */

public class CompetitionManager {
	
	
/*******************************************************************************************
 * Méthodes pour faciliter l'implémentation du code	
 ***********************************************************************************/
	
	/**
	 * Méthode pour chercher l'id d'une compétition, elle suppose déjà l'existence la compétition
	 * @param c
	 * @param competition
	 * @return integer
	 * @throws SQLException 
	 */
	public static int chercherIdCompetition(Connection c,String competition) throws SQLException{
		int idCompetition=0;
	PreparedStatement chercherIdCompetition = c.prepareStatement("Select id from competition where name=?");
	chercherIdCompetition.setString(1, competition);
	ResultSet id_compet= chercherIdCompetition.executeQuery();
	while(id_compet.next()){
		idCompetition=id_compet.getInt("idcompetition");
	}
	chercherIdCompetition.close();
	return idCompetition;
	}

	


	/**
	 * Méthode pour transformer une date en calendrier
	 * @param date
	 * @return calendar
	 */
	public static Calendar toCalendar(Date date){ 
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(date);
		  return cal;
		}
	
	/**
	 * Méthode pour vérifier la bonne mise à jour après une requête sql
	 * @param c
	 * @param e1
	 * @throws SQLException
	 */
	public static void verifierBonneMAJ(Connection c,SQLException e1) throws SQLException{
		
			try {

				c.rollback();
				/*on va annuler les executions faites si un problème arrive avec rollback*/

			} catch (SQLException e2) {
				e1.printStackTrace();
				/*pour afficher les details de l'erreur pour le diagnostic pour mentionner ce qui arrivé
				 *  et dans quel bout de code il y a un problème */
			}
		}
	
	/**
	 * Méthode qui renvoie true si la compétition existe dans la base de donnée et false sinon
	 * @param competitionName
	 * @return Boolean
	 * @throws SQLException
	 * @throws BadParametersException
	 * @throws CompetitionException 
	 */
	public static boolean findByName(String competitionName) throws SQLException, BadParametersException, CompetitionException {
		Competition competition = null;
		Connection c = DBConnection.newConnection();
		try{
			PreparedStatement psSelect = c.prepareStatement("select * from competition where name = ?");
			psSelect.setString(1, competitionName);
			ResultSet resultSet = psSelect.executeQuery();
			if(resultSet.next()){
				competition = new Competition(resultSet.getString("name"),resultSet.getDate("closingdate"));
				}
			resultSet.close();
			psSelect.close();
			
		} catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		/*on ferme la connection */
		
		if (competition==null){
			return false;
		}
		else {
			return true;
		}
		}
	/**
	 * Méthode qui renvoie  la compétition 
	 * @param competitionName
	 * @return Competition
	 * @throws SQLException
	 * @throws BadParametersException
	 * @throws CompetitionException 
	 */
	public static Competition findByName1(String competitionName) throws SQLException, BadParametersException, CompetitionException {
		Competition competition = null;
		Connection c = DBConnection.newConnection();
		try{
			PreparedStatement psSelect = c.prepareStatement("select * from competition where name = ?");
			psSelect.setString(1, competitionName);
			ResultSet resultSet = psSelect.executeQuery();
			if(resultSet.next()){
				competition = new Competition(resultSet.getString("name"),resultSet.getDate("closingdate"));
				}
			resultSet.close();
			psSelect.close();
			
		} catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		/*on ferme la connection */
		
		return competition;
			
		}
	
	
	/**
	 * Méthode qui renvoie une compétition a partir de son ID
	 * @param competitionName
	 * @return
	 * @throws SQLException
	 * @throws BadParametersException
	 * @throws ExistingCompetitionException 
	 * @throws CompetitionException 
	 */
	public static Competition findById(int ID) throws SQLException, BadParametersException, ExistingCompetitionException, CompetitionException {
		Competition competition = null;
		Connection c = DBConnection.newConnection();
		try{
			PreparedStatement psSelect = c.prepareStatement("select * from competition where idcompetition = ?");
			psSelect.setInt(1,ID);
			ResultSet resultSet = psSelect.executeQuery();
			if(resultSet.next()){
				competition = new Competition(resultSet.getString("name"),resultSet.getDate("closingdate"));
				competition.setId(ID);
				}
			resultSet.close();
			psSelect.close();
			
		} catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		/*on ferme la connection */
		if(competition==null){throw new ExistingCompetitionException("la compétition n'existe pas");}
		return competition;
		}	
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	
	
	/**
	 * Methode pour ajouter une competition
	 * 
	 * @param competition
	 * @throws SQLException
	 * @throws ExistingCompetitionException
	 * @throws BadParametersException
	 * @throws ExistingCompetitorException 
	 */
	
	public static void addCompetition(String competition, Calendar closingDate,
			Collection<Competitor> competitors )
			throws  ExistingCompetitionException,
			CompetitionException, BadParametersException, SQLException, ExistingCompetitorException {
		/*
		 * On vérifie au début le password du manager
		 */
		
		/** on va creer une nouvelle connection à la base de donnée*
		 *  */
		Connection c = DBConnection.newConnection();
		/**
		 * On crée une competition
		 */
		Competition compet=new Competition(competition,closingDate.getTime());
		/*
		 * On vérifie si la competition existe
		 */
		if(findByName(competition)==true){throw new ExistingCompetitionException("la compétition existe déjà");}
		/*
		 * On vérifie qu'il y'a plus de 2 compétiteurs
		 */
		if(competitors.size()<2){throw new CompetitionException("Il y'a moins de 2 compétiteurs dans la compétition");}
		/*
		 * On vérifie qu'il n'y'a pas 2 compétiteurs pareils
		 */
		for (Competitor i: competitors){
			if(Collections.frequency(competitors,i)>1){
				throw new CompetitionException("Il y'a une redondance dans la liste des compétiteurs");}
		}
		/*
		 * On insère nos données dans la base
		 */
		try {

			/* avec cette preparedStatement on va inserer une competition à la base de données
			 *
			 */
			PreparedStatement preparedStatement1 = c.prepareStatement("INSERT INTO competition (name, closingdate,"
					+ "values (?, ?)");
			
				/*
				 * on prépare le statement
				 */
			preparedStatement1.setString(1, competition);
			preparedStatement1.setDate(2, java.sql.Date.valueOf(closingDate.toString()));
		
			
		
			preparedStatement1.executeUpdate();
			preparedStatement1.close();
			// On ajoute les compétiteurs
			
			for(Competitor i:competitors)
			{ addCompetitor(competition,i);}
		
			
			PreparedStatement psIdValue = c.prepareStatement("select idperson_seq.currval from dual");
			
			ResultSet resultSet = psIdValue.executeQuery();
            resultSet.next();
			int id =  Integer.parseInt(resultSet.getString("idperson"));
			
			c.commit();
			//the auto-incremented iD will be settled in person object
			
			compet.setId(id);
		} catch (SQLException e1) {
			verifierBonneMAJ(c,e1);
		}
		c.close();
		/*on ferme la connection */
	}

	/**
	 * Méthode pour annuler une compétition
	 * @throws BadParametersException 
	 * @throws SQLException 
	 */
	public static void cancelCompetition(String competition )
			throws  ExistingCompetitionException,
			CompetitionException, SQLException, BadParametersException{
		
		
		if ((findByName(competition))==false){throw new ExistingCompetitionException("la competition n'existe pas");}
		
		if (findByName1(competition).isClosed()==true){
			throw new CompetitionException("la compétition est déjà terminée");
		}
		Connection connection1 = DBConnection.newConnection();
		
		try{
			
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("Update  competition set date=? where name LIKE ?");
		preparedStatement1.setDate(1,(Date) new java.util.Date());
		preparedStatement1.setString(2, competition);
		preparedStatement1.executeUpdate();
		preparedStatement1.close();
		connection1.commit();
	    connection1.close();
			
		
		}
		catch (SQLException e1) {
			verifierBonneMAJ(connection1,e1);

		}
		connection1.close();
		/*on ferme la connection */
		
	}
	
	/** 
 	* Méthode pour supprimer une competition
	 * @param nom_competition
	 * @throws SQLException
	 * @throws BadParametersException 
	 */
	
	public static void deleteCompetition(String competition )
			throws  ExistingCompetitionException,
			CompetitionException, SQLException, BadParametersException{
		
	
	
	if ((findByName(competition))==false){throw new ExistingCompetitionException("la competition n'existe pas");}
	
	if (findByName1(competition).isClosed()==false){
		throw new CompetitionException("la compétition est en cours");
	}
	Connection connection1 = DBConnection.newConnection();
	
	try{
		
	PreparedStatement preparedStatement1 = connection1
			.prepareStatement("DELETE FROM competition WHERE name LIKE ?");
	preparedStatement1.setString(1,competition );
	preparedStatement1.executeUpdate();
	preparedStatement1.close();
	connection1.commit();
    connection1.close();
		
	
	}
	catch (SQLException e1) {
		verifierBonneMAJ(connection1,e1);

	}
	connection1.close();
	/*on ferme la connection */
	}
	
	

	
	/**
	 * Méthode pour lister les competitions
	 * 
	 * @param name
	 * @return ArrayList
	 * @throws SQLException
	 * @throws BadParametersException
	 * @throws CompetitionException 
	 */
	public static LinkedList<Competition> lister_Competition() throws SQLException, BadParametersException, CompetitionException {
	
		LinkedList<Competition> liste_competition= new LinkedList<Competition>();
		/* pareil on ouvre une nouvelle connection à la base */

		Connection c = DBConnection.newConnection();

		try{
		
		
		/* on prepare une prepared statemnet */
		PreparedStatement preparedStatement1 = c.prepareStatement("SELECT * FROM competition");
		
		ResultSet resultSet1 = preparedStatement1.executeQuery();
		/* on va maintenant chercher dans le resultat s'il y a une competition avec un name pareil */

		while (resultSet1.next()) {
			/*remarque tres importante pour ce qui suit:
			 *on a plusieurs competitions dans notre base de données , donc à chaque fois on instancie une compétition 
			 *et on l'ajoute dans notre liste de competitions**/
			Competition compet = new Competition(resultSet1.getString("name"),resultSet1.getDate("closingdate"));
			compet.setId(resultSet1.getInt("idcompetition"));
			liste_competition.add(compet);
		}

		resultSet1.close();
		preparedStatement1.close();
		

		
		}
		catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		/*on ferme la connection */
		return liste_competition;
		}

	/**
	 * Méthode pour trouver des persons avec le nom d'une competition
	 * @throws SQLException 
	 * @throws BadParametersException 
	 */
	public static Collection<Competitor> fetchPersons(String competition)
			throws ExistingCompetitionException, CompetitionException, SQLException, BadParametersException {
		
		if (findByName(competition)==false){throw new ExistingCompetitionException("La compétition n'existe pas");}
		
		ArrayList<Competitor> listePersons=new ArrayList<Competitor>();
		
		int idCompetition=findByName1(competition).getId();
		
		Connection c = DBConnection.newConnection();

		try{
		
		
		/* on prepare une prepared statemnet */
		PreparedStatement preparedStatement1 = c.prepareStatement(
				"Select * From person Where idperson="
				+ "(Select person from competitor where idcompetitor="
				+ "(Select competitor from competitionandcompetitor where id="+Integer.toString(idCompetition)+"))");
		
		ResultSet resultSet1 = preparedStatement1.executeQuery();
	
		while (resultSet1.next()) {
			
			Person competiteur=new Person(resultSet1.getString("firstname"),resultSet1.getString("lastname"),resultSet1.getDate("borndate"));
			
		
			
			listePersons.add(competiteur);
		}

		resultSet1.close();
		preparedStatement1.close();
		

		
		}
		catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		/*on ferme la connection */
		return listePersons;
		}
		
	/**
	 * Méthode pour trouver les teams qui participent à une compétition
	 * @throws ClassNotFoundException 
	 */
	public static Collection<Team> fetchTeams(String competition)
			throws ExistingCompetitionException, CompetitionException, SQLException, BadParametersException, ClassNotFoundException {
		
		if (findByName(competition)==false){throw new ExistingCompetitionException("La compétition n'existe pas");}
		
		ArrayList<Team> listeTeams=new ArrayList<Team>();
		
		int idCompetition=findByName1(competition).getId();
		
		Connection c = DBConnection.newConnection();

		try{
		
		
		/* on prepare une prepared statemnet */
		PreparedStatement preparedStatement1 = c.prepareStatement(
				"Select * From team Where idteam="
				+ "(Select team from competitor where idcompetitor="
				+ "(Select competitor from competitionandcompetitor where id="+Integer.toString(idCompetition)+"))");
		
		ResultSet resultSet1 = preparedStatement1.executeQuery();
	
		while (resultSet1.next()) {
			
			Team competiteur=new Team(resultSet1.getString("teamname"));
			
		
			
			listeTeams.add(competiteur);
		}

		resultSet1.close();
		preparedStatement1.close();
		

		
		}
		catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		/*on ferme la connection */
		return listeTeams;
		}
	
	/**
	 * Méthode pour avoir les compétiteurs d'une team à travers son nom
	 */
	
	public static Collection<Competitor> fetchTeamCompetitors(String teamName) 
			throws ExistingCompetitionException, CompetitionException, SQLException, BadParametersException, ClassNotFoundException{
		
		
		
		ArrayList<Competitor> teamCompetitors = new ArrayList<Competitor>();
		
		
		
		Connection c = DBConnection.newConnection();

		try{
		
		
		/* on prepare une prepared statemnet */
		PreparedStatement preparedStatement1 = c.prepareStatement(
				"Select * From person Where team="+teamName);
			
		ResultSet resultSet1 = preparedStatement1.executeQuery();
	
		while (resultSet1.next()) {
			
			Person competiteur=new Person(resultSet1.getString("firstname"),resultSet1.getString("lastname"),resultSet1.getDate("borndate"));
			competiteur.setTeam(resultSet1.getString("team"));
			
		
			
			teamCompetitors.add(competiteur);
		}

		resultSet1.close();
		preparedStatement1.close();
		

		
		}
		catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		/*on ferme la connection */
		return teamCompetitors;
		}
	
	/**
	 * Méthode pour lister les compétiteurs en forme de string d'une compétition
	 */
	public static ArrayList<String> listCompetitorsAsString(String competition) 
			throws ExistingCompetitionException, CompetitionException, SQLException, BadParametersException, ClassNotFoundException{
		
		if (findByName(competition)==false){throw new ExistingCompetitionException("La compétition n'existe pas");}
		
		ArrayList<String> competitorsList=new ArrayList<String>();
		

		
		int idCompetition=findByName1(competition).getId();
		
		Connection c = DBConnection.newConnection();

		try{
		
		
		/* on prepare un statemnet */
		PreparedStatement preparedStatement1 = c.prepareStatement(
				"Select competitortype From competitor Where idcompetitor="
				+ "(Select competitor from competitionandcompetitor where competition="+
						Integer.toString(idCompetition)+")");
						
		ResultSet resultSet1 = preparedStatement1.executeQuery();

		
		
		if (resultSet1.getString("team")=="team"){
			
			ArrayList<Team> teamCompetitors=  (ArrayList<Team>)fetchTeams(competition);
			int i=0;
			while(i<teamCompetitors.size()){
				
				Team competitor=teamCompetitors.remove(i);
				competitorsList.add(competitor.getName()+",");
			}
			
		}
		if (resultSet1.getString("team")=="person"){
			ArrayList<Competitor> personCompetitors= (ArrayList<Competitor>)fetchPersons(competition);
			int i=0;
			while(i<personCompetitors.size()){
				Person competitor=(Person) personCompetitors.remove(i);
				competitorsList.add(competitor.getFirstName()+" "+competitor.getLastName()+",");
			}
			
		}

		resultSet1.close();
		preparedStatement1.close();
		

		
		}
		catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		/*on ferme la connection */
		return competitorsList;
	}
	/**
	 * Méthode lister les bets
	 * @throws BadParametersException 
	 * @throws SQLException 
	 * @throws CompetitionException 
	 */
	public static ArrayList<String> consultBetsCompetition(String competition)
			throws ExistingCompetitionException, SQLException, BadParametersException, CompetitionException{
		if(findByName(competition)==false){throw new ExistingCompetitionException("La compétition n'existe pas");}
		ArrayList<String> bets = new ArrayList<String>();
		try(Connection c = DBConnection.newConnection()){
			PreparedStatement psSelect = c
					.prepareStatement("SELECT id FROM bet WHERE competition=?");
			
			ResultSet resultSet = psSelect.executeQuery();

			while (resultSet.next()) {
				bets.add(Integer.toString(resultSet.getInt("idbet")));
			}
			resultSet.close();
			psSelect.close();
			return bets;
		} catch(SQLException e) {
			e.printStackTrace();
		}
        return bets;
	}
	
	/**
	 * Méthode pour lister les bets sous la forme d'un string
	 * @throws BadParametersException 
	 * @throws SQLException 
	 * @throws ExistingCompetitionException 
	 * @throws CompetitionException 
	 */
	
	public static String consultBetsCompetitionAsString(String competition) throws ExistingCompetitionException, SQLException, BadParametersException, CompetitionException{
		ArrayList<String> bets = consultBetsCompetition(competition);
		int i=0;
		String s="";
		while(i<bets.size()){
			 s+=bets.remove(i)+",";
		}
		return s;
		
	}
	/**
	Méthode pour lister les compétitions comme on veut dans l'interface
	 * @throws CompetitionException 
	 * @throws ExistingCompetitionException 
	 * @throws ClassNotFoundException 
	**/
	public static List<List<String>> listCompetitions() throws SQLException, BadParametersException, ClassNotFoundException, ExistingCompetitionException, CompetitionException{
		
		List<List<String>> listeCompetitions = new ArrayList<>();
		
		LinkedList<Competition> laListeDesCompetitions = (LinkedList<Competition>)lister_Competition();
		
		while(!(laListeDesCompetitions==null)){
			
			List<String> string = (List<String>) new LinkedList<String>();
			Competition competition=laListeDesCompetitions.removeFirst();
			string.add(competition.getName()+": ");
			Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String s = formatter.format("sa date de cloture est : "+competition.getClosingDate()+",");
			string.add(s);
			string.add("Les Bets en cours sont : "+consultBetsCompetitionAsString(competition.getName())+",");
			string.add("Les competiteurs sont"+listCompetitorsAsString(competition.getName()));
			
			
			listeCompetitions.add(string);	
			
		}
		return listeCompetitions;
		
		
	}
	
	/**
	 * Méthode pour renvoyer les compétiteurs d'une compétition ( on s'en fout que ça soit Team ou Person)
	 * @throws SQLException 
	 * @throws BadParametersException 
	 * @throws ClassNotFoundException 
	 */
	
	public static Collection<Competitor> listCompetitors(String competition)
			throws ExistingCompetitionException, CompetitionException, SQLException, BadParametersException, ClassNotFoundException{
		if(findByName(competition)==false){throw new ExistingCompetitionException("La compétition n'existe pas");}
		if(findByName1(competition).isClosed()==true){throw new CompetitionException("La compétition est fermée");}
		
		int idCompetition=findByName1(competition).getId();
		
		Connection c = DBConnection.newConnection();
		
		Collection<Competitor> competitors= new ArrayList<Competitor>();
		
		try{
		
		
		/* on prepare un statemnet */
		PreparedStatement preparedStatement1 = c.prepareStatement(
				"Select competitortype From competitor Where idcompetitor="
				+ "(Select competitor from competitionandcompetitor where competition="+
						Integer.toString(idCompetition)+")");
						
		ResultSet resultSet1 = preparedStatement1.executeQuery();

		
		
		
		if (resultSet1.getString("team")=="team"){
			competitors=fetchTeamCompetitors(competition);
			
			
			
			
			}
			
		
		else {
			competitors= fetchPersons(competition);
			
			}
			
		

		resultSet1.close();
		preparedStatement1.close();
		

		
		}
		catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		
		c.close();
		/*on ferme la connection */
		
		return competitors;
		
		
		
	}
	
	/**
	 * Méthode pour insérer un compétiteur dans une compétition
	 * @param competition
	 * @param competitor
	 * @throws SQLException
	 */
	public static void addCompetitor(String competition, Competitor competitor) throws 
			ExistingCompetitionException, CompetitionException,
			ExistingCompetitorException, BadParametersException, SQLException{
		/*
		 * On vérifie le mot de pass du manager
		 */
		
		/*
		 * On cherche si la compétition existe déjà
		 */
		if (findByName(competition)==false){throw new ExistingCompetitionException("la compétition n'existe pas");}
		/*
		 * On cherche si la competition est fermée
		 */
		if (findByName1(competition).isClosed()==true){throw new ExistingCompetitionException("la compétition n'existe pas");};
		/*
		 * On ouvre la connection
		 */
		Connection c = DBConnection.newConnection();
		/*
		 * On prépare notre statement
		 */
		
		try{
		
            PreparedStatement psInsert = c.prepareStatement("INSERT INTO competitionandcompetitor VALUES (?,?)");
            psInsert.setInt(1, ((fr.uv1.bettingServices.competitor.Competitor) competitor).getIdCompetitor());
            psInsert.setInt(2, chercherIdCompetition(c,competition));
            psInsert.executeUpdate();
            psInsert.close();
            c.commit();
		}
		catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		/*on ferme la connection */
	
    }
	
	
	/**
	 * Méthode pour supprimer un compétiteur d'une compétition
	 *
	 * @param competition
	 * @param competitor
	 * @throws BadParametersException 
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public static void deleteCompetitor(String competition, Competitor competitor
			) throws 
			ExistingCompetitionException, CompetitionException,
			ExistingCompetitorException, SQLException, BadParametersException, ClassNotFoundException{
		/*
		 * On vérifie mot de pass manager
		 */
		
		/*
		 * On cherche si la compétition existe déjà
		 */
		if(findByName(competition)==false){throw new ExistingCompetitionException("la compétition n'existe pas");};
		/*
		 * Si la date de la compétition fermée
		 */
		if(findByName1(competition).isClosed()==true){throw new CompetitionException("la compétition est fermée");}
		/*
		 * On se connecte à la base de donnée
		 */
		if(listCompetitors(competition).size()<2){throw new CompetitionException("il n'y'a que 2 compétiteurs dans la compétition");}
		
		Connection c = DBConnection.newConnection();
		try {
            PreparedStatement psDelete = c.prepareStatement("DELETE FROM competitionandcompetitor WHERE competitor_id = ? AND competition_name = ?");
            psDelete.setInt(1, ((fr.uv1.bettingServices.competitor.Competitor) competitor).getIdCompetitor());
            psDelete.setInt(2, chercherIdCompetition(c,competition));
            psDelete.executeQuery();
            psDelete.close();
            c.commit();
		}
		catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		/*on ferme la connection */
		}
	
	
	

	/**
	 * Méthode pour donner un rank au compétiteur
	 * 
	 * @param competitor
	 * @param rank
	 * @param competition
	 * @throws SQLException
	 */
	private  static void rankCompetitor(Competitor competitor,String rank,String competition) throws SQLException{
		
    	Connection c = DBConnection.newConnection();
        try{
            PreparedStatement psUpdate = c.prepareStatement("UPDATE competition SET competitor? = ? WHERE  idcompetition = ?");
            psUpdate.setString(1, rank);
            psUpdate.setInt(2, ((fr.uv1.bettingServices.competitor.Competitor) competitor).getIdCompetitor());
            psUpdate.setInt(3, chercherIdCompetition(c,competition));
            psUpdate.executeUpdate();
            psUpdate.close();
            
            c.commit();
    	}
        catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		/*on ferme la connection */
		}
    
    
    /**
     * Méthode pour faire un poduim
     * 
     * @param competition
     * @param competitor1
     * @param competitor2
     * @param competitor3
     * @throws SQLException
     * @throws BadParametersException
     * @throws ClassNotFoundException 
     */
	public static void settlePodium(String competition, Competitor winner, Competitor second,
			Competitor third )
			throws  ExistingCompetitionException,
			CompetitionException, SQLException, BadParametersException, ClassNotFoundException{
    	/*
    
    	
    	/*
    	 * On vérifie si la compétition existe
    	 */
    	if(findByName(competition)==false){throw new ExistingCompetitionException("La compétition n'existe pas");}
    	/*
    	 * On vérifie que 2 des gagnants ne sont pas les mêmes
    	 */
    	if(winner.equals(second) | second.equals(third) |third.equals(winner)){throw new CompetitionException
    		("Vous ne pouvez pas mettre les même compétiteurs en tant que gagnants");}
    	/*
    	 * On vérifie qu'il y'a déjà des compétiteurs dans la compétition
    	 */
    	if(listCompetitors(competition).size()==0){throw new CompetitionException("la competition n'a pas de compétiteurs");}
    	/*
    	 * On vérifie que les compétiteurs appartiennent à la compétition
    	 */
    	if(listCompetitors(competition).contains(winner)==false
    		| listCompetitors(competition).contains(second)==false
    		| listCompetitors(competition).contains(third)==false)
    	{throw new CompetitionException("les gagnants ne participent pas à la compétition");}
    	
    	/*
    	 * On peut faire notre rank maintenant
    	 */
    	rankCompetitor(winner,"1",competition);
    	rankCompetitor(second,"2",competition);
    	rankCompetitor(third,"3",competition);
    }
    
    /**
     * Méthode pour faire un poduim
     * 
     * @param competition
     * @param competitor
     * @throws SQLException
     * @throws BadParametersException
     * @throws ClassNotFoundException 
     */
    public static void settleWinner(String competition, Competitor winner)
			throws ExistingCompetitionException,
			CompetitionException, SQLException, BadParametersException, ClassNotFoundException{
    	/*

    	 */
    	
    	/*
    	 * On vérifie l'existence de la compétition
    	 */
    	
    	if(findByName(competition)==false){throw new ExistingCompetitionException("La compétition n'existe pas");}
    	/*
    	 
    	/*
    	 * On vérifie qu'il y'a déjà des compétiteurs dans la compétition
    	 */
    	if(listCompetitors(competition).size()==0){throw new CompetitionException("la competition n'a pas de compétiteurs");}
    	/*
    	 * On vérifie que les compétiteurs appartiennent à la compétition
    	 */
    	if(listCompetitors(competition).contains(winner)==false)
    	{throw new CompetitionException("les gagnants ne participent pas à la compétition");}
    	rankCompetitor(winner,"1",competition);    	
    }
    
    public static ArrayList<Competitor> consultResultsCompetition(String competition) 
    		throws ExistingCompetitionException, SQLException, BadParametersException, CompetitionException, ClassNotFoundException {
    	/*
    	 * On vérifie l'existence de la compétition
    	 */
    	
    	if(findByName(competition)==false)
    		throw new ExistingCompetitionException("La compétition n'existe pas");
    	if(findByName1(competition).isClosed()==false){throw new CompetitionException("La compétition n'est pas fermée");}
		
		
		Connection c = DBConnection.newConnection();
		
		ArrayList<Competitor> les_competiteurs= new ArrayList<Competitor>();
		
		
		/* on prepare un statemnet */
		PreparedStatement preparedStatement1 = c.prepareStatement(
				"Select competitor1,competitor2,competitor3 From competition Where name="
				+competition);
						
		ResultSet resultSet1 = preparedStatement1.executeQuery();
	
		les_competiteurs.add(findCompetitorById(resultSet1.getInt("competitor1")));
		les_competiteurs.add(findCompetitorById(resultSet1.getInt("competitor2")));
		les_competiteurs.add(findCompetitorById(resultSet1.getInt("competitor3")));
		
		resultSet1.close();
		c.close();
		return les_competiteurs;
    
    	
    	
    	
    	}
	public static Competitor findCompetitorById(int idCompetitor) throws BadParametersException, ClassNotFoundException, SQLException{
		Connection c = DBConnection.newConnection();
		
		Competitor competitor=null;
		
		
		/* on prepare un statemnet */
		PreparedStatement preparedStatement1 = c.prepareStatement(
				"Select competitortype From competitor Where idcompetitor="
				+Integer.toString(idCompetitor)+")");
						
		ResultSet resultSet1 = preparedStatement1.executeQuery();

		
		
		
		if (resultSet1.getString("team")=="team"){
			competitor=TeamDAO.findByIdCompetitor(idCompetitor);
			
			
			
			
			}
			
		
		else {
			competitor= PersonDAO.findByIdCompetitor(idCompetitor);
			
			}
		
			
		

		resultSet1.close();
		preparedStatement1.close();
			
		
		
		c.close();
	
		return competitor;
		}
	}
		
    
    
    

    

