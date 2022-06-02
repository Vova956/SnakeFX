package org.example;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.io.*;

import java.util.ArrayList;
import java.util.Scanner;

public class SnakeGame extends Pane {

    private ArrayList<Circle> snake = new ArrayList<>();
    private Circle apple = new Circle((int)(1+Math.random()*11)*50,(int)(1+Math.random()*11)*50,25,Color.CYAN);

    private int snakeXDirection = -1;//1 - right    -1 - left
    private int snakeYDirection = 0;//-1 - up       1 - down

    private int score = 0;

    private double time = 0.5;

    private Label label;

    private int xAdd = 0,yAdd = 0;



    public SnakeGame() {
        super();

        snake.add(new Circle(25,Color.CRIMSON));
        snake.get(0).setCenterX(300);
        snake.get(0).setCenterY(300);
        getChildren().add(snake.get(0));

        for (int i = 1; i < 3; i++) {
            snake.add(new Circle(25,Color.GREEN));
            snake.get(i).setCenterX(300+ i*50);
            snake.get(i).setCenterY(300);
            getChildren().add(snake.get(i));

        }

        getChildren().add(apple);

        label = new Label("Score : " + score);
        label.setLayoutX(20);
        label.setLayoutY(20);
        label.setPrefSize(50,15);
        getChildren().add(label);


        setOnKeyPressed(evt->{
            if(evt.getCode() == KeyCode.UP){
                snakeYDirection = -1;
                snakeXDirection = 0;
            }
            else if(evt.getCode() == KeyCode.DOWN){
                snakeYDirection = 1;
                snakeXDirection = 0;
            }
            else if(evt.getCode() == KeyCode.RIGHT){
               snakeXDirection = 1;
               snakeYDirection = 0;
            }
            else if(evt.getCode() == KeyCode.LEFT){
               snakeXDirection = -1;
               snakeYDirection = 0;
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                time -= 0.01;

                if(time <= 0){
                    snakeMove();
                    checkApple();
                    if(checkForDeath()){
                        try {
                            HOF();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //close();
                    }

                    time = 0.5;
                }
            }
        };

       timer.start();

        setFocusTraversable(true);
        setFocused(true);
    }

    boolean checkForDeath(){
        if(snake.get(0).getCenterY() >= 650 || snake.get(0).getCenterX() >= 650) {
            return true;
        }

        for (int i = 0; i < snake.size()-1; i++) {
            for (int j = i+1; j < snake.size(); j++) {
                if(snake.get(i).getCenterX() == snake.get(j).getCenterX() && snake.get(i).getCenterY() == snake.get(j).getCenterY()){
                    return true;
                }
            }
        }

        return false;
    }




    public void snakeMove(){

        xAdd = (int) snake.get(snake.size()-1).getCenterX();
        yAdd = (int) snake.get(snake.size()-1).getCenterY();
        for (int i = snake.size()-1; i >= 1; i--) {
            snake.get(i).setCenterX(snake.get(i-1).getCenterX());
            snake.get(i).setCenterY(snake.get(i-1).getCenterY());
        }

        snake.get(0).setCenterX(snake.get(0).getCenterX() + snakeXDirection*50);
        snake.get(0).setCenterY(snake.get(0).getCenterY() + snakeYDirection*50);
    }

    public void checkApple(){
        if(snake.get(0).getCenterX() == apple.getCenterX() && snake.get(0).getCenterY() == apple.getCenterY()){
            score++;
            label.setText("Score : " + score);

            snake.add(new Circle(xAdd,yAdd, 25,Color.GREEN));

            getChildren().add(snake.get(snake.size()-1));
            do {
                apple.setCenterX((int) (1 + Math.random() * 11) * 50);
                apple.setCenterY((int) (1 + Math.random() * 11) * 50);
            }while(!checkForCord());

        }

    }

    boolean checkForCord(){
        for (int i = 1; i < snake.size(); i++) {
            if(snake.get(i).getCenterX() == apple.getCenterX() && snake.get(i).getCenterY() == apple.getCenterY()){
                return false;
            }
        }
        return true;
    }

    public void HOF() throws IOException {
        FileReader fr= new FileReader("src/main/HOF.txt");
        ArrayList<Player> players = new ArrayList<>();
        Scanner scanner = new Scanner(fr);
        Scanner scanner2 = new Scanner(System.in);

        while(scanner.hasNext()){
           players.add(new Player(Integer.parseInt(scanner.next()),scanner.next()));
        }

        scanner.close();
        try {
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print("Enter name > ");
        String str = scanner2.next();
        players.add(new Player(score,str));

        Player players2[] = players.toArray(new Player[0]);


        for (int i = 0; i < players2.length; i++) {
            for (int j = i+1; j < players2.length; j++) {
                if(players2[j].getScore() > players2[i].getScore()){
                    Player buff = new Player(players2[j]);
                    players2[j] = players2[i];
                    players2[i] = buff;
                }
            }
        }

        PrintWriter writer = new PrintWriter("src/main/HOF.txt");
        writer.print("");
        writer.close();

        System.out.println("HOF : ");
        PrintWriter fw = new PrintWriter("src/main/HOF.txt");
        for (int i = 0; i < players2.length; i++) {
            System.out.println(players2[i].toString());
            fw.println(players2[i].getScore());
            fw.println(players2[i].getName());

        }

        fw.close();

    }
}
