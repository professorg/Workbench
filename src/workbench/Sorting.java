package workbench;

import engine.Core;
import graphics.Graphics2D;
import graphics.Window2D;
import java.util.*;
import util.Color4;
import util.Mutable;
import util.Vec2;

public class Sorting {
    
    public static final Mutable<Integer[]> data = new Mutable(null);
    public static Vec2 scale=Vec2.ZERO;
    
    public static void main(String args[]) {
        
        //Initialize Window
        Core.init();
        Window2D.background = Color4.BLACK;
        Window2D.viewPos = Window2D.UR();
        
        //Make data and scatter it
        data.o = createData(5);
        scatterData();
        
        //Size window
        scale = (Window2D.viewSize.divide(data.o.length));
        
        //Sort Data
        sort(1);
        
        //Draw Data
        Core.render.onEvent(() -> {
            for(int i=0;i<data.o.length;i++){
                Graphics2D.fillRect(new Vec2(i*scale.x,0), new Vec2(Math.ceil(scale.x*.9),scale.y*data.o[i]), Color4.WHITE);
            }
        });
        
        Core.run();
    }
    
    //Array of integers, 1 through length
    public static Integer[] createData(int length) {
        Integer[] d = new Integer[length];
        for(int i=0;i<length;i++){
            d[i] = i+1;
        }
        return d;
    }
    
    //Randomize data by switching points
    public static void scatterData() {
        for(int i=0;i<data.o.length*data.o.length;i++){
            
            //Two random numbers and a buffer integer
            int r1 = (int) (Math.floor(Math.random()*data.o.length));
            int r2 = (int) (Math.floor(Math.random()*data.o.length));
            int buffer;
            
            //Put first data in buffer,
            buffer = data.o[r1];
            
            //Put second in first,
            data.o[r1] = data.o[r2];
            
            //Put first into second (through buffer),
            data.o[r2] = buffer;
            
        }
    }
    
    public static void sort(int mode){
        //Global
        final Mutable<Boolean> sorted = new Mutable(false);
        final Mutable<Integer> i = new Mutable(0);
        
        //Partial
        final Mutable<Object> buffer = new Mutable(null);
        final Mutable<Boolean> reading = new Mutable(true);
        
        //Specific
        final Mutable<Boolean> switched = new Mutable(true);
        final Mutable<Integer> digit = new Mutable(0);
        final Mutable<ArrayList<Integer>> sub0 = new Mutable(new ArrayList());
        final Mutable<ArrayList<Integer>> sub1 = new Mutable(new ArrayList());
        final Mutable<ArrayList<Integer[]>> subs = new Mutable(new ArrayList());
        final Mutable<Boolean> splitting = new Mutable(true);

        
        switch(mode){
            case 0:
                Core.interval(.005).filter(() -> (!sorted.o)).onEvent(() -> {
                    System.out.println("Checked");
                    if(data.o[i.o]>data.o[i.o+1]){
                        buffer.o=data.o[i.o];
                        data.o[i.o]=data.o[i.o+1];
                        data.o[i.o+1]=(Integer)buffer.o;
                        System.out.println("Switched");
                        switched.o=true;
                    }
                    i.o++;
                    if(i.o==data.o.length-1) {
                        if(!switched.o) {
                            sorted.o=true;
                            System.out.println("Sorted");
                        } else {
                            switched.o=false;
                        }
                    }
                    i.o%=data.o.length-1;
                });        
            break;
            case 1:
                Core.interval(.05).filter(()->(!sorted.o)).onEvent(()->{
                    if(Arrays.equals(data.o,createData(data.o.length))){
                        System.out.println("Sorted");
                        sorted.o=true;
                    }
                    if(!sorted.o) scatterData();
                });
            break;
            case 2:
                Core.interval(.0005).filter(() -> (!sorted.o)).onEvent(()->{
                    if(reading.o){
                        if(((data.o[i.o])&(1<<digit.o))==0) {
                            sub0.o.add(data.o[i.o]);
                        } else {
                            sub1.o.add(data.o[i.o]);
                        }
                    } else {
                        if(i.o<=sub0.o.size()-1){
                            data.o[i.o]=sub0.o.get(i.o);
                        } else {
                            data.o[i.o]=sub1.o.get(i.o-sub0.o.size());
                        }
                    }
                    
                    i.o++;
                    if(i.o==data.o.length){
                        if(checkSorted(data.o)) sorted.o=true;
                        i.o%=data.o.length;
                        reading.o = !reading.o;
                        if(reading.o){
                            sub0.o.clear();
                            sub1.o.clear();
                            digit.o++;
                        }
                    }
                });
            break;
            case 3:
                subs.o.add(data.o);
                Core.interval(.5).filter(() -> (!sorted.o)).onEvent(() -> {
                    if(reading.o){
                        if(splitting.o){
                            
                        } else {
                            
                        }
                    } else {
                        if(i.o==subs.o.size()){
                            
                        }
                    }
                    i.o++;
                    data.o=merge((Integer[][]) subs.o.toArray());
                    if(splitting.o && subs.o.get(0).length==1) splitting.o=false;
                    if(i.o==data.o.length){
                        if(checkSorted(data.o)) sorted.o=true;
                        i.o%=data.o.length;
                        reading.o=!reading.o;
                    }
                });
            break;
        }
        
        
    }
    
    public static boolean checkSorted(Integer[] a){
        for(int i=0;i<a.length-1;i++){
            if(a[i]>a[i+1]) return false;
        }
        return true;
    }
    
    public static Integer[] mergeWOrder(Integer[] aa, Integer[] ba){
        Integer[] out=new Integer[aa.length+ba.length];
        ArrayList<Integer> a= new ArrayList(Arrays.asList(aa));
        ArrayList<Integer> b= new ArrayList(Arrays.asList(ba));
        
        for(int i=0;i<a.size()+b.size();i++){
            if(a.get(0)<=b.get(0)){
                out[i]=a.get(0);
                a.remove(0);
            } else {
                out[i]=b.get(0);
                b.remove(0);
            }
        }
        return out;
    }
    
    public static Integer[] merge(Integer[]... args){
        Integer[] out=args[0];
        for(int i=0;i<args.length;i++){
            for(int n=0;n<args[i].length;n++){
                out[i*args[i].length+n]=args[i][n];
            }
        }
        return out;
    }
}