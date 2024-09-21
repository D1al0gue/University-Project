// single neuron code
// all functions for Assignment 1
// and Ex1 of Assignment 2 implemented

#include <iostream>
#include <fstream> // for logging
#include <math.h>
#include <vector>
#include <numeric>
#include <algorithm>
#include <vector>

//activation function
double sigmoid(double x){
    return 1.0 / (1.0 + exp(-x));
}

// one point/sample of training set. 
struct train_point_t{
	std::vector<double> inputs;
	double target;
};

//vector of training samples
struct train_set_t{
  std::vector<train_point_t> train_points;	
};

// usually loaded from file, but in this case training set is small
void set_train_set(train_set_t& ts){
	train_point_t p1;
	p1.inputs = {0.0, 0.0}; //initialize vector
	p1.target = 0.0;
	ts.train_points.push_back(p1);
	train_point_t p2;
	p2.inputs = {0.0, 1.0};
	p2.target = 0.0;
	ts.train_points.push_back(p2);
	train_point_t p3;
	p3.inputs = {1.0, 0.0};
	p3.target = 0.0;
	ts.train_points.push_back(p3);
	train_point_t p4;
	p4.inputs = {1.0, 1.0};
	p4.target = 1.0;
	ts.train_points.push_back(p4);
	
}

// prints all training saples/points
void print_set(const train_set_t& ts){
	for(train_point_t tp:ts.train_points){
		std::cout<<" Inputs ";
		for(double in:tp.inputs) std::cout<<in<<" ";
		std::cout <<" Output: "<<tp.target<<" ";
		std::cout<<std::endl;
	}
}

// to help with plotting the search convergence
// vector to store 
//save file function
std::vector<double> convergence;
void save_vector_to_file(std::vector<double> v){
	std::string file_name;
	std::cout<<" Enter file name for convergence graph:";
	std::cin>>file_name;
	std::ofstream ofs;
    std::cout<<"Saving vector "<<file_name<<" ..."<<std::endl;
    ofs.open(file_name.c_str(), std::ofstream::out);//, std::ofstream::out | std::ofstream::trunc);
	if (ofs.is_open()){
		int count =0 ;
		for(double ve:v){
			ofs<<count<<" "<<ve<<std::endl;
			count++;
		}
		ofs.close();
	}
}

//returns neuron
struct Neuron{
	std::vector<double> weights;   // weights
	double bias;
	double z; // before sigmoid
	double y; // outputs
	void init(int nw);
	double forward(std::vector<double> inputs);
	void print_neuron();
};

// prototype
void draw_output(Neuron& n);

// sets wieghts and biases
void Neuron::init(int n){
	
	bias = 0.0;
	weights.reserve(2); //reserve memory for weights
	weights.emplace_back(0.0); // faster compared with "push_back()"
	weights.emplace_back(0.0);
	std::cout<<" weight size="<<weights.size();
}

// calculates and returns neuron output
double Neuron::forward(std::vector<double> inputs){
	z = bias + weights[0] *  inputs [0] + weights[1] * inputs [1];
	y = sigmoid(z);
	return y;
}


void Neuron::print_neuron(){
    std::cout<<" bias="<<bias;
    std::cout<<" w0="<<weights[0];
    std::cout<<" w1="<<weights[1]<<std::endl;
	
}

// what is the difference now between 
// output and target
double error(Neuron& neuron, double t){
	return (neuron.y -t);
}

// combined squared error for all training samples
double total_error(Neuron& neuron, const train_set_t& ts){
	 double tot_error = 0.0;
    for ( uint i =0 ; i < ts.train_points.size() ; i++){
		neuron.forward(ts.train_points.at(i).inputs);
		double e = error(neuron, ts.train_points[i].target);
        tot_error = tot_error + e*e;
     }
     return tot_error;
}

//enter
void manual_entry(Neuron& n, train_set_t ts) {
    std::cout << "Enter bias: ";
    std::cin >> n.bias;

    std::cout << "Enter weight 0: ";
    std::cin >> n.weights[0];

    std::cout << "Enter weight 1: ";
    std::cin >> n.weights[1];

    // Calculate the total error with the entered values
    double total_err = total_error(n, ts);

    // Print the value of total error
    std::cout << "Total Error: " << total_err << std::endl;
}

void global_search(Neuron& neuron,const train_set_t& train_set ){
    double min_tot_err = 10000.0;
    double w0_min_err=0.0,w1_min_err=0.0,bias_min_err=0.0;
	for (double bias = -10.0; bias < 10.0; bias = bias + 0.1){
	  for ( double w0 = -10.0; w0 < 10.0; w0 = w0 + 0.1){
	   for ( double w1 = -10.0; w1 < 10.0; w1 = w1 + 0.1){
 	      //neuron.set_neuron({w0,w1}, bias);
 	      neuron.bias = bias;
 	      neuron.weights[0] = w0;
 	      neuron.weights[1] = w1;
 	      // your code here
			double tot_error = total_error(neuron, train_set);

			if(tot_error <= min_tot_err){
			min_tot_err = tot_error;
			w0_min_err= w0;
			w1_min_err = w1;
			bias_min_err = bias;
			}
	    }
       }
	}
    // and here
    std::cout<<"total minimum error: "<< min_tot_err<<"\n";
	std::cout<<"total error: "<< total_error << "\n"; 
	std::cout<<"weight 0 total error: "<<w0_min_err<<"\n";
	std::cout<<"weight 1 total error: "<< w1_min_err<<"\n";
	std::cout<<"bias total error: "<<bias_min_err<<"\n";

}

void gradient_search(Neuron& neuron, const train_set_t& train_set) {
    double d = 0.01;
    double learn_rate = 0.1; //change when doing the final exercise
    int n_step = 0;
    double de_db;
    double de_dw0;
    double de_dw1;

    while (n_step < 150) {
        for (uint i = 0; i < train_set.train_points.size(); i++) {
            double e0 = total_error(neuron, train_set);

            // Calculate de_db
            neuron.bias += d;
            double e1 = total_error(neuron, train_set);
            de_db = (e1 - e0) / d;
            neuron.bias -= d;

            // Calculate de_dw0
            neuron.weights[0] += d;
            e1 = total_error(neuron, train_set);
            de_dw0 = (e1 - e0) / d;
            neuron.weights[0] -= d;

            // Calculate de_dw1
            neuron.weights[1] += d;
            e1 = total_error(neuron, train_set);
            de_dw1 = (e1 - e0) / d;
            neuron.weights[1] -= d;

            // Update weights and bias
            neuron.bias -= de_db * learn_rate;
            neuron.weights[0] -= de_dw0 * learn_rate;
            neuron.weights[1] -= de_dw1 * learn_rate;

            double current_tot_err = total_error(neuron, train_set);
            std::cout << "current_tot_err: " << current_tot_err << "   stepcount: " << n_step << "\n";
            convergence.push_back(current_tot_err);
        }
        n_step++;
    }

    for (uint i = 0; i < train_set.train_points.size(); i++) {
        neuron.forward(train_set.train_points.at(i).inputs);
        double e = train_set.train_points.at(i).target - neuron.y;
        std::cout << " i= " << i << " y=" << neuron.y << " e=" << e << std::endl;
    }
    neuron.print_neuron();
}



  
int main(){
	train_set_t train_set;
	set_train_set(train_set);
	print_set(train_set);
	Neuron neuron;
	neuron.init(2);
	// comment/uncomment functions 
	manual_entry(neuron,train_set);
	//global_search(neuron,train_set);
   	//gradient_search(neuron,train_set);
   	//save_vector_to_file(convergence);
 } 
   

