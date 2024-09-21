size(400,400);

background(211,211,211);

for (int row = 0; row < 20; row++){
   for (int col = 0; col < 20; col++){
     
      fill(random(40, 100), 0, 140);
      
      float gap = 10;
      float ellipse_size = random(15, 30);
      float startx= 20;
      float starty= 20;
      float x = startx * col + gap;
      float y = starty * row + gap;
      
      ellipse(x, y, ellipse_size, ellipse_size);
    }
  }
