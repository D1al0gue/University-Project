#include <iostream> // input-output library
#include <math.h>  // library for sin function
#include <vector>  // if using vectors
#include "wav.hpp" // make sure to include this helper library


int main() {
    int sample_rate = 44100;
    double duration = 7;
    double n_samples = sample_rate * duration;
    double dt = 1.0 / sample_rate;
    std::vector<int> waveform;
    double frequency_2 = 700;
    double frequency_1 = 950;
    double volume = 6000;


    for (int i_sample = 0; i_sample < n_samples; i_sample++) {
        int cycle = i_sample / 22000;

        if (cycle % 2 == 0) {
            waveform.push_back(volume * sin(2 * M_PI * frequency_1 * (i_sample * dt)));
        } else {
            waveform.push_back(volume * sin(2 * M_PI * frequency_2 * (i_sample * dt)));
        }
    }


    MakeWavFromVector("Ambulance.wav", sample_rate, waveform);

    waveform.clear();

    return 0;
}