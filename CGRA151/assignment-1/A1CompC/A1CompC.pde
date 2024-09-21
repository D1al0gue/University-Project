void setup() {
size(400, 300);
  frameRate(60);
  /*frame rate needs to be 60 to make it seems continuous as the monitor 
  I'm using currently only supports 60 fps)*/
}

void draw() {
  background(255);
  if (mousePressed){
    fill(0,255,0);
  }else{
    fill(255,0,0);
  }
  ellipse(mouseX, mouseY, 40, 40);
}
