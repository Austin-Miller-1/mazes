package com.amw.sms;

import com.amw.sms.algorithms.Dijkstra;
import com.amw.sms.algorithms.generation.Sidewinder;
import com.amw.sms.mazes.InvalidMazeException;
import com.amw.sms.mazes.MazeBuilder;
import com.amw.sms.mazes.MazeBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import ij.IJ;

/**
 * Application class. 
 * Creates a maze using one of the implemented maze-generation algorithms, 
 * prints it to the console, displays it as an image and saves that image
 * to the folder containing the application.
 */
@SpringBootApplication
public class App 
{
    @Autowired
    private MazeBuilderFactory mazeBuilderFactory;
    
    public static void main(final String[] args) throws InvalidMazeException {
        new SpringApplicationBuilder(App.class)
            .headless(false)
            .run(args);
    }

    @Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
            final var maze = mazeBuilderFactory.create()
                .withSize(10, 10)
                .usingLongestPath()
                .usingAlgorithm(new Sidewinder())
                .showDistances()
                .build();

            System.out.println(maze);

            final var solveAlgorithm = new Dijkstra();
            solveAlgorithm.solve(maze);
            System.out.println(maze);

            final var mazeImg = maze.getGrid().toImage("Sidewinder", 30);
            mazeImg.show();
            IJ.save(mazeImg, "maze.tif");
		};
	}
}
