import java.util.Random;

public class PSO {
    
    public static final double CHI = 0.7298;
    public static final double SIGMA1 = 2.05;
    public static final double SIGMA2 = 2.05;
    
    public static String topology;
    public static int num_particles;
    public static int num_iterations;
    public static String test_function;
    public static int num_dimensions;
    
    public static void main(String args[]) {
        
        Random rand = new Random();

        read_arguments(args);                           // sets params
        
        // create swarm and neighbor array 
        Particle[] swarm = new Particle[num_particles];
        Particle[] neighbors = new Particle[2];
        
        // populates swarm with correct number of particles and topology
        for (int i = 0; i < num_particles; i++) {
            swarm[i] = new Particle(num_dimensions, test_function);
        }
        
        // these next lines are to set the particles' neighborhood for their 
        // respective topologies 
        // if global, neighborhood is whole swarm
        if (topology.equals("gl")) {
            for (int i = 0; i < num_particles; i++) {
                swarm[i].set_neighborhood(swarm);
            }
        }
        
        // if ring, neighbors are to left and right
        else if (topology.equals("ri")) {
            for (int i = 0; i < num_particles; i++) {
                if (i == 0) {
                    neighbors[0] = swarm[num_particles-1];
                    neighbors[1] = swarm[i+1];
                }
                else if (i == num_particles-1) {
                    neighbors[0] = swarm[i-1];
                    neighbors[1] = swarm[0];
                }
                else {
                    neighbors[0] = swarm[i-1];
                    neighbors[1] = swarm[i+1];
                }
                swarm[i].set_neighborhood(neighbors);
            }
        }
        
        // if von neumann, neighbors are left right above and below
        else if (topology.equals("vn")) {
            vonNeumann_top(swarm);
        }
        
        // if random, neighborhood of size k recreated every iteration with probability 0.2
        else if (topology.equals("ra")) {
            for (int b = 0; b < num_particles; b++) {
                random_top(swarm[b], swarm, b);
            }
            
        }
        
        // for each iteration
        for (int i = 0; i < num_iterations; i++) {
            
            // prints the best solution swarm has found every 1000th iteration
            if ((i+1) % 1000 == 0 || i == 1) {
                //System.out.printf("%-10d", i+1);
                printBest(swarm);
                if (i+1 == 10000) {
                    System.out.println();
                }
            }
            
            // for each particle
            for (int j = 0; j < num_particles; j++) {
                // if random topology, recreate given probability
                if (topology.equals("ra")) {
                    if (rand.nextDouble() < 0.2) {
                        random_top(swarm[j], swarm, j);
                    }
                }
                
                // update the particle's velocity and position
                update_velocity(swarm[j]);
                update_position(swarm[j]);
                
                // score particle's position on rosenbrock
                if (test_function.equals("rok")) {
                    swarm[j].set_score(rosenbrock(swarm[j]));
                    // update personal best if new found
                    if (swarm[j].get_score() < swarm[j].get_personalBestScore()) {
                        swarm[j].set_personalBestScore(swarm[j].get_score());
                        swarm[j].set_personalBest(swarm[j].get_position());
                    }
                }
                
                // score particle's position on ackley
                else if (test_function.equals("ack")) {
                    swarm[j].set_score(ackley(swarm[j]));
                    // update personal best if new found
                    if (swarm[j].get_score() < swarm[j].get_personalBestScore()) {
                        swarm[j].set_personalBestScore(swarm[j].get_score());
                        swarm[j].set_personalBest(swarm[j].get_position());
                    }
                }
                
                // score particle's position on rastrigin
                else if (test_function.equals("ras")) {
                    swarm[j].set_score(rastrigin(swarm[j]));
                    // update personal best if new found
                    if (swarm[j].get_score() < swarm[j].get_personalBestScore()) {
                        swarm[j].set_personalBestScore(swarm[j].get_score());
                        swarm[j].set_personalBest(swarm[j].get_position());
                    }
                }
            }
            
            // check for new neighborhood bests
            for (int k = 0; k < num_particles; k++) {
                for (int a = 0; a < swarm[k].get_neighborhood().length; ++a) {
                    if (swarm[k].get_neighborhood()[a].get_score() < swarm[k].get_neighborhoodBestScore()) {
                        swarm[k].set_neighborhoodBestScore(swarm[k].get_neighborhood()[a].get_score());
                        swarm[k].set_neighborhoodBest(swarm[k].get_neighborhood()[a].get_position());
                    }
                }
            }
            
            
        }   
    }
    
    // this function takes arguments given in command line
    // and assigns them to the correct instance variables 
    public static void read_arguments(String[] args) {
        
        topology = args[0];
        num_particles = Integer.parseInt(args[1]);
        num_iterations = Integer.parseInt(args[2]);
        test_function = args[3];
        num_dimensions = Integer.parseInt(args[4]);
        
    }
    
    
    // this is the function that updates a particles velocity vector.
    // Given a particle it uses the function:
    // vi = X(v + V(0,ɸ1)(xi -pi) + V(0, ɸ2)(xi - ni))
    // to update the velocity vector given its current velocity
    // the contriction factor, position of particle, local best, 
    // and neighborhood best.  It also makes sure that the velocities
    // do not exceed a certain bounds based on the test function. 
    // it sets the particle's new velocity at the end.
    public static void update_velocity(Particle particle) {
        
        Random rand = new Random();
        double[] rand1 = new double[num_dimensions];
        double[] rand2 = new double[num_dimensions];
        double[] position = particle.get_position();
        double[] velocity = particle.get_velocity();
        double[] personalBest = particle.get_personalBest();
        double[] neighborhoodBest = particle.get_neighborhoodBest();
        
        for (int j = 0; j < num_dimensions; j++) {
            rand1[j] = rand.nextDouble() * SIGMA1;
            rand2[j] = rand.nextDouble() * SIGMA2;
        }
        
        for (int i = 0; i < num_dimensions; i++) {
            velocity[i] = CHI * (velocity[i] + (rand1[i] * (personalBest[i] - position[i])) + 
                    (rand2[i] * (neighborhoodBest[i] - position[i])));
            if (test_function.equals("ras")) {
                if (velocity[i] < -5.12) {
                    velocity[i] = -5.12;
                }
                else if (velocity[i] > 5.12) {
                    velocity[i] = 5.12;
                }
            }
            if (test_function.equals("ack")) {
                if (velocity[i] < -32.768) {
                    velocity[i] = -32.768;
                }
                else if (velocity[i] > 32.768) {
                    velocity[i] = 32.768;
                }
            }
            if (test_function.equals("rok")) {
                if (velocity[i] < -2.048) {
                    velocity[i] = -2.048;
                }
                else if (velocity[i] > 2.048) {
                    velocity[i] = 2.048;
                }
            }
        }
        
        particle.set_velocity(velocity);
    }
    
    
    // This function updates a particles position. It takes the 
    // particles position vector and its velocity vector to calculate the
    // new position and then it sets this new position for the given particle. 
    public static void update_position(Particle particle) {
        
        double[] position = particle.get_position();
        double[] velocity = particle.get_velocity();
        
        for (int i = 0; i < num_dimensions; i++) {
            position[i] += velocity[i];
        }
        
        particle.set_position(position);
    }
    
    
    // Given a particle, it uses it's position vector to determine 
    // what it's score is for rosenbrock test function.
    // it returns this score. 
    public static double rosenbrock(Particle particle) {
        double sum = 0.0, x = 0.0, x2 = 0.0;
        double[] positions = particle.get_position();
        for(int i = 0; i < num_dimensions-1; i++){
             x = positions[i];
             x2 = positions[i+1];
             sum += 100.0 * Math.pow(x2 - x*x, 2.0) + Math.pow(x-1.0, 2.0);
          }  
        return sum;
    }
    
    
    // Given a particle, it uses it's position vector to determine 
    // what it's score is for ackley test function.
    // it returns this score. 
    public static double ackley(Particle particle) {
        double x = 0.0, firstSum = 0.0, secondSum = 0.0;
        double[] positions = particle.get_position();

        for(int i = 0; i < num_dimensions; i++){
             x = positions[i];
             firstSum += x*x;
             secondSum += Math.cos(2.0*Math.PI*x);
          }  
        
        return -20.0 * Math.exp(-0.2 * Math.sqrt(firstSum/num_dimensions)) - 
                Math.exp(secondSum/num_dimensions) + 20.0 + Math.E;   
    }
    
    
    // Given a particle, it uses it's position vector to determine 
    // what it's score is for rastrigin test function.
    // it returns this score. 
    public static double rastrigin(Particle particle) {
        double sum = 0.0, x = 0.0;
        double[] positions = particle.get_position();
        for(int i = 0; i < num_dimensions; i++){
            x = positions[i];
            sum += x*x - 10.0*Math.cos(2.0*Math.PI*x) + 10.0;
        }
    
        return sum;
    }
    
    
    // assigns neighborhood for all swarm if they are in the Von Neumann topology
    // it uses a bunch of conditionals to build the neighborhoods properly.
    // at the end it sets the neighborhood it constructed for VN to each particle
    // in the swarm. 
    public static void vonNeumann_top(Particle[] swarm) {
        
        Particle[][] vonNeumannArray;
        Particle[] neighbors = new Particle[4];
        int counter = 0;
        
        if (num_particles == 16) {
            vonNeumannArray = new Particle[4][4];
            
            for (int i = 0; i < 4; ++i) {
                for (int j = 0; j < 4; ++j) {
                    vonNeumannArray[i][j] = swarm[counter];
                    ++counter;      
                }
            }
            
            for (int a = 0; a < num_particles; a++) {
                for (int k = 0; k < 4; ++k) {
                    for (int l = 0; l < 4; ++l) {
                        if (k == 3) {
                            neighbors[0] = vonNeumannArray[0][l];
                        }
                        else {
                            neighbors[0] = vonNeumannArray[k+1][l];
                        }
                        if (k == 0) {
                            neighbors[1] = vonNeumannArray[3][l];
                        }
                        else {
                            neighbors[1] = vonNeumannArray[k-1][l];
                        }
                        if (l == 3) {
                            neighbors[2] = vonNeumannArray[k][0];
                        }
                        else {
                            neighbors[2] = vonNeumannArray[k][l+1];
                        }
                        if (l == 0) {
                            neighbors[3] = vonNeumannArray[k][3];
                        }
                        else {
                            neighbors[3] = vonNeumannArray[k][l-1];
                        }
                    }
                }
                swarm[a].set_neighborhood(neighbors);
            }
            
        }
        
        else if (num_particles == 30) {
            vonNeumannArray = new Particle[5][6];
            
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 6; j++) {
                    vonNeumannArray[i][j] = swarm[counter];
                    ++counter;      
                }
            }
            
            for (int a = 0; a < num_particles; ++a) {
                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 6; l++) {
                        if (k == 4) {
                            neighbors[0] = vonNeumannArray[0][l];
                        }
                        else {
                            neighbors[0] = vonNeumannArray[k+1][l];
                        }
                        if (k == 0) {
                            neighbors[1] = vonNeumannArray[4][l];
                        }
                        else {
                            neighbors[1] = vonNeumannArray[k-1][l];
                        }
                        if (l == 5) {
                            neighbors[2] = vonNeumannArray[k][0];
                        }
                        else {
                            neighbors[2] = vonNeumannArray[k][l+1];
                        }
                        if (l == 0) {
                            neighbors[3] = vonNeumannArray[k][5];
                        }
                        else {
                            neighbors[3] = vonNeumannArray[k][l-1];
                        }
                    }
                }
                swarm[a].set_neighborhood(neighbors);
            }
        }
        
        else if (num_particles == 49) {
            vonNeumannArray = new Particle[7][7];
            
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    vonNeumannArray[i][j] = swarm[counter];
                    ++counter;      
                }
            }
            
            for (int a = 0; a < num_particles; ++a) {
                for (int k = 0; k < 7; k++) {
                    for (int l = 0; l < 7; l++) {
                        if (k == 6) {
                            neighbors[0] = vonNeumannArray[0][l];
                        }
                        else {
                            neighbors[0] = vonNeumannArray[k+1][l];
                        }
                        if (k == 0) {
                            neighbors[1] = vonNeumannArray[6][l];
                        }
                        else {
                            neighbors[1] = vonNeumannArray[k-1][l];
                        }
                        if (l == 6) {
                            neighbors[2] = vonNeumannArray[k][0];
                        }
                        else {
                            neighbors[2] = vonNeumannArray[k][l+1];
                        }
                        if (l == 0) {
                            neighbors[3] = vonNeumannArray[k][6];
                        }
                        else {
                            neighbors[3] = vonNeumannArray[k][l-1];
                        }
                    }
                }
                swarm[a].set_neighborhood(neighbors);
            }
        }
        
    }
    
    
    // assigns neighborhood for single particle, makes recreating easier
    // it assigns unique random neighbors for each particle
    // sets the particle's neighborhood to the one it constructed. 
    public static void random_top(Particle particle, Particle[] swarm, int i) {
        
        Random rand = new Random();
        // fixed k value of 5
        Particle[] neighbors = new Particle[5];
        int[] indices = new int[6];
        boolean keepGoing = true;
        
        while (keepGoing) {
            keepGoing = false;
            indices[0] = i;
            indices[1] = rand.nextInt(num_particles);
            indices[2] = rand.nextInt(num_particles);
            indices[3] = rand.nextInt(num_particles);
            indices[4] = rand.nextInt(num_particles);
            indices[5] = rand.nextInt(num_particles);
            // check for unique indices
            for (int j = 0; j < indices.length-1; j++) {
                for (int k = j+1; k < indices.length; k++) {
                    if (indices[j] == indices[k]) {
                        keepGoing = true;
                    }
                }
            }
        }
            
        for (int l = 0; l < neighbors.length; l++) {
            neighbors[l] = swarm[indices[l]];
        }
        
        particle.set_neighborhood(neighbors);       
    }
    
    // prints the particle with the best score score in the swarm
    public static void printBest(Particle[] swarm) {
        
        double best = Double.MAX_VALUE;
        
        // find best score
        for (int i = 0; i < num_particles-1; i++) {
            if (swarm[i].get_score() < best) {
                best = swarm[i].get_score();
            }
        }
        
        System.out.print(best + "   ");
    }

}
