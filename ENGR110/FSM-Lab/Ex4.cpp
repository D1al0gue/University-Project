#include <iostream>
#include <string>

using namespace std;

string state;

int ProcessChar(char in_char) {
    int accept = 0;
    if (state == "s0") {
        if (in_char == ',') {
            // Ignore repeated commas
        } else {
            state = "s1";
            cout << in_char;  // Output the character
        }
    } else if (state == "s1") {
        if (in_char == ',') {
            state = "s2";
        } else {
            cout << in_char;  // Output the character
        }
    } else if (state == "s2") {
        if (in_char != ',') {
            state = "s1";
            cout << "," << in_char;  // Output a single comma and the character
        }
    }
    return accept;
}

int main() {
    string input;
    cout << "Enter input string: ";
    getline(cin, input);
    cout << "Input is " << input << endl << endl << " state is " << state << endl << endl;
    bool string_accepted = false;
    state = "s0";
    for (char s : input) {
        string_accepted = ProcessChar(s);
    }
    cout << endl;  // Add a newline for the final output
    return 0;
}
