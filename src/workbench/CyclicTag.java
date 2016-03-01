package workbench;

import engine.Core;
import engine.EventStream;
import graphics.Graphics2D;
import graphics.Window2D;
import org.lwjgl.opengl.Display;
import util.Color4;
import util.Vec2;

public class CyclicTag {
    
    public static void main(String[] args){
       System.out.println("Start of program");
       
       //Init
       Core.init();
       Window2D.background = Color4.RED;
       int scale = 50;
       
       //Cyclic Tag
       String[] tags = {"11", "10"};
       int p=0;
       String s = "1";
       
       System.out.println("Vars initialized");
       
       for(int i=0;i<Core.screenHeight/scale;i++){
           Core.update.forEach(t -> Display.setTitle("FPS: " + 1 / t));
           for(int n=0;n<s.length();n++){
               if(s.charAt(n)=='0') {
                   Graphics2D.drawRect(new Vec2(i*scale,n*scale), new Vec2(scale,scale), Color4.WHITE);
               } else if(s.charAt(n)=='1') {
                   Graphics2D.drawRect(new Vec2(i*scale,n*scale), new Vec2(scale,scale), Color4.BLACK);
               }
           }
           if(s.charAt(0) == '1') s+=tags[p];
           s=s.substring(1);
           p=(p+1)%(tags.length);
           System.out.println("Finished Drawing for line " + i + ".");
           
       }
       
       Core.run();
    }
    
    
}