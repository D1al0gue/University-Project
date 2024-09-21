#include <iostream>
#include "E101.h"

using namespace std;

//This function checks to see if the Ruby is present.
//This doesn't occur in the first 0.25 seconds, as it takes in the initial position of the pixel.
bool isRubyThere(int orgX, int orgY, int curX, int curY, bool rubySafe){
	if (curX == 0 && curY == 0){
		//If Retrieved position of the Ruby is (0,0) this means it's not on the screen and that it has been stolen.
		cout<<"WARNING: Ruby has been stolen!"<<endl;
		return false;
	} else if (orgX < (curX - 5) || orgX > (curX + 5) || orgY < (curY - 5) || orgY > (curY + 5) || rubySafe == false){
		//If the Retrieved position of the Ruby is not the same as its initial position, movement is detected and the system
		//will raise the alarm!
		//This alarm will also be raised if the Ruby has been subject in an attempted robbery.
		cout<<"WARNING: Ruby is being stolen!"<<endl;
		return false;
	} else {
		cout<<"Ruby is safe"<<endl;
		return true;
	}
}

int checkForRuby(){
	int position = 0; //Position of the first red pixel of the Ruby.
	
    //For all pixels in latest image
    for (int row = 0 ; row < 240 ; row++) {	
		for (int col = 0; col < 320; col++) {
			 //If the amount of Red in the pixel is greater than the amount of Green and Blue combined,
			 //this indicates that the certain pixel MUST be a Red pixel.
			 if (get_pixel(row, col, 0) > (get_pixel(row, col, 1) + get_pixel(row, col, 2))){
				 position = 256*(row) + (col); //We are using Bit shifting techniques to store the X,Y coordinates in one variable!
			}
		}
	}
	
	//Returns the detected position of the first Red pixel of the image. (In a single value!)
	return position;
}


//Main program block. 
int main() {
  int err = init(1); //Must have this in! (Video feed will not work.)
  cout<<"Error: "<<err<<endl;
  open_screen_stream(); //Turns on the camera and displays it. (Writes video feed into the buffer.)
  
  //Initial position of the Ruby. (Recorded at Frame 1.)
  int initialX;
  int initialY;
  
  int currentPos; //Current position of the Ruby (X,Y) stored in ONE value as you can only return one value from a function.
  
  bool rubySafe = true; //Boolean that determines whether the Ruby has moved at least once since startup.
  
	//Runs the program. Because of the "true," value in the FOR loop, it will effectively run non-stop.
    for (int countrun = 0; true; countrun++) {
	take_picture(); //Takes a picture of the Ruby
	update_screen(); //Updates the screen. (Stores picture in memory on screen.)
	currentPos = checkForRuby(); //Runs a method that checks to make sure the ruby is present.
							     //This method sets the "currentPos," variable, because the method calls returns the current position of the Ruby.

	//If the count value is 0, it will set the initial position of the Ruby.
	//If the count value is above 0, it will compare the new position with the original position to see if it has moved.
	//If it has, then there has been a theft! (OOPS)!
	if (countrun == 0){
		initialX = (currentPos >> 8);
		initialY = currentPos - (256*(currentPos >> 8));
	} else {
		//Checks position of the Ruby with the initial position, in a seperate function.
		cout<<"----------------"<<endl;
		if ( isRubyThere(initialX, initialY, (currentPos >> 8), (currentPos - (256*(currentPos >> 8))), rubySafe) ){
				rubySafe = true; //If the function returns true (i.e: Ruby has not been touched), the rubySafe boolean is true.
			} else {
				rubySafe = false; //If not (Ruby has previously moved in attempted robbery,) then the rubySafe boolean is false.
			}
		cout<<"----------------"<<endl;
	}
  }  
  close_screen_stream();
  return 0;
}
