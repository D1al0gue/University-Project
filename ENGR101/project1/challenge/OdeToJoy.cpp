#include <string>
#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include "wav.hpp"
#include <algorithm>

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

    /*for (int i = 0; i <= freq_vector.size(); i++) {
        cout << freq_vector[i] << endl;
    }*/
    myfile.close();

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

    //Play sound (no modifications)
    /*for (double freq: freq_vector) {
        for (int i = 0; i < n_samples; i++) {
            //cout << freq << endl;
            waveform.push_back(max_vol * sin(2 * M_PI * freq * (i * dt)));
        }
    }*/

/* THE FUNCTION BELOW WILL MAKE THE MUSIC SOUND LIKE A DEEP BASS FLUTE (ATTACK, SUSTAIN, BY RUNNING IT THROUGH A LOOP
 * BY EACH WAVE TRAVELLING THROUGH A FOR LOOP FOR EVERY POSSIBLE OUTCOME AND USING THE SINE FUNCTION TO PRODUCE
 * A SOUND LIKE FLUTE. I ALSO ELEMINATED THE TIME BETWEEN EACH SAMPLES*/
    for (int i_freq = 0; i_freq <= freq_vector.size(); i_freq++) {
        attack = 2;
        for (int i_sample = 0; i_sample <= cycle_length; i_sample++) {
            double sin_Func = attack * sin((2 * M_PI * freq_vector[i_freq]) * (i_sample * dt));

            //attack (1/4th of the wave)
            if (i_sample <= cycle_length * 1 / 7) {
                waveform.push_back(sin_Func);
                attack = attack + vol_change;

                //decay (1/2nd of the wave duration)
            } else if (i_sample <= cycle_length * 2 / 7) {
                attack = attack - (vol_change / 2);
                waveform.push_back(sin_Func);

               //sustain (3/4th of the wave duration)
            } else if (i_sample <= (cycle_length * 3 / 7)) {
                waveform.push_back(sin_Func);

             //release (basically [or literally] the same with decay)
            } else if (i_sample <= cycle_length * 4 / 7) {
                attack = attack - (vol_change / 4);
                waveform.push_back(sin_Func);

            } else if (i_sample <= (cycle_length * 5 / 7)) {
                waveform.push_back(sin_Func);
            }
            else if (i_sample <= (cycle_length * 6 / 7)) {
                attack = attack - (vol_change / 2);
                waveform.push_back(sin_Func);
            }
            else if (i_sample <= (cycle_length)) {
                waveform.push_back(sin_Func);
                attack = (attack / 2) + vol_change;
            }

        }
    }
    MakeWavFromVector("OdeToJoy.wav", sample_rate, waveform);
    waveform.clear();
    return 0;
}

