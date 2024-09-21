int tower_size = 50;
float enemy_size = 10;
int towerPosX;
int towerPosY;
float circleRadius = 150; // Adjust the circle radius as needed

// GAMEPLAY MECHANICS AND BASICS
int roundCount = 1; // Start from round 1
int enemyHP = 3;
int towerHP = 15;
int towerAttack = 3;
float towerAttackSpeed = 2000; // Initial attacking speed in milliseconds
float towerRange = 150; // Initial tower range
int money = 300; // initial starting cash
int enemy_killed_in_round = 0; // initial enemy killed in that particular round.
int enemy_needed_for_advancing = 10;
float enemyX, enemyY;
float enemySpeed = 1; // will increase as the game goes
int moneyPerKill = 10;
int bossHP = 30;
int bossDamage = 5;
int maxTargets = 1;

ArrayList<Enemy> enemies = new ArrayList<Enemy>();
ArrayList<Boss> bosses = new ArrayList<Boss>(); // Add a list for bosses
ArrayList<Targetable> targets = new ArrayList<Targetable>();

// Tower shooting variables
boolean canShoot = true;
float lastShotTime = 0;
PVector shotStart; // Starting point of the shot
PVector shotVelocity; // Velocity vector of the shot

// Upgrade buttons
Button attackSpeedButton;
Button damageButton;
Button healthPointsButton;
Button rangeButton;
Button targetsButton;

PImage[] backgroundImages = new PImage [1]; 

void setup() {
  size(600, 600);
  background(100);
  towerPosX = width / 2;
  towerPosY = height / 2;
  shotStart = new PVector(towerPosX, towerPosY);
  shotVelocity = new PVector(1, 0); // Initial velocity
  shotVelocity.setMag(5); // Set the magnitude of the velocity

  // Create upgrade buttons
  attackSpeedButton = new Button(width - 150, 10, "Upgrade Speed: $50");
  damageButton = new Button(width - 150, 50, "Upgrade Damage: $50");
  healthPointsButton = new Button(width - 150, 90, "Upgrade Health: $50");
  rangeButton = new Button(width - 150, 130, "Upgrade Range: $50");
  targetsButton = new Button(width - 150, 170, "Upgrade Targets: $300");
  
  backgroundImages [0] = loadImage("imgs/background.png");
}

void drawEnemy() {
  fill(200, 0, 0);
  for (int i = 0; i < enemies.size(); i++) {
    Enemy enemy = enemies.get(i);
    enemy.moveTowardsTower();
    enemy.displayHealthBar(-enemy_size / 2, enemy_size / 2 + 5, enemyHP);
    ellipse(enemy.x, enemy.y, enemy_size, enemy_size);
    // Check if enemy touches the tower
    float distanceToTower = dist(enemy.x, enemy.y, towerPosX, towerPosY);
    if (distanceToTower < tower_size / 2 + enemy_size / 2) {
      // Enemy touches the tower and deals damage
      towerHP -= 1; // Deduct tower health
      enemies.remove(i); // Remove the enemy
    }
  }
}

void drawBosses() {
  fill(0, 0, 255); // Blue color for bosses
  for (int i = 0; i < bosses.size(); i++) {
    Boss boss = bosses.get(i);
    boss.moveTowardsTower();
    boss.displayHealthBar(-enemy_size / 2, enemy_size / 2 + 5, bossHP);
    ellipse(boss.x, boss.y, enemy_size * 2, enemy_size * 2); // Larger size for bosses
    // Check if boss touches the tower
    float distanceToTower = dist(boss.x, boss.y, towerPosX, towerPosY);
    if (distanceToTower < tower_size / 2 + enemy_size) {
      // Boss touches the tower and deals damage
      towerHP -= bossDamage; // Deduct tower health
      bosses.remove(i); // Remove the boss
    }
  }
}

void draw() {
  // Clear the background
  background(100);
  
  image(backgroundImages[0], -85, -85);
  // Draw the circle around the tower
  noFill(); // No fill color for the circle
  stroke(255, 0, 0); // Set the stroke color to red
  strokeWeight(2); // Set the stroke thickness

  // Calculate the center of the circle (same as the tower's position)
  float circleCenterX = towerPosX;
  float circleCenterY = towerPosY;

  // Update the circle radius based on towerRange
  circleRadius = towerRange;

  // Draw the circle
  ellipse(circleCenterX, circleCenterY, circleRadius * 2, circleRadius * 2);

  // Draw the tower in the center of the canvas
  fill(0, 200, 200);
  rect(towerPosX - (tower_size / 2), towerPosY - (tower_size / 2), tower_size, tower_size);

  // Code for other drawing and game logic

  // Display the round count
  fill(255); // Set the text color to white
  textSize(20); // Set the text size
  textAlign(CENTER, TOP); // Center the text horizontally, align it to the top
  text("Round: " + roundCount, width / 2, 10); // Display the round count at the top of the canvas
  text("Money: $" + money, width / 2, 40); // Display the amount of money

  // Display the tower's health
  fill(255, 0, 0); // Red color for health bar
  rect(towerPosX - (tower_size / 2), towerPosY + (tower_size / 2) + 10, tower_size, 5); // Red health bar background
  fill(0, 255, 0); // Green color for remaining health
  float healthBarWidth = map(towerHP, 0, 15, 0, tower_size);
  rect(towerPosX - (tower_size / 2), towerPosY + (tower_size / 2) + 10, healthBarWidth, 5); // Green health bar

  // Check if the tower's health reaches zero
  if (towerHP <= 0) {
    // Tower is destroyed, game over logic here
    fill(255, 0, 0);
    textSize(32);
    textAlign(CENTER, CENTER);
    text("Game Over", width / 2, height / 2);
    noLoop(); // Stop the game loop
  }

  // Check for button clicks and perform upgrades
  if (attackSpeedButton.isClicked() && money >= 50) {
    towerAttackSpeed *= 0.9; // Increase attack speed by reducing shootInterval
    money -= 50; // Deduct money
    delay(100);
    attackSpeedButton.label = "Upgrade Speed: $50";
  }
  if (damageButton.isClicked() && money >= 50) {
    towerAttack += 1; // Increase tower damage
    money -= 50; // Deduct money
    delay(100);
    damageButton.label = "Upgrade Damage: $50";
  }
  if (healthPointsButton.isClicked() && money >= 50) {
    towerHP += 5; // Increase tower health points
    money -= 50; // Deduct money
    delay(100);
    healthPointsButton.label = "Upgrade Health: $50";
  }
  if (rangeButton.isClicked() && money >= 50) {
    towerRange += 20; // Increase tower range
    money -= 50; // Deduct money
    delay(100);
    rangeButton.label = "Upgrade Range: $50";
  }
  
  if (targetsButton.isClicked() && money >= 300 && maxTargets < 4) {
    maxTargets++; // Increase the maximum number of targets
    money -= 300; // Deduct money
    delay(100);
    targetsButton.label = "Upgrade Targets: $" + (300 + (maxTargets - 1) * 100); // Update the label
  }

  // Spawn enemies randomly from all edges at the start of a new round
  if (enemy_killed_in_round >= enemy_needed_for_advancing) {
    // Start a new round
    roundCount++;
    enemy_killed_in_round = 0;
    enemyHP += 1; // Increase enemy health for the next round
  }

  if (frameCount % 60 == 0) {
    float spawnX, spawnY;
    int edge = int(random(4)); // Randomly choose an edge (0 to 3)
    if (edge == 0) {
      // Spawn from the top edge
      spawnX = random(width);
      spawnY = -enemy_size / 2;
    } else if (edge == 1) {
      // Spawn from the bottom edge
      spawnX = random(width);
      spawnY = height + enemy_size / 2;
    } else if (edge == 2) {
      // Spawn from the left edge
      spawnX = -enemy_size / 2;
      spawnY = random(height);
    } else {
      // Spawn from the right edge
      spawnX = width + enemy_size / 2;
      spawnY = random(height);
    }
    
    if (roundCount % 10 == 0 && bosses.isEmpty()) {
      // Boss wave: Spawn a super strong but slow boss
      bosses.add(new Boss(spawnX, spawnY, enemySpeed, bossHP));
      targets.add(bosses.get(bosses.size() - 1)); // Add the boss to the targets list
    } else {
      // Normal wave: Spawn regular enemies
      enemies.add(new Enemy(spawnX, spawnY, enemySpeed, enemyHP));
      targets.add(enemies.get(enemies.size() - 1)); // Add the enemy to the targets list
    }
  }

  // Tower attack logic
  if (millis() - lastShotTime >= towerAttackSpeed) {
    canShoot = true;
  }

  if (canShoot) {
    Targetable currentTarget = null;

    // Find a new target (Enemy or Boss)
    for (int i = targets.size() - 1; i >= 0; i--) {
      Targetable target = targets.get(i);
      float distance = dist(towerPosX, towerPosY, target.getX(), target.getY());
      if (distance <= towerRange) {
        currentTarget = target;
        break;
      }
    }

    if (currentTarget != null) {
      // Calculate the trajectory of the shot towards the target
      shotVelocity = PVector.sub(new PVector(currentTarget.getX(), currentTarget.getY()), shotStart);
      shotVelocity.setMag(5); // Set the shot speed

      // Draw the trajectory
      stroke(0, 255, 0); // Green color for trajectory
      line(shotStart.x, shotStart.y, shotStart.x + shotVelocity.x * 10, shotStart.y + shotVelocity.y * 10);

      // Update last shot time and prevent rapid shooting
      lastShotTime = millis();
      canShoot = false;

      currentTarget.decreaseHealth(towerAttack);

      if (currentTarget.isDead()) {
        if (currentTarget instanceof Boss) {
          bosses.remove(currentTarget);
        } else if (currentTarget instanceof Enemy) {
          enemies.remove(currentTarget);
        }

        enemy_killed_in_round++;
        money += 10; // Earn money for killing a target
        targets.remove(currentTarget); // Remove the target from the list
      }
    }
  }

  // Update and draw enemies and bosses
  drawEnemy();
  drawBosses();

  // Draw upgrade buttons
  attackSpeedButton.display();
  damageButton.display();
  healthPointsButton.display();
  rangeButton.display();
  targetsButton.display();
}

interface Targetable {
  float getX();
  float getY();
  void decreaseHealth(int damage);
  boolean isDead();
}

class Enemy implements Targetable {
  float x, y;
  float speed;
  int health;

  Enemy(float x, float y, float speed, int health) {
    this.x = x;
    this.y = y;
    this.speed = speed;
    this.health = health;
  }

  // Implement Targetable interface methods
  float getX() {
    return x;
  }

  float getY() {
    return y;
  }

  void decreaseHealth(int damage) {
    health -= damage;
  }

  boolean isDead() {
    return health <= 0;
  }

  void moveTowardsTower() {
    float angle = atan2(towerPosY - y, towerPosX - x);
    float distance = dist(x, y, towerPosX, towerPosY);

    if (distance > tower_size / 2 + enemy_size / 2) {
      x += cos(angle) * speed;
      y += sin(angle) * speed;
    }
  }

  void displayHealthBar(float offsetX, float offsetY, int maxHealth) {
    float barWidth = map(health, 0, maxHealth, 0, enemy_size);
    fill(255, 0, 0);
    noStroke();
    rect(x - enemy_size / 2 + offsetX, y + offsetY, enemy_size, 5);
    fill(0, 255, 0);
    rect(x - enemy_size / 2 + offsetX, y + offsetY, barWidth, 5);
  }
}

class Boss implements Targetable {
  float x, y;
  float speed;
  int health;

  Boss(float x, float y, float speed, int health) {
    this.x = x;
    this.y = y;
    this.speed = speed;
    this.health = health;
  }

  // Implement Targetable interface methods
  float getX() {
    return x;
  }

  float getY() {
    return y;
  }

  void decreaseHealth(int damage) {
    health -= damage;
  }

  boolean isDead() {
    return health <= 0;
  }

  void moveTowardsTower() {
    float angle = atan2(towerPosY - y, towerPosX - x);
    float distance = dist(x, y, towerPosX, towerPosY);

    if (distance > tower_size / 2 + enemy_size) {
      x += cos(angle) * speed;
      y += sin(angle) * speed;
    }
  }

  void displayHealthBar(float offsetX, float offsetY, int maxHealth) {
    float barWidth = map(health, 0, maxHealth, 0, enemy_size * 2); // Double the size for bosses
    fill(255, 0, 0);
    noStroke();
    rect(x - enemy_size + offsetX, y + offsetY, enemy_size * 2, 5); // Double the size for health bar
    fill(0, 0, 255); // Blue color for boss health
    rect(x - enemy_size + offsetX, y + offsetY, barWidth, 5);
  }
}

class Button {
  float x, y;
  float w, h;
  String label;
  color defaultColor;
  color hoverColor;
  color clickColor;
  boolean isClicked = false;

  Button(float x, float y, String label) {
    this.x = x;
    this.y = y;
    this.w = 140;
    this.h = 30;
    this.label = label;
    this.defaultColor = color(200);
    this.hoverColor = color(255, 0, 0); // Bright red when the mouse hovers
    this.clickColor = color(0, 0, 255); // Bright blue when the button is clicked
  }

  boolean isClicked() {
    if (mousePressed && mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h) {
      isClicked = true;
    } else {
      isClicked = false;
    }
    return isClicked;
  }

  void display() {
    if (isHovered()) {
      fill(hoverColor);
    } else if (isClicked()) {
      fill(clickColor);
    } else {
      fill(defaultColor);
    }

    stroke(0);
    rect(x, y, w, h);
    fill(0);
    textSize(12);
    textAlign(CENTER, CENTER);
    text(label, x + w / 2, y + h / 2);
  }
  
  boolean isHovered() {
    return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
  }
}
