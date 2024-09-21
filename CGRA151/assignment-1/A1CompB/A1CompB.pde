size(400, 400);
background(135, 206, 235); // Sky

fill(34, 139, 34); // Forest Green
noStroke();

int mountainTop = height - 250;
int mountainWidth = width;
int mountainRadius = mountainWidth / 2;

ellipse(mountainRadius, mountainTop + width, width * 3, height * 1.9);

fill(0, 100, 0); // Dark Green

stroke(0);
int numTrees = 100;
float treeSize = random(50, 65);

for (int i = 0; i < numTrees; i++) {
  int x = (int) random(width);
  int y = (int) random(mountainTop, height - treeSize);
  triangle(x, y, x - treeSize / 2, y + treeSize, x + treeSize / 2, y + treeSize);
}
