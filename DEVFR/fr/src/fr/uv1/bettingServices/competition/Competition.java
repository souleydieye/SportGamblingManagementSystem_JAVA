package fr.uv1.bettingServices.competition;
import fr.uv1.bettingServices.Competitor;
import fr.uv1.bettingServices.bet.Bet;
import fr.uv1.bettingServices.competitor.Team;
import fr.uv1.bettingServices.exceptions.AuthenticationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.manager.CompetitionManager;
import fr.uv1.bettingServices.player.Player;
import fr.uv1.utils.*;

import java.sql.SQLException;
import java.util.*;
/**
 * @author Gargouri Mohamed Ghassen
 * @category La classe Competition
 */
public class Competition {
	
	/**
	 * Attributs locales
	 */
	
	private String name;
	private Date closingDate;
	private int id;
	
	/*
	 * Attributs d'association
	 */
	
	private ArrayList<Competitor> competitors = new ArrayList<Competitor>();
	private ArrayList<Competitor> winners = new ArrayList<Competitor>();
	private ArrayList<Bet> bets = new ArrayList<Bet>();
	
	/*
	 * Constante pour valider les noms
	 */
	
	private static final String VALID_NAME = "[\\w\\s\\-.]{4,}";
	
	/**
	 * Accesseurs et mutateurs
	 */
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getClosingDate() {
		return closingDate;
	}
	
	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}
	
	public ArrayList<Competitor> getCompetitors() {
		return competitors;
	}
	
	public void setCompetitors(ArrayList<Competitor> competitors) {
		this.competitors = competitors;
	}
	
	public ArrayList<Competitor> getWinners() {
		return winners;
	}
	
	public void setWinners(ArrayList<Competitor> winners) {
		this.winners = winners;
	}
	
	public ArrayList<Bet> getBets() {
		return bets;
	}
	
	public void setBets(ArrayList<Bet> bets) {
		this.bets = bets;
	}
	
	/**
	 * Constructeur avec 2 paramétres qui servira à la recherche des compétitions dans la base
	 * @param name
	 * @param closingDate
	 * @throws BadParametersException 
	 * @throws CompetitionException 
	 */
	
	public Competition(String name, Date closingDate) throws BadParametersException, CompetitionException {
		 if(!name.matches(VALID_NAME)){
			 throw new fr.uv1.bettingServices.exceptions.BadParametersException("the competition name is invalid");
		 }
	        Date today = new Date(System.currentTimeMillis());
		 if (today.getTime() > (closingDate.getTime())){
				throw new fr.uv1.bettingServices.exceptions.CompetitionException("Provided closing date is invalid");
		          }
		this.name = name;
		this.closingDate = closingDate;
	}
	
	/**
	 * Constructeur qui servira à la construction des compétitions en vérifiant les exceptions
	 * @param name
	 * @param closingDate
	 * @param competitors
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ExistingCompetitionException
	 * @throws CompetitionException
	 * @throws BadParametersException
	 * @throws ExistingCompetitorException
	 */

	public Competition(String name, Date closingDate, ArrayList<Competitor> competitors) throws ClassNotFoundException, SQLException, ExistingCompetitionException, CompetitionException, BadParametersException, ExistingCompetitorException{
		if (CompetitionManager.findByName(name)){
			throw new fr.uv1.bettingServices.exceptions.ExistingCompetitionException();
              }
		
		if (Calendar.getInstance().getTime().after(closingDate)){
			throw new fr.uv1.bettingServices.exceptions.CompetitionException("Provided closing date is invalid");
	          }
		
		if (competitors == null ){
			throw new BadParametersException("Empty competitors");
		}	
		
		if (competitors.size() < 2){
					throw new fr.uv1.bettingServices.exceptions.CompetitionException("There are less than two competitors in this competition");
		                           }
		
		 if (name.length() < 4){
	            throw new fr.uv1.bettingServices.exceptions.BadParametersException("the competition name must contain more than three letters");
	        }
		 
		 if(!name.matches(VALID_NAME)){
			 throw new fr.uv1.bettingServices.exceptions.BadParametersException("the competition name is invalid");
		 }
		 
	      this.closingDate = closingDate;
	      this.name = name;
	      this.competitors = competitors;	
	}
	
	/**
	 * Ajouter un compétiteur à la compétition
	 * @param c
	 * @param managerPwd
	 * @throws ExistingCompetitorException
	 * @throws AuthenticationException
	 * @throws ExistingCompetitionException
	 * @throws CompetitionException
	 * @throws BadParametersException
	 * @throws SQLException
	 */
	
	public void addCompetitor(Competitor c) throws ExistingCompetitorException, AuthenticationException, ExistingCompetitionException, CompetitionException, BadParametersException, SQLException {
		for (Competitor competitor: this.competitors){
			if( competitor.equals(c) ){
				throw new ExistingCompetitorException();
			}
		}
		competitors.add(c);
		//Insertion dans la base
		CompetitionManager.addCompetitor(this.getName(), c);
	}
	
    /**
     * Supprimer un compétiteur de la compétition
     * @param competitor
     * @param managerPwd
     * @throws AuthenticationException
     * @throws ExistingCompetitionException
     * @throws CompetitionException
     * @throws ExistingCompetitorException
     * @throws BadParametersException
     * @throws SQLException
     */
	
	public void deleteCompetitor(Competitor competitor) throws AuthenticationException, ExistingCompetitionException, CompetitionException, ExistingCompetitorException, BadParametersException, SQLException {
		competitors.remove(competitor);
		CompetitionManager.addCompetitor(this.getName(), competitor);
	}

	/**
	 * Trouver un compétiteur dans la compétition par son nom
	 * @param name
	 * @return
	 */
	
    public Competitor findCompetitor(String name) {
        for (Competitor comp : getCompetitors()) {
            if (comp instanceof Team && ((Team) comp).getName().equals(name)) {
                return comp;
            }
        }
        return null;
    }
	
    /**
     * Tester si la compétition est fermée ou pas
     * @return
     */
    
	public boolean isClosed(){
        Date today = new Date(System.currentTimeMillis());
        return (today.after(this.closingDate));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	/**
	 * Redéfinition de la méthode equals
	 */
	
	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof Competition))
			return false;
		Competition c = (Competition) other;
		return (this.name.equals(c.getName()));
	}


	

	

    
	

}
