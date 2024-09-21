#include "image_pr4.h"
#include <thread>
#include <chrono>
#include <vector>
#include <iostream>

using namespace std;

// You can use that
struct Orbit {
    // logged position and time
    std::vector<int> x;
    std::vector<int> y;
    std::vector<int> t;
    int xc, yc, r;  // center and radius
    int x_sunrise, y_sunrise;
    double omega = 0.1;
} orbit;

// Function to detect red pixels
void find_red_pix(Orbit& orbit, int time) {
    vector<int> redX;
    vector<int> redY;
    int totRed = 0;

    for (int row = 0; row < 900; row++) {
        for (int col = 0; col < 900; col++) {
            double r = get_pixel(image, row, col, 0);
            double g = get_pixel(image, row, col, 1);
            double b = get_pixel(image, row, col, 2);

            if (r > g * 2 && r > b * 2) {
                redX.push_back(col);
                redY.push_back(row);
                totRed++;
            }
        }
    }

    if (totRed > 20) {
        double avgX = 0;
        double avgY = 0;

        for (int i = 0; i < redX.size(); i++) {
            avgX += redX.at(i);
            avgY += redY.at(i);
        }

        avgX = avgX / redX.size();
        avgY = avgY / redY.size();
        orbit.x.push_back(avgX);
        orbit.y.push_back(avgY);
        orbit.t.push_back(time);
    } else {
        orbit.x.push_back(50);
        orbit.y.push_back(700);
        orbit.t.push_back(time);
    }
}

// Function to handle cloudy conditions
bool isCloudy() {
    // You can implement a method to detect clouds here
    // Return true if it's cloudy, otherwise false

    int redThreshold = 200;   // Minimum red component for a pixel to be considered white
    int greenThreshold = 200; // Minimum green component for a pixel to be considered white
    int blueThreshold = 200;  // Minimum blue component for a pixel to be considered white

    for (int row = 0; row < 900; row++) {
        for (int col = 0; col < 900; col++) {
            int r = get_pixel(image, row, col, 0);
            int g = get_pixel(image, row, col, 1);
            int b = get_pixel(image, row, col, 2);

            // Check if the pixel is white or light gray (indicating clouds)
            if (r >= redThreshold && g >= greenThreshold && b >= blueThreshold) {
                return true; // Clouds detected
            }
        }
    }
    return false; //Replace with your cloud detection logic
}

int main() {
    std::cout << "start..." << std::endl;
    init(1); // Init with level 1 (Completion)

    for (int time = 0; time < 950; time++) {
        // Detect red pixels and track the sun
        find_red_pix(orbit, time);

        // Check if it's cloudy
        bool cloudy = isCloudy();

        // Adjust panel behavior based on weather conditions
        if (cloudy) {
            // Handle cloudy conditions
            // Move the solar panel to a predefined position, e.g., facing east
            scene.x_aim = orbit.x.at(time);
            scene.y_aim = orbit.y.at(time);
            find_red_pix(orbit, time);
        } else {
            // Follow the sun if it's not cloudy
            scene.x_aim = orbit.x.at(time);
            scene.y_aim = orbit.y.at(time);
        }

        draw_all(time); // image is ready,
        std::cout << " time=" << time << std::endl;
        std::this_thread::sleep_for(std::chrono::milliseconds(5));
    }

    return 0;
}
