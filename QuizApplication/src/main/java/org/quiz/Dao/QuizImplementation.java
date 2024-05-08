package org.quiz.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class QuizImplementation implements QuizDao {

    private final String url = "jdbc:mysql://localhost:3306/quizapp?user=root&password=12345";
    private Scanner scanner;

    public QuizImplementation() {
        scanner = new Scanner(System.in);
    }

    @Override
    public void createQuiz() {
        String insertQuery = "INSERT INTO questionbank (topic, question, option1, option2, option3, option4, correctOption) VALUES (?,?,?,?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(insertQuery)) {

            boolean continueAddingQuestions = true;
            while (continueAddingQuestions) {
                System.out.print("Enter the topic name: ");
                String topic = scanner.nextLine();

                System.out.print("Enter the question: ");
                String question = scanner.nextLine();
                
                //DataStructures for storing
                ArrayList<String> options = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    System.out.print(i + 1 + ").  ");
                    String option = scanner.nextLine();
                    options.add(option);
                }

                int correctAnswer = 0;
                while (correctAnswer < 1 || correctAnswer > 4) {
                    System.out.print("Enter the correct answer option(1-4): ");
                    correctAnswer = scanner.nextInt();
                    scanner.nextLine();
                    if (correctAnswer < 1 || correctAnswer > 4) {
                        System.out.println("Invalid answer option. Please enter a number between 1 and 4.");
                    }
                }
                
                //Inserting into Database
                statement.setString(1, topic);
                statement.setString(2, question);
                for (int i = 0; i < 4; i++) {
                    statement.setString(i + 3, options.get(i));
                }
                statement.setInt(7, correctAnswer);

                int result = statement.executeUpdate();

                if (result != 0) {
                    System.out.println("Successfully created!");
                } else {
                    System.out.println("Technical error occurred.");
                }

                System.out.print("Do you want to add more questions (Y/N): ");
                String yn = scanner.next();
                scanner.nextLine();
                if (yn.equalsIgnoreCase("N")) {
                    continueAddingQuestions = false;
                } else if (!yn.equalsIgnoreCase("Y")) {
                    System.out.println("Enter a valid option (Y/N).");
                }
            }

            System.out.println("Successfully inserted your quiz questions.");
            System.out.print("Do you want to take the quizz (Y/N): ");
            String yn = scanner.next();
            scanner.nextLine();
            if (yn.equalsIgnoreCase("Y")) {
                takeQuiz();
                System.out.println("=================================");
            } else if (yn.equalsIgnoreCase("N")) {
                System.out.println("Thank you for experinces");
            }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to insert quiz questions.");
        }
    }

    @Override
    public void takeQuiz() {
       
        String selectQuestionsByTopic = "SELECT * FROM questionbank WHERE topic=?";
        listQuizzes();
       
            System.out.println();
            System.out.print("================================="
            		+ "\nEnter the topic name: ");
            String topic = scanner.next();
            
            int score = 0;

            try {
            	Connection connection = DriverManager.getConnection(url);
            	PreparedStatement questionStatement = connection.prepareStatement(selectQuestionsByTopic);
            	
                questionStatement.setString(1, topic);
                ResultSet questionResultSet = questionStatement.executeQuery();

                while (questionResultSet.next()) {
                    String question = questionResultSet.getString("question");
                    String option1 = questionResultSet.getString("option1");
                    String option2 = questionResultSet.getString("option2");
                    String option3 = questionResultSet.getString("option3");
                    String option4 = questionResultSet.getString("option4");
                    int correctAnswer = questionResultSet.getInt("correctOption");

                    System.out.println("Quiz Question: \n" + question);
                    System.out.println("1. " + option1 + " 	  3. " + option3);
                    System.out.println("2. " + option2 + " 	  4. " + option4);

                    System.out.println("Select the answer: ");
                    int userAnswer = scanner.nextInt();

                    if (userAnswer == correctAnswer) {
                        System.out.println("Correct!");
                        score++;
                    } else {
                        System.out.println("Incorrect!");
                    }
                }
                System.out.println("Your score: " + score);
                
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to take quiz.");
        }
    }    

    
	@Override
	public void listQuizzes() {
		
		 String selectDistinctTopic = "SELECT DISTINCT topic FROM questionbank";
		 
		 try {
			 Connection connection = DriverManager.getConnection(url);
		 	 PreparedStatement topicStatement = connection.prepareStatement(selectDistinctTopic);
	              
	            ResultSet topicResultSet = topicStatement.executeQuery();
	              
	            while (topicResultSet.next()) {
	                System.out.print(topicResultSet.getString("topic") + "  ");
	            }
		 	} catch (SQLException e){
		 		 e.printStackTrace();
		 		 System.out.println("Failed to list quizzes.");
		 	}

	}
	
}   