package fr.uv1.bettingServices.exceptions;
/**
 * @author gargouri <br>
 * 
 */
public class ExistingCompetitorException extends Exception{
	public ExistingCompetitorException(){
		System.out.println("Le comp�titeur que vous voulez ajouter existe d�j� dans la comp�tition courante");
	}
}
