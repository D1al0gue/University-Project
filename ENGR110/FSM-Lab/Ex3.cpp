#include <iostream>
#include <string>

using namespace std;

string state;

int ProcessChar(char in_char) {
    int accept = 0;
    if (state == "s0") {
        if (in_char >= '1' && in_char <= '9') {
            state = "s1";
        } else if (in_char == '0') {
            state = "s4";  // Leading zero, transition to error state
        }
    } else if (state == "s1") {
        if (isdigit(in_char)) {
            // Continue accepting digits
        } else {
            state = "s3";  // Transition to accepting state
            accept = 1;    // String accepted
        }
    } else if (state == "s4") {
        if (isdigit(in_char)) {
            state = "s2";  // Transition to non-zero digit
        }
    } else if (state == "s2") {
        if (isdigit(in_char)) {
            // Continue accepting digits
        } else {
            state = "s3";  // Transition to accepting state
            accept = 1;    // String accepted
        }
    }
    return accept;
}

int main() {
    string input;
    cout << "Enter input string: ";
    getline(cin, input);
    cout << "Input is " << input << endl;
    bool string_accepted = false;
    state = "s0";
    for (char s : input) {
        string_accepted = ProcessChar(s);
    }
    if (state == "s4") {
        cout << "Error: Leading zero detected!" << endl;
    } else {
        cout << "Accepted = " << string_accepted << endl;
    }
    return 0;
}
