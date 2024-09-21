float angle = 0.0;
float rotationSpeed = 0.005;
int rectSize = 50;
color[] colors = { 
  color(255, 0, 0), 
  color(255, 255, 0), 
  color(0, 255, 0), 
  color(0, 255, 255), 
  color(0, 0, 255) 
};
int colorIndex = 0;

void setup() {
  size(400, 400);
  rectMode(CENTER);
}

void draw() {
  background(0);
  translate(width / 2, height / 2);
  rotate(angle);
  
  for (int i = 0; i < 8; i++) {
    fill(colors[colorIndex]);
    rect(0, 0, rectSize, rectSize);
    rotate(PI / 4);
  }
  
  angle += rotationSpeed;
}

void mousePressed() {
  colorIndex = (colorIndex + 1) % colors.length;
}

void mouseWheel(MouseEvent event) {
  float delta = event.getCount();
  rotationSpeed += delta * 0.01;
}
