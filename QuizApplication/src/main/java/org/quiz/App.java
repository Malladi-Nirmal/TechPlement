package org.quiz;

import java.util.Scanner;

import org.quiz.Dao.QuizDao;
import org.quiz.Dao.QuizImplementation;

public class App 
{
    public static void main( String[] args )
    {
    	 
	    	Scanner scan = new Scanner(System.in);
	        System.out.println( "Welcome to QuizApplication \n==========================");
	        System.out.println( "Press \n1) For Create Quizzes"
	        		    			+ "\n2) For Take Quizzes"
	        		    			+ "\n3) For ListQuizzes"
	        		    			+ "\n4) For Exit" );
	        while (true) {
	        	
	        int input = scan.nextInt();
	        QuizDao quizDao = new QuizImplementation();
	        
	        switch (input) {
	        case 1: quizDao.createQuiz();
	        	break;
	        
	        case 2: quizDao.takeQuiz();
	        	break;
	        
	        case 3: quizDao.listQuizzes();
	        	break;
	        	
	        case 4:	System.out.println("Goodbye!");
	        		System.exit(0);
	        	break;
	        
	        default: System.out.println("Invalid command!");
	        }
	        System.out.println();
        } 
    }
}
