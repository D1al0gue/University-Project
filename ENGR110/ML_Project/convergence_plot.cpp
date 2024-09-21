/* for assignment 1 - plot division boundary*/ 

#include <fstream>
#include <string>
#include <string>
#include <vector>
#include <thread>
#include <chrono>
#include <iostream>
#include <iomanip>
#include <sstream>
#include <algorithm>
#include <math.h>

//activation function
double sigmoid(double x){
    return 1.0 / (1.0 + exp(-x));
}

struct Point2Dd{
    double x,y;
};

struct RGB{int r;int g; int b;};

std::string rgb_to_string(RGB c){
    if ((c.r==-1)&&(c.g==-1)&&(c.b==-1))
    return "transparent";
    return "\"rgb(" +std::to_string(c.r)+","+ std::to_string(c.g)+","+std::to_string(c.b)+")\"";
}

struct Graph{
	std::vector<Point2Dd> points;
	double x_max=-1000.0;
	double x_min=1000.0;
	double y_max=-1000.0;
	double y_min=1000.0;
	std::string name;
	void add_point(double x, double y);
	void print_graph();
	RGB rgb;
};

void Graph::add_point(double x, double y){
	Point2Dd new_point;
	new_point.x = x; new_point.y = y;
	x_min = std::min(x,x_min);
	if (y<y_min) y_min = y;
	if (x>x_max) x_max = x;
	if (y>y_max) y_max = y;
	points.push_back(new_point);
}

void Graph::print_graph(){
	std::cout<<" Graph:"<<name<<std::endl;
	std::cout<<" x_min="<<x_min<<" x_max="<<x_max<<" y_min="<<y_min<<" y_max="<<y_max<<std::endl;
	std::for_each(points.begin(),points.end(),[](Point2Dd p){ std::cout<<p.x<<"  "<<p.y<<std::endl;});
}

std::string make_svg_polyline(Graph& g, int margin, int h, double scale_x,double scale_y){
	//std::string out ="";
	std::string plot_polyline_str = " <polyline points=\"";
    for (unsigned int j = 0 ; j < g.points.size() ; j++){ 
        double x = margin  + g.points.at(j).x*scale_x;
        double y = h - (margin + g.points.at(j).y * scale_y);
        plot_polyline_str += std::to_string(x) + " " + std::to_string(y)+" "; 
    }
    plot_polyline_str += "\" stroke="+ rgb_to_string( g.rgb )  +" fill=\"none\" stroke-width=\"2\"/>\n";
    return plot_polyline_str;    
	
}

Graph read_file_into_graph(std::string file_name){
	std::cout<<" reading file "<<file_name<<std::endl;
	Graph read_graph;
	std::ifstream in_file(file_name);
    if(!in_file){ // is the file open.
        std::cout<<"Error opening output file"<<std::endl;
        exit(1);
    }
    read_graph.name = file_name;
    std::string line_in;
    while (std::getline(in_file, line_in)){
	    int dlim = line_in.find(' ');
        std::string xs = line_in.substr(0, dlim);
        std::string ys = line_in.substr(dlim+1, line_in.length() - dlim);
        //std::cout<<"  xs="<<xs<<"  ys="<<ys<<std::endl;
       // int st; std::cin>>st;
        //std::cout<<"  x="<<std::stod(xs)<<"  y="<<std::stod(ys)<<std::endl;
        read_graph.add_point(std::stod(xs),std::stod(ys));
	}
	
	return read_graph;
}




// round decimal value to 2 decimal digits, return as string
std::string round_str(double var){
  std::stringstream stream;
  stream << std::fixed << std::setprecision(2) << var;
  return stream.str();
}

std::string circle(int cx, int cy, int r,RGB stroke,RGB fill){
    return "<circle cx=\"" + std::to_string(cx) + "\" cy=\"" + std::to_string(cy) +   
                                              "\" r=\""   + std::to_string(r)  + 
                                              "\" stroke=" + rgb_to_string(stroke)  +
                                              " fill="+ rgb_to_string(fill)+
                                              " stroke-width=\"1\"/>\n";
}

std::string rect(int x,int y, int w, int h,RGB stroke,RGB fill){
   return"<rect x=\"" + std::to_string(x) +  "\" y=\"" + std::to_string(y) +   
               "\" width=\""   + std::to_string(w)  + "\" height=\""   + std::to_string(h)  + 
                "\" stroke=" + rgb_to_string(stroke)  + " fill="+ rgb_to_string(fill)+
                                              " stroke-width=\"1\"/>\n";
}

std::string line(int x1,int y1, int x2, int y2, RGB stroke,int width){
	return "<line x1=\""+ std::to_string(x1) + "\" x2=\""+ std::to_string(x2) + 
	            "\" y1=\""+ std::to_string(y1) + "\" y2=\""+ std::to_string(y2) +
	            "\" stroke=" + rgb_to_string(stroke) + " stroke-width=\""+ std::to_string(width)+ "\"/>\n";

}

std::string text(int x1,int y1, RGB col, std::string txt){
	return "<text x=\""+std::to_string(x1)+"\" y=\""+ std::to_string(y1)  +"\" fill="+ rgb_to_string(col)+ ">"+txt+"</text>\n";
}

int main(){
    
    
    // get list of files    
    std::cout<<" Enter list of comma-separated file names"<<std::endl;
    std::string input_string;
    std::getline (std::cin,input_string);
    // parse the list
    std::vector<std::string> file_names;
    std::stringstream ss( input_string);
    while( ss.good() ){
       std::string substr;
       getline( ss, substr, ',' );
       file_names.push_back( substr );
    }
    
    //std::for_each(file_names.begin(), file_names.end(), [](std::string s) { std::cout<<s<<std::endl; });
    std::vector<Graph> graphs;
    std::for_each(file_names.begin(), file_names.end(), [&graphs](std::string s) { 
		graphs.push_back( read_file_into_graph(s)); });
	
     std::vector<RGB> pallette;
     pallette.push_back({255,0,0});	
     pallette.push_back({0,255,0});	
     pallette.push_back({0,0,255});	
     pallette.push_back({0,0,0});	
     pallette.push_back({100,100,255});	
     pallette.push_back({200,200,255});	

   //  std::for_each(graphs.begin(), graphs.end(), [](Graph g){ print_graph(g);} );
     // assign colors
     int count = 0;
     std::for_each(graphs.begin(), graphs.end(), [pallette, &count](Graph& g){ g.rgb = pallette.at(count); count++;});
   
    
     double x_min= 10000.0;     double x_max= -10000.0;     double y_min= 10000.0;     double y_max= -10000.0;
     std::for_each(graphs.begin(), graphs.end(), [&x_min,&x_max,&y_min,&y_max](Graph& g){ 
		 if (g.x_min<x_min) x_min = g.x_min;
		 if (g.y_min<y_min) y_min = g.y_min;
		 if (g.x_max>x_max) x_max = g.x_max;
		 if (g.y_max>y_max) y_max = g.y_max;
		 ;});
     
     double w = 700;      double h = 500;
     double margin = 50;
     double scale_x =  (w-2*margin)/(x_max-x_min);
     double scale_y =  (h-2*margin)/(y_max-y_min);
     
    
     std::vector<std::string> svg_out; // output svg file
     svg_out.clear();
     svg_out.push_back("<?xml version=\"1.0\" standalone=\"no\"?>\n");
     svg_out.push_back("<svg width=\""+ std::to_string(w)+"\" height=\""+ std::to_string(h) +"\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\"    xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n");
            
      //  draw plot boundary
     svg_out.push_back("<rect x=\"0\" y=\"0\" width=\""+ std::to_string(w)+"\" height=\""+ std::to_string(h) +"\" stroke=\"black\" fill=\"none\" stroke-width=\"1\"/>\n");
    
     for (unsigned int i = 0; i < graphs.size(); i++){
		 std::string pps = make_svg_polyline(graphs.at(i), margin, h, scale_x,  scale_y);
         svg_out.push_back(pps);
     }
     //draw pallette
     for (unsigned int i = 0; i < graphs.size(); i++){
		 //std::string pps = make_svg_polyline(graphs.at(i), margin, h, scale_x,  scale_y);
         svg_out.push_back(  text(w-2*margin,margin+ i*20,graphs.at(i).rgb, graphs.at(i).name )   );
     }
      
     // draw axes
     svg_out.push_back( line(margin,h-margin,w-margin,h-margin,{100,100,100},2) );
     svg_out.push_back( line(margin,h-margin,margin,margin,{100,100,100},2) );
     svg_out.push_back( text(5,h,{0,0,0},round_str(x_min) ));
     //svg_out.push_back("<text x=\"5\" y=\""+ std::to_string(h)  +"\" fill=\"black\">"+round_str(x_min)+"</text>\n");
     svg_out.push_back("<text x=\"" + std::to_string(w-margin-10+5) +"\" y=\""+ std::to_string(h)+"\" fill=\"black\">"+round_str(x_max)+"</text>\n");
     svg_out.push_back("<text x=\"250\" y=\""+ std::to_string(h)  +"\" fill=\"black\">"+" step"+"</text>\n");
     svg_out.push_back("<text x=\"5\" y=\""+ std::to_string(margin)  +"\" fill=\"black\">"+ round_str(y_max)+"</text>\n");
     svg_out.push_back("<text x=\"5\" y=\""+ std::to_string(h-margin)  +"\" fill=\"black\">"+round_str(y_min)+"</text>\n");
     svg_out.push_back("<text x=\"5\" y=\""+ std::to_string(250)  +"\" fill=\"black\">"+ "err"+"</text>\n");
     svg_out.push_back("</svg>\n");

     //save svg picture to file
     std::ofstream fout;
     std::cout<<" Enter picture file name. Should end with \".svg\""<<std::endl;
     std::string sfn;
     std::cin>>sfn;        
	 fout.open(sfn,std::ofstream::binary);
     for (std::string s:svg_out){
          fout<<s;
     }
     fout.close();
     std::string command = "firefox "+ sfn; 
     system(command.c_str());

}

