# PSO
 README

This Particle Swarm Optimization (PSO) system is a standard implmentation 

that can be tested against 3 test functions (Rosenbrock, Ackley, & Rastrigin) 

with distinct solution spaces.  The system implements 4 topologies: Global,

ring, Von Neumann, and a random topology.  The topology that is used determines

how each particle should construct its neighborhood (this is used for local best). 

Our runPSO.py file performs the different PSO implementations on the different 

functions and outputs the most optimal value the system had found.  The different

implementations of PSO can be run in the cmd or terminal using command line args.

THe syntax of the command should be:



	java PSO topology particles iterations testFuntion Dimensions



-PSO: is the file that where the algorithm is run from


-topology: is the topology you want to use (gl, ri, vn, ra)
	
	gl is global topology
	
	ri is ring topology
	
	vn is Von Neumann topology

	ra is random topology


-particles: the number of particles in the system
	
	because Von Neumann constructs its set of neighborhood particles

	we use specific set of numbers in our runPSO.py script.


-iterations: the number of iterations the system should do before terminating


-testFunction: the test function the PSO system should use (ack, rok & ras)

	ack is the Ackley function

	rok is the Rosenbrock function

	ras is the Rastrigin function

-Dimensions: the number of dimensions for the test functions
	
	**the way the test functions are implemented is to represent

	x in 30 dimensions. so the argument should always be 30.**




*****************************NOTES ON THE SYSTEM****************************

The system uses the standard PSO velocity updateequation that we are using 

to update every particles velocity is can be found in the update velocity 

function in the PSO.java class.



Also the way the test functions themselves are calculated can also be found 

in the PSO.java class. 