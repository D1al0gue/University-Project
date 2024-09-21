#include <iostream>
#include <string>

using namespace std;

string state;

int ProcessChar(char in_char) {
    int accept = 0;
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
        } else if (in_char != 'a') {
            state = "s0";  // Reset if not 't' or 'a'
        }
    } else if (state == "s3") {
        // Stay in accepting state if more characters are encountered
    }
    return accept;
}

int main() {
    string input;
    cout << "Enter input string: ";
    getline(cin, input);
    cout << "Input is " << input << " state is " << state << endl;
    bool string_accepted = false;
    state = "s0";
    for (char s : input) {
        string_accepted = ProcessChar(s);
    }

    // Check if the FSM ended in the accepting state and the last characters are "cat"
    if (state == "s3") {
        cout << "Accepted" << endl;
    } else {
        cout << "Rejected" << endl;
    }

    return 0;
}