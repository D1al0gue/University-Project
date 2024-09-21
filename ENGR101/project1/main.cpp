#include <string>
#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include "Wav.hpp"
#include "ENGR101 Core/wav.hpp"

using namespace std;
int main() {
    string line;
    ifstream myfile;
    vector<double> freq_vector;
    myfile.open("e.txt");
    while (getline(myfile, line)) {
        double doub = stod(line);
        freq_vector.push_back(doub);
    }

    for (int i = 0; i <= freq_vector.size(); i++) {
        cout << freq_vector[i] << endl;
    }

    int sample_rate = 44100;
    double duration = 5;
    int n_samples = 22500;
    double dt = 1.0 / 44100.0;
    int cycle_length = 20000;
    vector<int> waveform;
    int max_vol = 6000;
    int max_time = 50;
    double attack = 0.0;
    int cycle_per_change = 5000;
    double vol_change = (double) max_vol / (double) cycle_per_change;

    /*for (double freq: freq_vector) {
        for (int i = 0; i < n_samples; i++) {
            //cout << freq << endl;
            waveform.push_back(max_vol * sin(2 * M_PI * freq * (i * dt)));
        }
    }*/

/* THE FUNCTION BELOW WILL MAKE THE MUSIC SOUND LIKE A FLUTE (ATTACK, SUSTAIN, BY RUNNING IT THROUGH A LOOP
 * BY "CONSIDERING" EVERY POSSIBLE OUTCOME AND USING THE SINE FUNCTION TO PRODUCE
 * A SOUND LIKE FLUTE. I ALSO ELEMINATED THE TIME BETWEEN EACH SAMPLES*/
    for (int i_freq = 0; i_freq <= freq_vector.size(); i_freq++) {
        attack = 1.2;
        for (int i_sample = 0; i_sample <= cycle_length; i_sample++) {
            if (i_sample <= cycle_length / 4) {
                waveform.push_back(attack * sin((2 * M_PI * freq_vector[i_freq]) * (i_sample * dt)));
                attack = attack + vol_change;
            } else if (i_sample <= cycle_length / 2) {
                attack = attack - (vol_change / 2);
                waveform.push_back(attack * sin((2 * M_PI * freq_vector[i_freq]) * (i_sample * dt)));
            } else if (i_sample <= (cycle_length * 3 / 4)) {
                waveform.push_back(attack * sin((2 * M_PI * freq_vector[i_freq]) * (i_sample * dt)));
            } else if (i_sample <= cycle_length) {
                attack = attack - (vol_change / 2);
                waveform.push_back(attack * sin((2 * M_PI * freq_vector[i_freq]) * (i_sample * dt)));
            }

        }
    }

    MakeWavFromVector("odeToJoy.wav", sample_rate, waveform);
    waveform.clear();
    return 0;
}
