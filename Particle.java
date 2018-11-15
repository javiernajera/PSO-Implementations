import java.util.Random;

public class Particle {
    
    private double[] position;
    private double[] velocity;
    
    private Particle[] neighborhood;
    
    private double[] personalBest;
    private double[] neighborhoodBest;
    
    private double personalBestScore;
    private double neighborhoodBestScore;
    private double score;
    
    // Constructor for particle with two arguments num_dimensions and the
    // topology.
    public Particle (int num_dimensions, String test_function) {
        
        Random rand = new Random();
        
        position = new double[num_dimensions];
        velocity = new double[num_dimensions];
        neighborhoodBest = new double[num_dimensions];
        personalBest = new double[num_dimensions];
        neighborhood = null;
        
        personalBestScore = Double.MAX_VALUE;
        neighborhoodBestScore = Double.MAX_VALUE;
        score = Double.MAX_VALUE;
        // given each topology's feasible bounds, it sets the particles position
        // to be randomly assigned to the respective bounds for each topology
        for (int i = 0; i < num_dimensions; i++) {
        	if (test_function.equals("rok")) {
        		if (rand.nextDouble() < 0.5) {
        			position[i] = rand.nextDouble() * 30;
        		}
        		else {
        			position[i] = rand.nextDouble() * -30;
        		}
        	}
        	else if (test_function.equals("ack")) {
        		if (rand.nextDouble() < 0.5) {
        			position[i] = rand.nextDouble() * 32;
        		}
        		else {
        			position[i] = rand.nextDouble() * -32;
        		}
        	}
        	if (test_function.equals("ras")) {
        		if (rand.nextDouble() < 0.5) {
        			position[i] = rand.nextDouble() * 5.12;
        		}
        		else {
        			position[i] = rand.nextDouble() * -5.12;
        		}
        	}
        	if (rand.nextDouble() < 0.5) {
        		velocity[i] = rand.nextDouble();
        	}
        	else {
        		velocity[i] = rand.nextDouble() * -1;
        	}
            neighborhoodBest[i] = 1;
            personalBest[i] = position[i];
        }
    }
    
    // all the rest of these functions are getters and setters for the 
    // instance variables of a particle. 
    public double[] get_position() {
        return position;
    }
    
    public double[] get_velocity() {
        return velocity;
    }
    
    public Particle[] get_neighborhood() {
        return neighborhood;
    }
    
    public double[] get_personalBest() {
        return personalBest;
    }
    
    public double[] get_neighborhoodBest() {
        return neighborhoodBest;
    }
    
    public double get_personalBestScore() {
    	return personalBestScore;
    }
    
    public double get_neighborhoodBestScore() {
    	return neighborhoodBestScore;
    }
    
    public double get_score() {
    	return score;
    }
    
    public void set_position(double[] position) {
        this.position = position.clone();
    }
    
    public void set_velocity(double[] velocity) {
        this.velocity = velocity.clone();
    }
    
    public void set_neighborhood(Particle[] neighborhood) {
        this.neighborhood = neighborhood.clone();
    }
    
    public void set_personalBest(double[] personalBest) {
        this.personalBest = personalBest.clone();
    }
    
    public void set_neighborhoodBest(double[] neighborhoodBest) {
        this.neighborhoodBest = neighborhoodBest.clone();
    }
    
    public void set_personalBestScore(double personalBestScore) {
    	this.personalBestScore = personalBestScore;
    }
    
    public void set_neighborhoodBestScore(double neighborhoodBestScore) {
    	this.neighborhoodBestScore = neighborhoodBestScore;
    }
    
    public void set_score(double score) {
    	this.score = score;
    }

}
