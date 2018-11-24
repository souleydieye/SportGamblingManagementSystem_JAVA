package fr.uv1.bettingServices.exceptions;
/**
 * @author gargouri <br>
 * 
 */
public class ExistingCompetitionException extends Exception{
	
	public ExistingCompetitionException(){
		System.out.println("La comp�tition que vous voulez ajouter existe d�j� dans la base de donn�es");
	}

	public ExistingCompetitionException(String message){
		//constructor whit error message
		super(message);
	}


}
