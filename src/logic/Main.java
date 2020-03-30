package logic;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.revwalk.RevCommit;





public class Main {
	
    private static PrintWriter out;	
    private static Scanner scanner;
    private static final String CLONEPATH = "tmp";
    		
	public static void main(String[] args) 
	{
		
		try
		{
	
			//Dichiarazioni variabili
			
			String repositoryURL = "https://github.com/";	//URL repository
	    	
			try { out = new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"), true); }
			catch(UnsupportedEncodingException e){ e.printStackTrace(); }
			
			scanner = new Scanner(System.in);
			
			//Richiesta da input dell'URL
			
			out.println("Inserisci l'url username e nome della repository (es: andreapaci/Homework_git)");
			repositoryURL += scanner.next();
			
			out.println("La repository è: " + repositoryURL + "\n");
			
	        //Clone della repository in una cartella temporanea specificata da "CLONEPATH" (path relativo)
			//Il clone è essenziale per ottenere il log delle commit
			//La cartella e il suo contenuto vengono eliminati a fine processo
			
			out.println("Clonazione della repository \"" + repositoryURL + "\" ...");
			
	        Git git = Git.cloneRepository()
	        		  .setURI(repositoryURL)
	        		  .setDirectory(new File(CLONEPATH))
	        		  .call(); 
	        
	        out.println("Clonazione effettuata con successo!\n"
	        		+ "Stampa dell'id dei commit con la parola \"added\" nella descrizione.\n");
	        
	        //Stampa del log commit con il relativo ID
	        
	        Iterable<RevCommit> log = git.log().call();
	        
	        for (Iterator<RevCommit> iterator = log.iterator(); iterator.hasNext();) {
	        	RevCommit rev = iterator.next();
	        	//Variabile usata temporaneamente per effettuare il confronto tra la descrizione del commit e la
	        	//parola "added" convertendola in lower case
	        	String s = rev.getFullMessage().toLowerCase(Locale.ROOT);
	        	
	        	if(s.contains("added"))
	        	{
	        		out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n" + 
	        		"Commit ID [" + rev.getId().getName() + "]:\n\t"  + rev.getFullMessage().replace("\n", "\n\t\t"));
	        	}
	        }
      	  	out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n\n" + 
      	  			"Fine stampa dei commit\n");

	        
      	  	//Chiusura di git (essenziale per poter rimuovere la repository
	        git.close();
	        
	      
		}
		catch (InvalidRemoteException e) { e.printStackTrace(); } 
		catch (TransportException e) { e.printStackTrace(); } 
		catch (GitAPIException e) { e.printStackTrace(); }
		
		//Eliminazione della directory temporanea con la repository
		try { FileUtils.deleteDirectory(new File(CLONEPATH)); } 
		catch (IOException e) {	e.printStackTrace(); }
		
		out.println("Programma terminato");
		
		System.exit(0);
		
	}

}
