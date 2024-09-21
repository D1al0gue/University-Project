public void settings(){
        size(300,300);
    }

    public void setup(){
        background(255);
    }

    public void draw(){
        fill(255,0,0);
        rect(0,0,100,300);
        
        fill(255,255,0);
        triangle(0,150,100,150,50,75);
        triangle(0,150,100,150,50,225);
        
        fill(0,255,0);
        ellipse(150,150,100,300);
        
        for (int i = 0; i < 3; i++){
          for (int j = 0; j < 5; j++){
            
           float square = 26;
           float gap = 5;
           float xPos = 210 + (square + gap) * i; // Calculate the x-coordinate for each box
           float yPos = 0 + (square + gap) * j; //calculate the y-coordinate for each box
           //I put 210 since the squares and the ellipse sort of collide.
           
           fill(0, 255, 255); // Cyan
           rect(xPos, yPos, square, square);
          }
         fill(0,0,255); 
         triangle(300,150,200,240,250,280); 
        }
    }
