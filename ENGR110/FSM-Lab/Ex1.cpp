/*Exercise 1 of FSM asignment 1*/
/*recognize "cat" anywhere in the string*/
#include <iostream>
#include <string>

using namespace std;

string state;
int accept = 0;

int ProcessChar(char in_char) {
   
    if (state == "s0") {
        if (in_char == 'c') {
            state = "s1";
        }
    } else if (state == "s1") {
        if (in_char == 'a') {
            state = "s2";
        } else if (in_char != 'c') {
            state = "s0";  // Reset if not 'a' or 'c'
        }
    } else if (state == "s2") {
        if (in_char == 't') {
            state = "s3";
            accept = 1;  // String accepted
        } else if (in_char != 'c') {
            state = "s0";  // Reset if not 't' or 'c'
        }
    } else if (state == "s3") {
        // Stay in accepting state if more characters are encountered
    }
    return accept;
}


int main(){
   string input;
   cout<<"enter input string:";
   getline(std::cin,input);
   cout<<"Input is "<<input<<" state is"<<state<<endl;
   bool string_accepted = 0;
   state = "s0";
   for (char s:input){
	   string_accepted = ProcessChar(s);
   } 
   cout<<" accepted = "<<string_accepted<<endl;
   	
}

