PImage[] characterSpritesIdleLeft = new PImage[4];
PImage[] characterSpritesIdleRight = new PImage[4];
PImage[] characterSpritesRunLeft = new PImage[7];
PImage[] characterSpritesRunRight = new PImage[7];
PImage[] characterSpritesJump = new PImage[10]; // Jumping animation frames
int currentJumpFrame = 0; // Current frame of the jumping animation
boolean isMovingLeft = false;
boolean isMovingRight = false;
boolean hasMovedLeft = false;
boolean hasMovedRight = true; // Initial idle position
boolean isJumping = false;
float jumpForce = -10.0; // Adjust the jump force as needed
float characterY; // Character's vertical position
float gravity = 0.5; // Gravity force

PImage[] backgroundImages = new PImage[3];

void setup() {
  background(255);
  size(800, 350);

  // Load character idle sprites for left and right directions
  for (int i = 0; i < 4; i++) {
    characterSpritesIdleLeft[i] = loadImage("Idle0" + i + "_l.png");
    characterSpritesIdleRight[i] = loadImage("Idle0" + i + ".png");
  }

  // Load running animation sprites for left direction
  for (int i = 0; i < 7; i++) {
    characterSpritesRunLeft[i] = loadImage("run0" + i + "_l.png");
  }

  // Load running animation sprites for right direction
  for (int i = 0; i < 7; i++) {
    characterSpritesRunRight[i] = loadImage("run0" + i + ".png");
  }

  // Load jumping animation sprites
  for (int i = 0; i < 10; i++) {
    characterSpritesJump[i] = loadImage("jumpmid" + i + ".png");
  }

  // Load background sprites
  backgroundImages[0] = loadImage("bg_layer_1.png");
  backgroundImages[1] = loadImage("bg_layer_2.png");
  backgroundImages[2] = loadImage("bg_layer_3.png");

  camera = new Camera(); // Initialize the camera object
  characterY = height / 2; // Set initial vertical position of the character
}

void draw() {
  // Apply gravity to the character
  characterY += gravity;

  // Prevent the character from falling below the ground
  if (characterY > height / 2) {
    characterY = height / 2;
    isJumping = false; // Allow jumping again when on the ground
  }

  // Draw background sprites based on camera position
  float offsetX = camera.x;
  image(backgroundImages[2], 0 - offsetX * 1/3, 0);
  image(backgroundImages[1], 0 - offsetX * 2/3, 0);
  image(backgroundImages[0], 0 - offsetX, 0);

  // Check if the character is moving left or right
  if (isMovingLeft) {
    // Display running animation for left direction
    image(characterSpritesRunLeft[currentJumpFrame], width / 2, characterY);
    hasMovedLeft = true;
    hasMovedRight = false;
  } else if (isMovingRight) {
    // Display running animation for right direction
    image(characterSpritesRunRight[currentJumpFrame], width / 2, characterY);
    hasMovedLeft = false;
    hasMovedRight = true;
  } else if (isIdleLeft()) {
    // Display idle character sprite facing left
    image(characterSpritesIdleLeft[currentJumpFrame], width / 2, characterY);
  } else if (isIdleRight()) {
    // Display idle character sprite facing right
    image(characterSpritesIdleRight[currentJumpFrame], width / 2, characterY);
  }

  // Check if the character is jumping
  if (isJumping) {
    // Display jumping animation frames
    image(characterSpritesJump[currentJumpFrame], width / 2, characterY);

    // Advance to the next frame of the jumping animation
    currentJumpFrame = (currentJumpFrame + 1) % 10;
  }
}

boolean isIdleLeft() {
  return !isMovingLeft && !isMovingRight && hasMovedLeft;
}

boolean isIdleRight() {
  return !isMovingLeft && !isMovingRight && hasMovedRight;
}

class Camera {
  float x;
  float y;
}

Camera camera;

void keyPressed() {
  if (key == 'a') {
    // Move character left
    camera.x -= 5;  // Adjust the speed as needed
    isMovingLeft = true;
    isMovingRight = false;
  } else if (key == 'd') {
    // Move character right
    camera.x += 5;  // Adjust the speed as needed
    isMovingRight = true;
    isMovingLeft = false;
  } else if (key == ' ' && !isJumping) {
    // Jump if spacebar is pressed and not already jumping
    characterY -= jumpForce;
    isJumping = true;
  }
}

void keyReleased() {
  // Stop character movement when keys are released
  if (key == 'a' || key == 'd') {
    isMovingLeft = false;
    isMovingRight = false;
  }
}
