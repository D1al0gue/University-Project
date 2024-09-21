#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <algorithm>
#include <map>

struct Transition {
    std::string input_signal;
    std::string dest_state;
};

struct State {
    std::string name;
    std::vector<Transition> transitions;
};

struct FSM_out {
    std::vector<State> states;
    std::string accepting_name;
    std::string initial_name;
};

std::string ReadSpecFile(std::string fileName) {
    std::cout << "Reading specifications file " << fileName << std::endl;
    std::string out;
    std::ifstream inputFile(fileName);
    if (!inputFile) {
        std::cerr << "Error: Cannot open file " << fileName << std::endl;
        exit(1);
    }
    std::string line;
    out = "";
    while (getline(inputFile, line)) {
        line.erase(std::remove(line.begin(), line.end(), '\r'), line.end());
        line.erase(std::remove(line.begin(), line.end(), '\n'), line.end());
        line.erase(std::remove(line.begin(), line.end(), ' '), line.end());
        out.append(line);
    }
    return out;
}

std::vector<std::string> code; // generated code

void SaveCodeFile(std::string fileName) {
    std::ofstream codeOutFile(fileName);
    if (!codeOutFile) {
        std::cerr << "Error: Cannot create file " << fileName << std::endl;
        exit(1);
    }
    for (const std::string& line : code) {
        codeOutFile << line << std::endl;
    }
    codeOutFile.close();
}

int Proc(const std::string& input, const FSM_out& fsm, std::string& state) {
    if (state == fsm.initial_name) {
        state = fsm.initial_name; // Set initial state
    } else {
        auto current_state = std::find_if(fsm.states.begin(), fsm.states.end(), [&](const State& s) {
            return s.name == state;
        });

        if (current_state != fsm.states.end()) {
            auto transition = std::find_if(current_state->transitions.begin(), current_state->transitions.end(), [&](const Transition& t) {
                return t.input_signal == input;
            });

            if (transition != current_state->transitions.end()) {
                state = transition->dest_state; // Update the state

                if (state == fsm.accepting_name) {
                    return 1; // Accepting state
                }
            }
        }
    }

    return 0; // Not in an accepting state
}

int main() {
    std::string specFileName, cppFileName;
    std::cout << "Enter specification file name: ";
    std::cin >> specFileName;
    std::cout << "Enter generated cpp file name: ";
    std::cin >> cppFileName;

    std::string specFileContents = ReadSpecFile(specFileName);

    // Parse the specification and fill the FSM_out structure
    FSM_out fsm;
    // ... (Add your parsing logic here)

    code.push_back("#include <iostream>");
    code.push_back("using namespace std;");
    code.push_back("string state;");
    code.push_back("string input;");
    code.push_back("int accept = 0;");
    code.push_back("int Proc(const string& input, const FSM_out& fsm, string& state) {");
    // ... (Generated Proc function code)
    code.push_back("}");
    code.push_back("int main() {");
    code.push_back("state = \"" + fsm.initial_name + "\";");
    code.push_back("while (1) {");
    code.push_back("cout << \" state=\" << state << \" enter signal\" << endl;");
    code.push_back("cin >> input;");
    code.push_back("accept = Proc(input, fsm, state);");
    code.push_back("cout << \" new state=\" << state << endl;");
    code.push_back("if (accept == 1) {");
    code.push_back("cout << \"FSM reached accepting state\" << endl;");
    code.push_back("break;");
    code.push_back("}");
    code.push_back("}");
    code.push_back("}");

    SaveCodeFile(cppFileName);

    return 0;
}
