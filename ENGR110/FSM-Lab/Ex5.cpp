#include <iostream>
#include <string>
#include <vector>

using namespace std;

string state;
vector<string> tokens;

int ProcessChar(char in_char) {
    int accept = 0;
    if (state == "s0") {
        if (isdigit(in_char)) {
            state = "s1";
            string token;
            token.push_back(in_char);
            tokens.push_back(token);
        }
    } else if (state == "s1") {
        if (in_char == ',') {
            state = "s0";
        } else if (isdigit(in_char)) {
            tokens.back().push_back(in_char);
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

    // Check if the last token is missing
    if (state == "s1") {
        cout << "Error: Last token is missing!" << endl;
    }

    // Print the parsed tokens
    cout << "Parsed tokens:";
    for (const string& token : tokens) {
        cout << " " << token;
    }
    cout << endl;

    return 0;
}
